package com.example.model.param.business;

import com.example.annotation.valid.Add;
import com.example.model.vo.base.PageQueryParam;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 18237
 */
@Data
@Alias("InStockParam")
public class InStockParam extends PageQueryParam {


    private Long id;

    private String inNum;

    @NotNull(groups = {Add.class, Default.class}, message = "入库单类型不能为空")
    private Integer type;

    private String operator;

    private Long supplierId;

    private String supplierName;

    private Date createTime;

    private Date modifiedTime;

    /**
     * 该入库单的总数
     **/
    private Integer productNumber;


    @NotBlank(groups = {Add.class, Default.class}, message = "入库备注不能为空")
    private String remark;

    @NotEmpty(groups = {Add.class, Default.class}, message = "入库单物资不能为空不能为空")
    private List<Object> products = new ArrayList<>();

    private Integer status;

    private Date startTime;

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
    public SupplierParam checkInStockSupplier() {

        SupplierParam supplier = SupplierParam.builder()
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
