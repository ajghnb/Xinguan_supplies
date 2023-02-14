package com.example.service.business.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.converter.ProductConverter;
import com.example.dao.business.ProductDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.business.ProductParam;
import com.example.model.po.business.ProductPo;
import com.example.service.business.ProductService;
import com.example.common.utils.LogUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author 18237
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;


    /**
     * 物资列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<ProductPo> queryProductList(ProductParam param) {
        LogUtils.LOGGER.debug("物资列表: 分页参数:{}", param);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return productDao.queryProductList(param);
    }


    /**
     * 添加物资
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProduct(ProductParam param) {
        LogUtils.LOGGER.debug("开始新增物资, product: {}", param);
        //再次判断参数合法性
        if (param.getCategoryKeys().length != 3) {
            LogUtils.LOGGER.error("新增物资失败: {}", param);
            throw new ApiRuntimeException(Assert.ERROR, "新增物资失败,物资需要三级分类");
        }

        ProductPo product = ProductConverter.converterToProductPo(param);

        //物资状态：未审核
        product.setStatus(2);
        product.setPNum(UUID.randomUUID().toString().substring(0, 32));
        Assert.DB_OPERATE.sqlSuccess(productDao.insert(product))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资失败: {}", product);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 编辑物资信息
     *
     * @param productId
     * @return
     */
    @Override
    public ProductPo editById(Long productId) {
        LogUtils.LOGGER.debug("物资详情: productId:{}", productId);

        ProductPo product = productDao.editById(productId).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询物资失败, 找不到对应的物资, productId: {}", productId);
            return new ApiRuntimeException(Assert.IS_EXIST, "找不到对应的物资");
        });
        return product;
    }


    /**
     * 删除物资信息
     *
     * @param productId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long productId) {
        LogUtils.LOGGER.debug("开始删除物资, productId: {}", productId);

        ProductPo product = checkProductIsExit(productId);

        //只有物资处于回收站,或者待审核的情况下可删除
        if (product.getStatus() != 1 && product.getStatus() != 2) {
            throw new ApiRuntimeException(Assert.PRODUCT_STATUS, "删除物资失败, 该物资状态错误");
        }

        Assert.DB_OPERATE.sqlSuccess(productDao.deleteById(productId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除物资失败, productId: {}", product, productId);
            return new ApiRuntimeException(isAssert);
        });
    }


    /**
     * 更新商品
     *
     * @param productId
     * @param param
     * @reuturn
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProduct(Long productId, ProductParam param) {
        LogUtils.LOGGER.debug("开始更新物资信息, productId: {}, product: {}", productId, param);

        //再次判断参数合法性
        if (param.getCategoryKeys().length != 3) {
            throw new ApiRuntimeException(Assert.PARAMETER, "更新物资失败,物资需要三级分类");
        }

        checkProductIsExit(productId);

        ProductPo product = ProductConverter.converterToProductPo(param);

        LambdaUpdateWrapper<ProductPo> wrapper = new LambdaUpdateWrapper<ProductPo>()
                .eq(true, ProductPo::getId, productId);
        Assert.DB_OPERATE.sqlSuccess(productDao.update(product, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资信息失败, productId: {}, productPo: {}", productId, product);
            return new ApiRuntimeException(iAssert);
        });

    }


    /**
     * 移入回收站
     *
     * @param productId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeProduct(Long productId) {
        LogUtils.LOGGER.debug("开始将物资移入回收站, productId: {}", productId);

        ProductPo product = checkProductIsExit(productId);
        if (product.getStatus() != 0) {
            throw new ApiRuntimeException(Assert.PRODUCT_STATUS, "变更物资失败, 该物资状态错误");
        }

        product.setStatus(1);
        LambdaUpdateWrapper<ProductPo> wrapper = new LambdaUpdateWrapper<ProductPo>()
                .eq(true, ProductPo::getId, productId);
        Assert.DB_OPERATE.sqlSuccess(productDao.update(product, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("移动物资失败, productId: {}, productPo: {}", productId, product);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 从回收站恢复数据
     *
     * @param productId
     * @reuturn
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rollbackProduct(Long productId) {
        LogUtils.LOGGER.debug("开始将物资数据恢复, productId: {}", productId);

        ProductPo product = checkProductIsExit(productId);
        if (product.getStatus() != 1) {
            throw new ApiRuntimeException(Assert.PRODUCT_STATUS, "恢复物资数据失败, 该物资状态错误");
        }

        product.setStatus(0);
        LambdaUpdateWrapper<ProductPo> wrapper = new LambdaUpdateWrapper<ProductPo>()
                .eq(true, ProductPo::getId, productId);
        Assert.DB_OPERATE.sqlSuccess(productDao.update(product, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("恢复物资数据失败, productId: {}, productPo: {}", productId, product);
            return new ApiRuntimeException(iAssert);
        });

    }


    /**
     * 物资审核
     *
     * @param productId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkProduct(Long productId) {
        LogUtils.LOGGER.debug("开始审核物资, productId: {}", productId);

        ProductPo product = checkProductIsExit(productId);
        if (product.getStatus() != 2) {
            throw new ApiRuntimeException(Assert.PRODUCT_STATUS, "审核物资失败, 该物资状态错误");
        }

        product.setStatus(0);
        LambdaUpdateWrapper<ProductPo> wrapper = new LambdaUpdateWrapper<ProductPo>()
                .eq(true, ProductPo::getId, productId);
        Assert.DB_OPERATE.sqlSuccess(productDao.update(product, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("审核物资失败, productId: {}, productPo: {}", productId, product);
            return new ApiRuntimeException(iAssert);
        });

    }


    /**
     * 核对物资是否存在
     *
     * @param productId
     * @return
     */
    public ProductPo checkProductIsExit(Long productId) {
        ProductPo product = productDao.selectById(productId);
        if (product == null) {
            throw new ApiRuntimeException(Assert.IS_EXIST, "审核物资失败, 该物资不存在");
        }

        return product;
    }
}
