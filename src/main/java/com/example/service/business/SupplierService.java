package com.example.service.business;

import com.example.model.param.business.SupplierParam;
import com.example.model.po.business.SupplierPo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author 18237
 */
public interface SupplierService {

    /**
     * 查询物资供应商列表
     *
     * @param pageParam
     * @return
     */
    Page<SupplierPo> querySupplierList(SupplierParam pageParam);

    /**
     * 查询所有供应商
     *
     * @return
     */
    List<SupplierPo> findAll();

    /**
     * 添加供应商
     *
     * @param param
     * @return
     */
    Long addSupplier(SupplierParam param);

    /**
     * 编辑供应商
     *
     * @param supplierId
     * @return
     */
    SupplierPo editById(Long supplierId);

    /**
     * 更新供应商
     *
     * @param supplierId
     * @param param
     */
    void updateSupplier(Long supplierId, SupplierParam param);

    /**
     * 删除供应商
     *
     * @param supplierId
     */
    void deleteById(Long supplierId);



}
