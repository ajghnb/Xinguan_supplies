package com.example.common.converter;

import com.example.model.po.system.MenuNodePo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 该类用于递归构建树形菜单
 *
 * @author 18237
 */
public class MenuTreeBuilder {

    /**
     * 构建多级菜单树
     *
     * @param nodes
     * @return
     */
    public static List<MenuNodePo> build(List<MenuNodePo> nodes) {
        //初始化根节点
        List<MenuNodePo> rootMenus = new ArrayList<>();
        //创建根节点
        nodes.stream().forEach((node) -> {
            if (node.getParentId() == 0) {
                rootMenus.add(node);
            }
        });
        //根据Menu类的order排序
        Collections.sort(rootMenus, MenuNodePo.order());
        //为根菜单设置子菜单,getChild是递归调用的
        rootMenus.stream().forEach((rootMenu) -> {
            //获取根节点下的所有子节点 使用getChild方法
            List<MenuNodePo> childMenuNode = getChild(rootMenu.getId(), nodes);
            //为根节点设置子节点
            rootMenu.setChildren(childMenuNode);

        });
        return rootMenus;
    }

    /**
     * 获取子菜单
     *
     * @param MenuId
     * @param nodes
     * @return
     */
    private static List<MenuNodePo> getChild(Long MenuId, List<MenuNodePo> nodes) {
        // 初始化子节点(子菜单)
        ArrayList<MenuNodePo> childMenuNodes = new ArrayList<>();
        // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较,若相等说明该节点为该根节点的子节点。
        nodes.stream().forEach((node)->{
            if(node.getParentId().equals(MenuId)){
                childMenuNodes.add(node);
            }
        });
        // 递归调用为节点设置子节点
        childMenuNodes.stream().forEach((childMenuNode)->{
            childMenuNode.setChildren(getChild(childMenuNode.getId(),nodes));
        });
        Collections.sort(childMenuNodes,MenuNodePo.order());
        // 如果节点下没有子节点,返回一个空List（递归退出）
        if(childMenuNodes==null){
            return new ArrayList<MenuNodePo>();
        }
        return childMenuNodes;
    }
}
