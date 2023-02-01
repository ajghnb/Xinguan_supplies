package com.example.model.vo.system;

import com.example.model.po.system.MenuPo;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
public class MenuVo {

    private Long id;

    @NotNull(message = "父级ID必须")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    private String url;

    private String icon;

    @NotNull(message = "菜单类型不为空")
    private Integer type;

    @NotNull(message = "排序数不能为空")
    private Long orderNum;

    private Date createTime;

    private Date modifiedTime;

    @NotNull(message = "菜单状态不能为空")
    private boolean disabled;

    private Integer open;

    private String perms;

    public static MenuVo fromMenuPo(MenuPo menu){

        MenuVo menuVo = MenuVo.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .menuName(menu.getMenuName())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .type(menu.getType())
                .orderNum(menu.getOrderNum())
                .open(menu.getOpen())
                .perms(menu.getPerms())
                .build();

        return menuVo;
    }

}
