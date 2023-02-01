package com.example.model.po.business;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author 18237
 * Description:该类对应数据库物资分类表(由ProductCategory----->ProductCategoryTreeNodePo)
 */
@Data
@Alias("ProductCategoryTreeNode")
public class ProductCategoryTreeNodePo {

    private Long id;

    private int lev;

    private Long pid;

    private String name;

    private String remark;

    private Integer sort;

    private List<ProductCategoryTreeNodePo> children;

    private Date createTime;

    private Date modifiedTime;

    /**
     * 排序,根据order排序
     */
    public static Comparator<ProductCategoryTreeNodePo> order() {
        Comparator<ProductCategoryTreeNodePo> comparator = (o1, o2) -> {
            if (!o1.getSort().equals(o2.getSort())) {
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }
}
