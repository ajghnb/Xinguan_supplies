package com.example.model.param.system;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.vo.base.PageQueryParam;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("DepartmentParam")
public class DepartmentParam extends PageQueryParam {

    private Long id;

    /**
     * 部门内人数
     **/
    private int total;

    @Length(max = 32, groups = {Add.class, Edit.class}, message = "部门名字最长为32个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "部门名字不能为空")
    private String name;

    @Length(min = 11, max = 11, groups = {Add.class, Edit.class}, message = "办公电话只能为11位")
    @NotBlank(groups = {Add.class,Edit.class}, message = "办公电话不能为空")
    private String phone;

    @Length(max = 64, groups = {Add.class, Edit.class}, message = "办公地址最长为32个字符")
    @NotBlank(groups = {Add.class,Edit.class}, message = "办公地址不能为空")
    private String address;

    private Date createTime;

    private Date modifiedTime;

}
