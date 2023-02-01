package com.example.model.vo.business;


import com.example.model.po.business.ProductPo;
import lombok.Builder;
import lombok.Data;

/**
 * @author 18237
 */
@Data
@Builder
public class OutStockItemVo {

    private Long id;

    private int count;

    private String unit;

    private String pNum;

    private String name;

    private String model;

    private String imageUrl;

    public static OutStockItemVo fromProductPO(ProductPo product){

        OutStockItemVo outStockItemVo = OutStockItemVo.builder()
                .id(product.getId())
                .pNum(product.getPNum())
                .name(product.getName())
                .unit(product.getUnit())
                .model(product.getModel())
                .imageUrl(product.getImageUrl())
                .build();
        return outStockItemVo;
    }

}
