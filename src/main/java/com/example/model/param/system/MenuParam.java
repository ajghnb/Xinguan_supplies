package com.example.model.param.system;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author 18237
 */
@Data
public class MenuParam {

    private Long id;

    private String url;

    private String icon;

    private Integer open;

    private String perms;

    @NotNull(groups = {Add.class, Edit.class}, message = "父级ID不能为空")
    private Long parentId;

    @Length(max = 32, groups = {Add.class, Edit.class}, message = "物资方名字最长为32个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "菜单名称不能为空")
    private String menuName;

    @NotNull(groups = {Add.class, Edit.class}, message = "菜单类型不为空")
    private Integer type;

    @NotNull(groups = {Add.class, Edit.class}, message = "排序数不能为空")
    private Long orderNum;

    @NotNull(groups = {Add.class, Edit.class}, message = "菜单状态不能为空")
    private boolean disabled;


}