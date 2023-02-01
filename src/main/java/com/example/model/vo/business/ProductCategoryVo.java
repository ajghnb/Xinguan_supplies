package com.example.model.vo.business;

import com.example.model.po.business.ProductCategoryPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCategoryVo {

    private Long id;

    private String name;

    private String remark;

    private Integer sort;

    private Date createTime;

    private Date modifiedTime;

    /** 父级分类id*/
    @NotNull(message = "父级菜单不能为空")
    private Long pid;

    public static ProductCategoryVo fromProductCategoryPo(ProductCategoryPo productCategory){
        ProductCategoryVo productCategoryVo = ProductCategoryVo.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .remark(productCategory.getRemark())
                .sort(productCategory.getSort())
                .pid(productCategory.getPid())
                .build();
        return productCategoryVo;
    }
}
