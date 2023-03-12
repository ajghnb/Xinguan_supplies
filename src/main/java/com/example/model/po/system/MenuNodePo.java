package com.example.model.po.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuNodePo {

    private Long id;

    private Long parentId;

    private String menuName;

    private String url = null;

    private String icon;

    private Long orderNum;

    private Integer open;

    private boolean disabled;

    private String perms;

    private Integer type;


    private List<MenuNodePo> children = new ArrayList<>();

    /**
     * 排序,根据order排序
     */
    public static Comparator<MenuNodePo> order() {
        Comparator<MenuNodePo> comparator = (o1, o2) -> {
            if (!o1.getOrderNum().equals(o2.getOrderNum())) {
                return (int) (o1.getOrderNum() - o2.getOrderNum());
            }
            return 0;
        };
        return comparator;
    }

    public static MenuNodePo fromMenuPo(MenuPo menu) {
        MenuNodePo menuNode = MenuNodePo.builder()
                    .id(menu.getId())
                    .parentId(menu.getParentId())
                    .menuName(menu.getMenuName())
                    .url(menu.getUrl())
                    .icon(menu.getIcon())
                    .orderNum(menu.getOrderNum())
                    .open(menu.getOpen())
                    .perms(menu.getPerms())
                    .type(menu.getType())
                    .build();
        return menuNode;

    }

}
