package com.example.model.param.business;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.vo.base.PageQueryParam;
import com.example.model.vo.business.SupplierVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("SupplierParam")
public class SupplierParam extends PageQueryParam {

    private Long id;

    @Length(max = 32, groups = {Add.class, Default.class}, message = "名字最长为32个字符")
    @NotBlank(groups = {Add.class}, message = "供应商名称不能为空")
    private String name;

    @Length(max = 64, groups = {Add.class, Default.class}, message = "地址最长为32个字符")
    @NotBlank(groups = {Add.class}, message = "地址不能为空")
    private String address;

    @Email(groups = {Add.class, Default.class}, message = "正确邮件格式")
    @NotBlank(groups = {Add.class}, message = "邮箱不能为空")
    private String email;

    @Length(min = 11, max = 11, groups = {Add.class, Default.class}, message = "联系人电话只能为11位")
    @NotBlank(groups = {Add.class}, message = "联系人电话不能为空")
    private String phone;

    @NotNull(groups = {Add.class}, message = "排序号不能为空")
    private Integer sort;

    private String contact;


    public static SupplierParam fromSupplierVo(SupplierVo supplierVo) {
        SupplierParam supplierParam = SupplierParam.builder()
                .id(supplierVo.getId())
                .name(supplierVo.getName())
                .address(supplierVo.getAddress())
                .email(supplierVo.getEmail())
                .phone(supplierVo.getPhone())
                .sort(supplierVo.getSort())
                .contact(supplierVo.getContact())
                .build();
        return supplierParam;
    }


}
