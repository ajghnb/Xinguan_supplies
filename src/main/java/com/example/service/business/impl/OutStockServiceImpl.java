package com.example.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.converter.OutStockConverter;
import com.example.common.utils.LogUtils;
import com.example.common.utils.ValidationUtils;
import com.example.dao.business.*;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.param.business.ConsumerParam;
import com.example.model.param.business.OutStockParam;
import com.example.model.po.business.*;
import com.example.service.business.OutStockService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author 18237
 */
@Service("outStockService")
public class OutStockServiceImpl implements OutStockService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ConsumerDao consumerDao;

    @Autowired
    private OutStockDao outStockDao;

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private ProductStockDao productStockDao;

    @Autowired
    private OutStockInfoDao outStockInfoDao;

    @Autowired
    private OutStockConverter outStockConverter;

    @Override
    public Page<OutStockPo> queryOutStockList(OutStockParam pageParam) {
        LogUtils.LOGGER.debug("出库列表: 分页参数:{}", pageParam);
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        return outStockDao.queryOutStockList(pageParam);
    }

    @Override
    public OutStockPo queryOutStock(Long outStockId) {
        LogUtils.LOGGER.debug("查询出库明细: outStockId:{}", outStockId);
        OutStockPo outStock = checkOutStockIsExit(outStockId);

        return outStock;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOutStock(OutStockParam param) {
        LogUtils.LOGGER.debug("{[物资出库]}:开始记录物资出库, OutStock: {}", param);
        checkOutStockConsumer(param);
        //记录该单的总数
        int itemNumber = 0;
        //随机生成发放单号
        String outStockNum = UUID.randomUUID().toString().substring(0, 32).replace("-", "");
        //获取商品明细
        List<Object> products = param.getProducts();
        if (!CollectionUtils.isEmpty(products)) {
            for (Object product : products) {
                LinkedHashMap item = (LinkedHashMap) product;
                //发放数量
                int productNumber = (int) item.get("productNumber");
                //物资编号
                Integer productId = (Integer) item.get("productId");
                ProductPo productPo = productDao.selectById(productId);
                //检查物资发放信息合法与否
                checkOutStockProduct(productNumber, productPo);
                //出库单出库物资总数
                itemNumber += productNumber;
                //新增物资发放信息
                addOutStockInfo(productNumber, outStockNum, productPo);

            }
            //新增物资出库信息
            addOutStockProduct(itemNumber, outStockNum, param);
        } else {
            throw new ApiRuntimeException(Assert.PRODUCT_OUTSTOCK_EMPTY, "物资发放不能为空");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeOutStock(Long outStockId) {
        LogUtils.LOGGER.debug("开始将物资分发移入回收站: outStockId:{}", outStockId);
        //核查是否存在指定id的物资分发
        OutStockPo outStock = checkOutStockIsExit(outStockId);
        Integer status = outStock.getStatus();
        if (status != 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, "发放单状态不正确");
        } else {
            outStock.setStatus(1);
            //更新物资分发
            updateOutStock(outStock);
        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rollbackOutStock(Long outStockId) {
        LogUtils.LOGGER.debug("开始将回收站内的物资分发信息恢复: outStockId:{}", outStockId);
        //核查是否存在指定id的物资分发
        OutStockPo outStock = checkOutStockIsExit(outStockId);
        if (outStock.getStatus() != 1) {
            throw new ApiRuntimeException(Assert.PARAMETER, "发放单状态不正确");
        } else {
            outStock.setStatus(0);
            //更新物资分发
            updateOutStock(outStock);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long outStockId) {
        LogUtils.LOGGER.debug("开始删除物资发放单: outStockId:{}", outStockId);
        //核查是否存在指定id的物资分发
        OutStockPo outStock = checkOutStockIsExit(outStockId);
        if (outStock.getStatus() != 1 && outStock.getStatus() != 2) {
            throw new ApiRuntimeException(Assert.PARAMETER, "发放单状态错误,无法删除");
        } else {
            //删除物资分发
            Assert.DB_OPERATE.sqlSuccess(outStockDao.deleteById(outStockId))
                    .orThrow(isAssert -> {
                        LogUtils.LOGGER.error("删除物资分发失败: {}", outStock);
                        return new ApiRuntimeException(isAssert);
                    });
        }
        //物资分发单号
        String outStockNum = outStock.getOutNum();
        //可能存在物资出库信息,一起删除
        QueryWrapper<OutStockInfoPo> wrapper = new QueryWrapper<OutStockInfoPo>()
                .eq(true, "out_num", outStockNum);
        outStockInfoDao.delete(wrapper);
    }

    @Override
    public void checkOutStock(Long outStockId) {
        LogUtils.LOGGER.debug("开始审核物资发放: outStockId:{}", outStockId);
        //核查出库物资
        OutStockPo outStock = checkOutStockIsExit(outStockId);
        if (outStock.getStatus() != 2) {
            throw new ApiRuntimeException(Assert.PARAMETER, "发放单状态错误");
        }
        //核查物资发放信息
        ConsumerPo consumer = consumerDao.selectById(outStock.getConsumerId());
        if (consumer == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "物资发放来源信息错误");
        }
        //发放单号,查询物资发放信息
        String outStockNum = outStock.getOutNum();
        List<OutStockInfoPo> outStockInfos = outStockInfoDao.queryOutStockInfoList(outStockNum);
        if (!CollectionUtils.isEmpty(outStockInfos)) {
            for (OutStockInfoPo outStockInfo : outStockInfos) {
                //物资编号和物资数
                String pNum = outStockInfo.getPNum();
                Integer productNumber = outStockInfo.getProductNumber();
                List<ProductPo> products = productDao.queryByPNum(pNum);
                if (products.size() > 0) {
                    ProductPo product = products.get(0);
                    //如果存在就减少数量
                    List<ProductStockPo> productStocks = productStockDao.queryListByPNum(pNum);
                    if (!CollectionUtils.isEmpty(productStocks)) {
                        //更新数量
                        ProductStockPo productStock = productStocks.get(0);
                        if (productStock.getStock() < productNumber) {
                            throw new ApiRuntimeException(Assert.PARAMETER, "物资:" + product.getName() + "的库存不足");
                        }
                        productStock.setStock(productStock.getStock() - productNumber);
                        //更新物资库存数量
                        updateProductStock(productStock);

                    } else {
                        throw new ApiRuntimeException(Assert.PARAMETER, "该物资在库存中找不到");
                    }
                    //修改入库单状态
                    outStock.setStatus(0);
                    updateOutStock(outStock);
                } else {
                    throw new ApiRuntimeException(Assert.PARAMETER, "物资编号为:[" + pNum + "]的物资不存在");
                }
            }
        } else {
            throw new ApiRuntimeException(Assert.PARAMETER, "发放物资的明细不能为空");
        }

    }


    /**
     * 核对出库单是否存在
     *
     * @param outStockId
     * @return 存在则返回对应出库单, 否则抛出异常
     */
    public OutStockPo checkOutStockIsExit(Long outStockId) {

        OutStockPo outStock = outStockDao.queryOutStock(outStockId).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询出库单失败, 找不到对应的出库单, outStockId: {}", outStockId);
            return new ApiRuntimeException(Assert.IS_EXIST, "找不到对应的出库单");
        });
        return outStock;
    }


    /**
     * 核对物资发放信息是否合法
     *
     * @param productNumber
     * @param productPo
     * @return
     */
    public void checkOutStockProduct(int productNumber, ProductPo productPo) {
        if (productPo == null) {
            throw new ApiRuntimeException(Assert.PRODUCT_NOT_FOUND, "物资未找到");
        }
        if (productNumber <= 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, productPo.getName() + "发放数量不合法,无法入库");
        }
        ProductStockPo productStock = productStockDao.queryByPNum(productPo.getPNum());
        if (productStock == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该物资在库存中不存在");
        }
        if (productNumber > productStock.getStock()) {
            throw new ApiRuntimeException(Assert.PRODUCT_STOCk, productPo.getName() + "库存不足,库存剩余:" + productStock);
        }
    }


    /**
     * 检查出库物资方
     *
     * @param param
     * @return
     */
    public void checkOutStockConsumer(OutStockParam param) {
        LogUtils.LOGGER.debug("{[物资出库]}:开始校验物资发放方信息, consumerPo: {}", param);
        if (param.getConsumerId() == null) {
            //现在添加物资来源
            ConsumerParam consumerParam = param.checkOutStockConsumer();
            //参数校验
            ValidationUtils.validateEntity(consumerParam);
            ConsumerPo consumer = ConsumerPo.fromConsumerParam(consumerParam);
            //添加物资去向
            Assert.DB_OPERATE.sqlSuccess(consumerDao.insert(consumer))
                    .orThrow(isAssert -> {
                        LogUtils.LOGGER.error("新增物资去向失败: {}", consumer);
                        return new ApiRuntimeException(isAssert);
                    });
            param.setConsumerId(consumer.getId());
            return;
        }
        checkConsumerIsExit(param.getConsumerId());
    }


    /**
     * 检查物资去向是否存在
     *
     * @param consumerId
     * @return
     */
    public ConsumerPo checkConsumerIsExit(Long consumerId) {
        ConsumerPo consumer = consumerDao.selectById(consumerId);

        if (consumer == null) {
            LogUtils.LOGGER.error("核对物资去向失败, 找不对应的去向, consumerId: {}", consumer);
            throw new ApiRuntimeException(Assert.PARAMETER, "物资去向不存在");
        }
        return consumer;
    }


    /**
     * 新增物资出库信息
     *
     * @param productNumber
     * @param outStockNum
     * @param productPo
     * @return
     */
    public void addOutStockInfo(int productNumber, String outStockNum, ProductPo productPo) {
        LogUtils.LOGGER.debug("{[物资出库]}:开始新增物资出库信息, productPo: {}", productPo);

        OutStockInfoPo outStockInfo = new OutStockInfoPo();
        outStockInfo.setProductNumber(productNumber);
        outStockInfo.setPNum(productPo.getPNum());
        outStockInfo.setOutNum(outStockNum);

        Assert.DB_OPERATE.sqlSuccess(outStockInfoDao.insert(outStockInfo))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资出库信息失败: {}", outStockInfo);
                    return new ApiRuntimeException(isAssert);
                });

    }

    /**
     * 新增物资发放
     *
     * @param itemNumber
     * @param outStockNum
     * @param param
     * @return
     */
    public void addOutStockProduct(int itemNumber, String outStockNum, OutStockParam param) {
        LogUtils.LOGGER.debug("{[物资出库]}:开始新增物资发放信息, outStockPo: {}", param);

        OutStockPo outStock = new OutStockPo();
        BeanUtils.copyProperties(param, outStock);
        outStock.setProductNumber(itemNumber);
        //接口测试时若未登录,会造成空指针异常
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        outStock.setOperator(activeUser.getUser().getUsername());
        //生成入库单
        outStock.setOutNum(outStockNum);
        //设置为待审核
        outStock.setStatus(2);

        Assert.DB_OPERATE.sqlSuccess(outStockDao.insert(outStock))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资发放失败: {}", outStock);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 更新物资库存信息
     *
     * @param productStock
     * @return
     */
    public void updateProductStock(ProductStockPo productStock) {

        LambdaUpdateWrapper<ProductStockPo> wrapper = new LambdaUpdateWrapper<ProductStockPo>()
                .eq(true, ProductStockPo::getId, productStock.getId());

        Assert.DB_OPERATE.sqlSuccess(productStockDao.update(productStock, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资发放信息失败, productStockId: {}, productStock: {}", productStock.getId(), productStock);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 更新物资库存
     *
     * @param outStock
     * @return
     */
    public void updateOutStock(OutStockPo outStock) {

        LambdaUpdateWrapper<OutStockPo> wrapper = new LambdaUpdateWrapper<OutStockPo>()
                .eq(true, OutStockPo::getId, outStock.getId());

        Assert.DB_OPERATE.sqlSuccess(outStockDao.update(outStock, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资分发信息失败, outStockId: {}, outStock: {}", outStock.getId(), outStock);
            return new ApiRuntimeException(iAssert);
        });
    }

}
