package com.example.model.dto;

import com.example.annotation.valid.Login;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

/**
 * @author 18237
 */
@Data
@ApiModel(value = "用户登入表单")
public class LoginInfo {

    @ApiModelProperty(value = "用户名")
    @Length(min = 3, max = 10, groups = {Login.class}, message = "用户名只能为10个字符")
    @NotBlank(groups = {Login.class, Default.class}, message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @Length(min = 6, groups = {Login.class}, message = "密码最少8个字符")
    @NotBlank(groups = {Login.class, Default.class}, message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码")
    @Length(min = 4, max = 4, groups = {Login.class}, message = "验证码只能为4个字符")
    @NotBlank(groups = {Login.class, Default.class}, message = "验证码不能为空")
    private String captcha;
}
