package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.SupplierParam;
import com.example.model.po.business.SupplierPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 18237
 */
@Repository
public interface SupplierDao extends BaseMapper<SupplierPo> {

    /**
     * 查询物资供应商列表
     *
     * @param supplierParam
     * @return
     */
    Page<SupplierPo> querySupplierList(SupplierParam supplierParam);


    /**
     * 查询物资供应商
     *
     * @param supplierId
     * @return
     */
    @Select("select * from biz_supplier where id = #{id}")
    Optional<SupplierPo> queryById(@Param("id") Long supplierId);

}
