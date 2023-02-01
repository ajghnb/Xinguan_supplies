package com.example.model.vo.system;

import com.alibaba.fastjson.JSON;
import com.example.model.po.system.MenuNodePo;
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
public class MenuNodeVo {

    private Long id;

    private String url = null;

    private String icon;

    private String perms;

    private Integer type;

    private Long orderNum;

    private Integer open;

    private Long parentId;

    private String menuName;

    private boolean disabled;


    private List<MenuNodeVo> children = new ArrayList<>();

    /**
     * 排序,根据order排序
     */
    public static Comparator<MenuNodeVo> order() {
        Comparator<MenuNodeVo> comparator = (o1, o2) -> {
            if (o1.getOrderNum().equals(o2.getOrderNum())) {
                return (int) (o1.getOrderNum() - o2.getOrderNum());
            }
            return 0;
        };
        return comparator;
    }

    public static MenuNodeVo fromMenuNodePo(MenuNodePo menuNode) {
        MenuNodeVo menuNodeVo = MenuNodeVo.builder()
                .id(menuNode.getId())
                .parentId(menuNode.getParentId())
                .menuName(menuNode.getMenuName())
                .url(menuNode.getUrl())
                .icon(menuNode.getIcon())
                .orderNum(menuNode.getOrderNum())
                .open(menuNode.getOpen())
                .disabled(menuNode.isDisabled())
                .perms(menuNode.getPerms())
                .type(menuNode.getType())
                .build();

        String jsonMenuNode = JSON.toJSONString(menuNode.getChildren());
        List<MenuNodeVo> menuNodeVos = JSON.parseArray(jsonMenuNode, MenuNodeVo.class);
        menuNodeVo.setChildren(menuNodeVos);

        return menuNodeVo;
    }

}
