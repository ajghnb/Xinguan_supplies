package com.example.common.converter;


import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.MenuPo;
import com.example.model.vo.system.MenuVo;


/**
 * @author 18237
 */
public class MenuConverter {

    /**
     * 转成menuPo(只包含菜单)List
     *
     * @param menu
     * @return
     */
    public static MenuNodePo converterToMenuNodePo(MenuPo menu) {
        if (menu != null) {
            MenuNodePo menuNode = MenuNodePo.fromMenuPo(menu);
            if (menu.getType() == 0) {
                menuNode.setDisabled(menu.getAvailable() == 0);
            }
            return menuNode;
        }
        return null;
    }
}
