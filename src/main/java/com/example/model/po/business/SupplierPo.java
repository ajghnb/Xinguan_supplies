package com.example.model.po.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.business.SupplierParam;
import com.example.model.vo.business.SupplierVo;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@Alias("Supplier")
@TableName("biz_supplier")
public class SupplierPo {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String name;

    private String address;

    private String email;

    private String phone;

    private Date createTime;

    private Date modifiedTime;

    private Integer sort;

    private String contact;

    public static SupplierPo fromSupplierVo(SupplierVo supplierVo) {

        SupplierPo supplier = SupplierPo.builder()
                .id(supplierVo.getId())
                .name(supplierVo.getName())
                .address(supplierVo.getAddress())
                .email(supplierVo.getEmail())
                .phone(supplierVo.getPhone())
                .sort(supplierVo.getSort())
                .contact(supplierVo.getContact())
                .build();
        return supplier;
    }

    public static SupplierPo fromSupplierParam(SupplierParam param) {

        SupplierPo supplier = SupplierPo.builder()
                .id(param.getId())
                .name(param.getName())
                .address(param.getAddress())
                .email(param.getEmail())
                .phone(param.getPhone())
                .sort(param.getSort())
                .contact(param.getContact())
                .build();
        return supplier;
    }



}
