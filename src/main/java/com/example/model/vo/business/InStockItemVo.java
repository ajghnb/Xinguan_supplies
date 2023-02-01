package com.example.model.vo.business;

import com.example.model.po.business.ProductPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InStockItemVo {

    private Long id;

    private int count;

    private String unit;

    private String pNum;

    private String name;

    private String model;

    private String imageUrl;

    public static InStockItemVo fromProductPo(ProductPo product){

        InStockItemVo inStockItem = InStockItemVo.builder()
                .id(product.getId())
                .pNum(product.getPNum())
                .name(product.getName())
                .model(product.getModel())
                .unit(product.getUnit())
                .imageUrl(product.getImageUrl())
                .build();

        return inStockItem;
    }
}
