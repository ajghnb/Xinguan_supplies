package com.example.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.utils.LogUtils;
import com.example.dao.business.SupplierDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.business.SupplierParam;
import com.example.model.po.business.SupplierPo;
import com.example.service.business.SupplierService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 18237
 */
@Service("supplierService")
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierDao supplierDao;


    @Override
    public Page<SupplierPo> querySupplierList(SupplierParam pageParam) {
        LogUtils.LOGGER.debug("物资供应商列表: 分页参数:{}", pageParam);

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        return supplierDao.querySupplierList(pageParam);
    }

    @Override
    public List<SupplierPo> findAll() {
        LogUtils.LOGGER.debug("查询所有物资供应商");

        return supplierDao.selectList(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long addSupplier(SupplierParam param) {
        LogUtils.LOGGER.debug("开始新增物资供应商: Supplier:{}", param);

        SupplierPo supplier = SupplierPo.fromSupplierParam(param);
        Assert.DB_OPERATE.sqlSuccess(supplierDao.insert(supplier))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资供应商失败: {}", supplier);
                    return new ApiRuntimeException(isAssert);
                });
        Long supplierId = supplier.getId();
        return supplierId;
    }

    @Override
    public SupplierPo editById(Long supplierId) {
        LogUtils.LOGGER.debug("编辑物资提供商: supplierId:{}", supplierId);

        SupplierPo supplier = supplierDao.queryById(supplierId).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询物资供应商失败, 找不到对应的供应商, supplierId: {}", supplierId);
            return new ApiRuntimeException(Assert.PARAMETER, "找不到对应的供应商");
        });
        return supplier;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSupplier(Long supplierId, SupplierParam param) {
        LogUtils.LOGGER.debug("开始更新供应商信息, supplierId: {}, supplier: {}", supplierId, param);
        //检查物资供应商
        checkSupplierIsExit(supplierId);
        SupplierPo supplier = SupplierPo.fromSupplierParam(param);
        //更新供应商
        LambdaUpdateWrapper<SupplierPo> wrapper = new LambdaUpdateWrapper<SupplierPo>()
                .eq(true, SupplierPo::getId, supplierId);
        Assert.DB_OPERATE.sqlSuccess(supplierDao.update(supplier, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资供应商失败, supplierId: {}, supplier: {}", supplierId, supplier);
            return new ApiRuntimeException(iAssert);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long supplierId) {
        LogUtils.LOGGER.debug("开始删除物资供应商, supplierId: {}", supplierId);
        //检查物资供应商
        checkSupplierIsExit(supplierId);

        Assert.DB_OPERATE.sqlSuccess(supplierDao.deleteById(supplierId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除供应商失败, supplier: {}", supplierId);
            return new ApiRuntimeException(isAssert);
        });
    }

    /**
     * 检查物资供应商是否存在
     *
     * @param supplierId
     * @return
     */
    public void checkSupplierIsExit(Long supplierId) {
        SupplierPo supplier = supplierDao.selectById(supplierId);

        if (supplier == null) {
            LogUtils.LOGGER.error("核对物资供应商失败, 找不对应的供应商, supplierId: {}", supplierId);
            throw new ApiRuntimeException(Assert.PARAMETER, "物资供应商不存在");
        }
    }
}
