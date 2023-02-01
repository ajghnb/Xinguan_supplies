package com.example.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.utils.LogUtils;
import com.example.common.utils.ValidationUtils;
import com.example.dao.business.*;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.param.business.SupplierParam;
import com.example.model.po.business.ProductPo;
import com.example.model.param.business.InStockParam;
import com.example.model.po.business.*;
import com.example.service.business.InStockService;
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
@Service("inStockService")
public class InStockServiceImpl implements InStockService {

    @Autowired
    private InStockDao inStockDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private InStockInfoDao inStockInfoDao;

    @Autowired
    private ProductStockDao productStockDao;

    @Override
    public Page<InStockPo> queryInStockList(InStockParam pageParam) {
        LogUtils.LOGGER.debug("入库列表: 分页参数:{}", pageParam);
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());

        return inStockDao.queryInStockList(pageParam);
    }


    @Override
    public InStockPo queryInStock(Long inStockId) {
        LogUtils.LOGGER.debug("查询入库单: inStockId:{}", inStockId);
        InStockPo inStock = checkInStockIsExit((inStockId));
        return inStock;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addInStock(InStockParam param) {
        //检查入库物资供应商信息是否合法
        checkInStockSupplier(param);
        //记录入库单物资数量
        int itemNumber = 0;
        //随机生成入库单号
        String inStockNum = UUID.randomUUID()
                .toString().substring(0, 32)
                .replace("-", "");

        List<Object> products = param.getProducts();
        for (Object product : products) {

            LinkedHashMap productItem = (LinkedHashMap) product;
            int productNumber = (int) productItem.get("productNumber");
            int productId = (int) productItem.get("productId");
            //核对入库物资是否存在
            ProductPo productPo = productDao.selectById(productId);
            if (productPo == null) {
                throw new ApiRuntimeException(Assert.IS_EXIST, "入库物资不存在");
            }
            //核对入库物资
            checkInStockProduct(productPo, productNumber);
            //计算入库物资数量
            itemNumber += productNumber;
            //添加入库单明细
            addInStockInfo(productPo, productNumber, inStockNum);
        }
        //添加入库单
        addProductInStock(param, inStockNum, itemNumber);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long inStockId) {
        InStockPo inStock = checkInStockIsExit(inStockId);

        if (inStock.getStatus() != 1 && inStock.getStatus() != 2) {
            throw new ApiRuntimeException(Assert.INSTOCK_STATUS, "入库单状态错误,无法删除");
        }

        //可能存在多条入库物资信息,一起删除
        QueryWrapper<InStockInfoPo> wrapper = new QueryWrapper<InStockInfoPo>()
                .eq(true, "in_num", inStock.getInNum());
        inStockInfoDao.delete(wrapper);

        Assert.DB_OPERATE.sqlSuccess(inStockDao.deleteById(inStockId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除入库单失败, inStockId: {}", inStock, inStockId);
            return new ApiRuntimeException(isAssert);
        });
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeInStock(Long inStockId) {
        InStockPo inStock = checkInStockIsExit(inStockId);
        if (inStock.getStatus() != 0) {
            throw new ApiRuntimeException(Assert.INSTOCK_STATUS, "入库单状态错误");
        }

        inStock.setStatus(1);
        LambdaUpdateWrapper<InStockPo> wrapper = new LambdaUpdateWrapper<InStockPo>()
                .eq(true, InStockPo::getId, inStockId);
        Assert.DB_OPERATE.sqlSuccess(inStockDao.update(inStock, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("删除入库单失败, inStockId: {}, inStock: {}", inStockId, inStock);
            return new ApiRuntimeException(iAssert);
        });

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rollbackInStock(Long inStockId) {
        InStockPo inStock = checkInStockIsExit(inStockId);
        if (inStock.getStatus() != 1) {
            throw new ApiRuntimeException(Assert.INSTOCK_STATUS, "入库单状态错误");
        }
        //变更入库单状态
        inStock.setStatus(0);
        LambdaUpdateWrapper<InStockPo> wrapper = new LambdaUpdateWrapper<InStockPo>()
                .eq(true, InStockPo::getId, inStockId);
        Assert.DB_OPERATE.sqlSuccess(inStockDao.update(inStock, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("恢复入库单失败, inStockId: {}, inStock: {}", inStockId, inStock);
            return new ApiRuntimeException(iAssert);
        });

    }


    @Override
    public void checkInStock(Long inStockId) {
        InStockPo inStock = inStockDao.selectById(inStockId);
        if (inStock == null) {
            throw new ApiRuntimeException(Assert.IS_EXIST, "入库单不存在");
        }
        if (inStock.getStatus() != 2) {
            throw new ApiRuntimeException(Assert.INSTOCK_STATUS, "入库单状态错误");
        }

        SupplierPo supplier = supplierDao.selectById(inStock.getSupplierId());
        if (supplier == null) {
            throw new ApiRuntimeException(Assert.IS_EXIST, "物资信息来源错误,物资提供方不存在");
        }

        List<InStockInfoPo> inStockInfos = inStockInfoDao.queryInStockInfos(inStock.getInNum());
        if (!CollectionUtils.isEmpty(inStockInfos)) {
            for (InStockInfoPo inStockInfo : inStockInfos) {
                //入库物资
                Integer productNumber = inStockInfo.getProductNumber();
                List<ProductPo> products = productDao.queryByPNum(inStockInfo.getPNum());
                if (products.size() > 0) {
                    ProductPo product = products.get(0);
                    List<ProductStockPo> productStocks = productStockDao.queryListByPNum(product.getPNum());
                    if (!CollectionUtils.isEmpty(productStocks)) {
                        //更新物资库存数量
                        ProductStockPo productStock = productStocks.get(0);
                        updateProductStock(productStock, productNumber);
                    } else {
                        //插入
                        addProductStock(product, productNumber);
                    }
                    updateInStock(inStock);

                } else {
                    throw new ApiRuntimeException(Assert.PARAMETER, "物资编号为:[" + inStockInfo.getPNum() + "]的物资不存在");
                }

            }
        } else {
            throw new ApiRuntimeException(Assert.PARAMETER, "入库的明细不能为空");
        }
    }


    /**
     * 核对入库单是否存在
     *
     * @param inStockId
     * @return 存在则返回对应入库单, 否则抛出异常
     */
    public InStockPo checkInStockIsExit(Long inStockId) {

        InStockPo inStock = inStockDao.queryInStock(inStockId).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询入库单失败, 找不到对应的入库单, inStockId: {}", inStockId);
            return new ApiRuntimeException(Assert.IS_EXIST, "找不到对应的入库单");
        });
        return inStock;
    }


    /**
     * 核对入库物资信息是否和法
     *
     * @param productPo
     * @return
     */
    public void checkInStockProduct(ProductPo productPo, int productNumber) {
        //核对入库物资状态
        if (productPo.getStatus() == 1) {
            throw new ApiRuntimeException(Assert.PRODUCT_REMOVE, productPo.getName() + "物资状态错误,物资已被回收,无法入库");
        }
        if (productPo.getStatus() == 2) {
            throw new ApiRuntimeException(Assert.PRODUCT_WAIT, productPo.getName() + "物资待审核,无法入库");
        }
        //核对入库数量
        if (productNumber <= 0) {
            throw new ApiRuntimeException(Assert.PRODUCT_INSTOCK_NUMBER, productPo.getName() + "物资入库数量不合法,无法入库");
        }
    }


    /**
     * 添加入库单明细
     *
     * @return
     */
    public void addInStockInfo(ProductPo productPo, int productNumber, String inStockNum) {

        InStockInfoPo inStockInfo = new InStockInfoPo();
        inStockInfo.setProductNumber(productNumber);
        inStockInfo.setInNum(inStockNum);
        inStockInfo.setPNum(productPo.getPNum());

        Assert.DB_OPERATE.sqlSuccess(inStockInfoDao.insert(inStockInfo))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增入库明细失败: {}", inStockInfo);
                    return new ApiRuntimeException(isAssert);
                });

    }


    /**
     * 添加入库单
     *
     * @return
     */
    public void addProductInStock(InStockParam param, String inStockNum, int itemNumber) {
        InStockPo inStock = new InStockPo();

        BeanUtils.copyProperties(param, inStock);
        //设置入库单物资数量
        inStock.setProductNumber(itemNumber);
        //设置操作人
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        inStock.setOperator(activeUser.getUser().getUsername());
        //设置入库单编号
        inStock.setInNum(inStockNum);
        //设置为待审核
        inStock.setStatus(2);

        Assert.DB_OPERATE.sqlSuccess(inStockDao.insert(inStock))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增入库单: {}", inStock);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 更新物资库存数量
     *
     * @return
     */
    public void updateProductStock(ProductStockPo productStock, Integer productNumber) {
        productStock.setStock(productStock.getStock() + productNumber);

        LambdaUpdateWrapper<ProductStockPo> wrapper = new LambdaUpdateWrapper<ProductStockPo>()
                .eq(true, ProductStockPo::getId, productStock.getId());
        Assert.DB_OPERATE.sqlSuccess(productStockDao.update(productStock, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资库存信息失败, productStockId: {}, productStock: {}", productStock.getId(), productStock);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 添加物资库存
     *
     * @return
     */
    public void addProductStock(ProductPo product, Integer productNumber) {

        ProductStockPo productStock = new ProductStockPo();
        productStock.setPNum(product.getPNum());
        productStock.setStock((long) productNumber);

        Assert.DB_OPERATE.sqlSuccess(productStockDao.insert(productStock))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资库存失败: {}", productStock);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 更新入库单状态
     * @param inStock
     * @return
     */
    public void updateInStock(InStockPo inStock) {
        inStock.setStatus(0);

        LambdaUpdateWrapper<InStockPo> wrapper = new LambdaUpdateWrapper<InStockPo>()
                .eq(true, InStockPo::getId, inStock.getId());
        Assert.DB_OPERATE.sqlSuccess(inStockDao.update(inStock, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资库存信息失败, inStockId: {}, inStock: {}", inStock.getId(), inStock);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 检查入库物资供应商
     * @param param
     * @return
     */
    public void checkInStockSupplier(InStockParam param){
        if (param.getSupplierId() == null) {
            SupplierParam supplierParam = param.checkInStockSupplier();
            //参数校验
            ValidationUtils.validateEntity(supplierParam);
            SupplierPo supplier = SupplierPo.fromSupplierParam(supplierParam);
            //新增物资对应供应商
            Assert.DB_OPERATE.sqlSuccess(supplierDao.insert(supplier))
                    .orThrow(isAssert -> {
                        LogUtils.LOGGER.error("新增物资供应商失败: {}", supplier);
                        return new ApiRuntimeException(isAssert);
                    });
            param.setSupplierId(supplier.getId());
        }
    }

}
