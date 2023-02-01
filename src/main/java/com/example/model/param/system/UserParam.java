package com.example.model.param.system;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.vo.base.PageQueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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
@Alias("UserParam")
public class UserParam extends PageQueryParam {

    private Long id;

    @NotNull(groups = {Add.class, Edit.class}, message = "性别不能为空")
    private Integer sex;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @NotNull(groups = {Add.class, Edit.class}, message = "生日不能为空")
    private Date birth;

    @Email(groups = {Add.class, Edit.class}, message = "请输入正确的邮箱格式")
    private String email;

    private Boolean status;

    @Length(min = 10, max = 10, groups = {Add.class, Edit.class}, message = "用户名只能为10个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "用户名不能为空")
    private String username;

    @Length(min = 8, groups = {Add.class, Default.class}, message = "密码最少为8个字符")
    @NotBlank(groups = {Add.class, Default.class}, message = "密码不能为空")
    private String password;

    @Length(min = 5, max = 10, groups = {Add.class, Edit.class}, message = "昵称只能为5-10个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "昵称不能为空")
    private String nickname;

    @Length(min = 11, max = 11, groups = {Add.class, Edit.class}, message = "电话号码只能为11个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "电话号码不能为空")
    private String phoneNumber;

    @NotNull(groups = {Add.class, Edit.class}, message = "部门id不能为空")
    private Long departmentId;

    private Date createTime;

    private String departmentName;
}
