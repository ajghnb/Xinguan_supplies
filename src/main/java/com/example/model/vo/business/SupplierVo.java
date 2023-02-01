package com.example.model.vo.business;

import com.example.model.po.business.SupplierPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierVo {

    private Long id;

    private String name;

    private String address;

    private String email;

    private String phone;

    private Integer sort;

    private String contact;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    public static SupplierVo fromSupplierPo(SupplierPo supplier) {

        SupplierVo supplierVo = SupplierVo.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .sort(supplier.getSort())
                .contact(supplier.getContact())
                .createTime(supplier.getCreateTime())
                .modifiedTime(supplier.getModifiedTime())
                .build();
        return supplierVo;
    }


}
