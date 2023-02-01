package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.business.ProductCategoryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("ProductCategory")
@TableName("biz_product_category")
public class ProductCategoryPo {

    private Long id;

    private Long pid;

    private Integer sort;

    private String name;

    private String remark;

    private Date createTime;

    private Date modifiedTime;

    public static ProductCategoryPo fromProductCategoryParam(ProductCategoryParam param){
        ProductCategoryPo productCategory = ProductCategoryPo.builder()
                .id(param.getId())
                .pid(param.getPid())
                .name(param.getName())
                .remark(param.getRemark())
                .sort(param.getSort())
                .build();
        return productCategory;
    }
}
