package com.example.service.system;

import com.example.model.param.system.MenuParam;
import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.MenuPo;

import java.util.List;

/**
 * @author 18237
 */
public interface MenuService {

    /**
     * 获取菜单树
     *
     * @return
     */
    List<MenuNodePo> getMenuTree();


    /**
     * 获取所有菜单
     *
     * @return
     */
    List<MenuPo> findAll();


    /**
     * 所有展开菜单的ID
     *
     * @return
     */
    List<Long> queryOpenMenuIds();


    /**
     * 添加菜单
     *
     * @param param
     * @return
     */
    MenuPo addMenu(MenuParam param);

    /**
     * 删除节点
     *
     * @param menuId
     */
    void deleteById(Long menuId);

    /**
     * 编辑节点
     *
     * @param menuId
     * @return
     */
    MenuPo editById(Long menuId);

    /**
     * 更新节点
     *
     * @param menuId
     * @param param
     */
    void updateMenu(Long menuId, MenuParam param);
}
