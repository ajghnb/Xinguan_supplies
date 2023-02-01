package com.example.model.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;


/**
 * @author 18237
 */
@Data
@ApiModel(value = "用户登入信息")
public class UserInfoVo {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "菜单")
    private Set<String> url;

    @ApiModelProperty(value = "权限")
    private Set<String> perms;

    @ApiModelProperty(value = "角色集合")
    private List<String> roles;

    @ApiModelProperty(value = "所在部门")
    private String department;

    @ApiModelProperty(value = "是否是超管")
    private Boolean isAdmin=false;



}
