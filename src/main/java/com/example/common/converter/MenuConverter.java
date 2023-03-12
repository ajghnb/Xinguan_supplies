package com.example.common.converter;


import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.MenuPo;
import com.example.model.vo.system.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 18237
 */
public class MenuConverter {

    /**
     * 转成menuPo(只包含菜单)
     *
     * @param menu
     * @return
     */
    public static MenuNodePo converterToMenuNodePo(MenuPo menu) {
        MenuNodePo menuNode = null;
        if (menu != null) {
             menuNode = MenuNodePo.fromMenuPo(menu);
            if (menu.getType() == 0) {
                menuNode.setDisabled(menu.getAvailable() == 0);
            }
        }
        return menuNode;
    }


    /**
     * 转成menuPos(只包含菜单)List
     *
     * @param menus
     * @return
     */
    public static List<MenuNodePo> converterToMenuNodePos(List<MenuPo> menus) {
        //先过滤出用户的菜单
        List<MenuNodePo> menuNodes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(menus)){
            for (MenuPo menu : menus) {
                if(menu.getType()==0){
                    MenuNodePo menuNode = new MenuNodePo();
                    BeanUtils.copyProperties(menu, menuNode);
                    menuNode.setDisabled(menu.getAvailable()==0);
                    menuNodes.add(menuNode);
                }
            }
        }
        return menuNodes;
    }


}
