package com.example.model.param.business;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.vo.base.PageQueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("ConsumerParam")
public class ConsumerParam extends PageQueryParam {

    private Long id;

    @Length(max = 32, groups = {Add.class, Edit.class}, message = "物资方名字最长为32个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "物资方名字不能为空")
    private String name;

    @Length(max = 64, groups = {Add.class, Edit.class}, message = "地址最长为32个字符")
    @NotBlank(groups = {Add.class,Edit.class}, message = "地址不能为空")
    private String address;

    @Length(min = 11, max = 11, groups = {Add.class, Edit.class}, message = "联系人电话只能为11位")
    @NotBlank(groups = {Add.class,Edit.class}, message = "联系人电话不能为空")
    private String phone;

    @NotNull(groups = {Add.class,Edit.class}, message = "排序号不能为空")
    private  Integer sort;

    @Length(max = 16, groups = {Add.class, Edit.class}, message = "联系人姓名32个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "联系人姓名不能为空")
    private String contact;

}
