package com.example.common.converter;

import com.example.model.po.business.ProductCategoryPo;
import com.example.model.po.business.ProductCategoryTreeNodePo;
import com.github.pagehelper.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
public class ProductCategoryBuilder {


    /**
     * 构建多级
     *
     * @param productCategories
     * @return
     */
    public static List<ProductCategoryTreeNodePo> build(List<ProductCategoryPo> productCategories) {

        //通过流实现类转换
        List<ProductCategoryTreeNodePo> nodes = productCategories.stream()
                .map(ProductCategoryConverter::converterToTreeNodePo)
                .collect(Collectors.toList());

        //创建寻找根节点
        List<ProductCategoryTreeNodePo> rootMenus = new ArrayList<>();
        nodes.stream().forEach((node) -> {
            if (node.getPid() == 0) {
                node.setLev(1);
                rootMenus.add(node);
            }
        });

        // 根据Menu类的order排序
        Collections.sort(rootMenus, ProductCategoryTreeNodePo.order());

        //通过流遍历为根菜单设置子菜单,getChild是递归调用的
        rootMenus.stream().forEach((rootMenu) -> {
            List<ProductCategoryTreeNodePo> childNodes = getChild(rootMenu, nodes);
            rootMenu.setChildren(childNodes);

        });
        return rootMenus;

    }


    /**
     * 获取子菜单
     *
     * @param pNode  父节点
     * @param cNodes 孩子节点
     * @return
     */
    private static List<ProductCategoryTreeNodePo> getChild(ProductCategoryTreeNodePo pNode, List<ProductCategoryTreeNodePo> cNodes) {

        //创建子菜单
        List<ProductCategoryTreeNodePo> childNodes = new ArrayList<>();

        //通过流遍历确定根节点的子节点(将所有菜单的父id与传过来的根节点的id比较)
        cNodes.stream().forEach((cNode) -> {
            if (cNode.getPid().equals(pNode.getId())) {
                cNode.setLev(pNode.getLev() + 1);
                childNodes.add(cNode);
            }
        });

        //通过流遍历递归为每个节点寻找子节点
        childNodes.stream().forEach((childNode) -> {
            childNode.setChildren(getChild(childNode, cNodes));
        });

        //排序
        Collections.sort(childNodes, ProductCategoryTreeNodePo.order());

        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childNodes.size() == 0) {
            return null;
        }
        return childNodes;

    }


    /**
     * 获取二级父级分类
     *
     * @param nodes
     * @return
     */
    public static List<ProductCategoryTreeNodePo> buildParent(List<ProductCategoryTreeNodePo> nodes) {
        //创建根节点
        ArrayList<ProductCategoryTreeNodePo> rootMenus = new ArrayList<>();
        //遍历集合匹配父节点
        nodes.stream().forEach((node) -> {
            if (node.getPid() == 0) {
                node.setLev(1);
                rootMenus.add(node);
            }
        });
        //根据Menu类的order排序
        Collections.sort(rootMenus, ProductCategoryTreeNodePo.order());
        //为根菜单设置子菜单,getChild是递归调用的
        rootMenus.stream().forEach((rootMenu) -> {
            //获取根节点下的所有子节点 使用getChild方法
            List<ProductCategoryTreeNodePo> childNodes = getParenChild(rootMenu, nodes);
            //为根节点设置子节点
            rootMenu.setChildren(childNodes);
        });
        return rootMenus;
    }

    /**
     * 获取父级子集分类
     *
     * @param pNode
     * @param cNodes
     * @return
     */
    private static List<ProductCategoryTreeNodePo> getParenChild(ProductCategoryTreeNodePo pNode, List<ProductCategoryTreeNodePo> cNodes) {
        //创建子菜单
        ArrayList<ProductCategoryTreeNodePo> childNodes = new ArrayList<>();
        cNodes.stream().forEach((cNode) -> {
            if (cNode.getPid().equals(pNode.getId())) {
                cNode.setLev(2);
                childNodes.add(cNode);
            }
        });
        return childNodes;
    }


}
