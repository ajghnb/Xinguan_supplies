package com.example.model.vo.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 18237
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InStockVo {

    private Long id;

    private String inNum;

    private Integer type;

    private String operator;

    private Long supplierId;

    private String supplierName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 该入库单的总数
     **/
    private Integer productNumber;

    private String remark;

    private List<Object> products = new ArrayList<>();

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


    /**
     * 如果supplierId不存在需要添加供应商信息
     **/

    private String name;

    private String address;

    private String email;

    private String phone;

    private Integer sort;

    private String contact;


    /**
     * 核对入库物资供应商
     *
     * @return
     */
    public SupplierVo checkInStockSupplier() {

        SupplierVo supplier = SupplierVo.builder()
                .name(this.name)
                .address(this.address)
                .email(this.email)
                .phone(this.phone)
                .sort(this.sort)
                .contact(this.contact)
                .build();
        return supplier;
    }


}
