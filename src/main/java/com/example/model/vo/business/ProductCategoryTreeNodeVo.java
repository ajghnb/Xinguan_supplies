package com.example.model.vo.business;

import com.alibaba.fastjson.JSON;
import com.example.model.po.business.ProductCategoryTreeNodePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryTreeNodeVo {

    private Long id;

    private int lev;

    private Long pid;

    private String name;

    private String remark;

    private Integer sort;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private List<ProductCategoryTreeNodeVo> children;

    /**
     * 排序,根据order排序
     */
    public static Comparator<ProductCategoryTreeNodeVo> order() {

        Comparator<ProductCategoryTreeNodeVo> comparator = (o1, o2) -> {
            if (o1.getSort().equals(o2.getSort())) {
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }

    public static ProductCategoryTreeNodeVo fromProductCategoryNodePo(ProductCategoryTreeNodePo treeNode) {

        ProductCategoryTreeNodeVo treeNodeVo = ProductCategoryTreeNodeVo.builder()
                .id(treeNode.getId())
                .lev(treeNode.getLev())
                .pid(treeNode.getPid())
                .name(treeNode.getName())
                .remark(treeNode.getRemark())
                .sort(treeNode.getSort())
                .createTime(treeNode.getCreateTime())
                .modifiedTime(treeNode.getModifiedTime())
                .build();

        List<ProductCategoryTreeNodePo> cNode = treeNode.getChildren();
        //利用json转化list集合
        if (!CollectionUtils.isEmpty(cNode)) {
            String jsonNode = JSON.toJSONString(cNode);
            List<ProductCategoryTreeNodeVo> childrenNodes = JSON.parseArray(jsonNode,
                    ProductCategoryTreeNodeVo.class);
            treeNodeVo.setChildren(childrenNodes);
        }
        return treeNodeVo;

    }
}
