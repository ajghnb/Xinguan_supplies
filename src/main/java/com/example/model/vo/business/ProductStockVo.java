package com.example.model.vo.business;

import com.example.model.dto.ProductStock;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


/**
 * @author 18237
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductStockVo {

    private Long id;

    private Long stock;

    private String name;

    private String pNum;

    private String model;

    private String unit;

    private String remark;

    private String imageUrl;

    public static ProductStockVo fromProductStockVo(ProductStock productStock) {

        ProductStockVo productStockVo = ProductStockVo.builder()
                .id(productStock.getId())
                .stock(productStock.getStock())
                .name(productStock.getName())
                .pNum(productStock.getPNum())
                .model(productStock.getModel())
                .unit(productStock.getUnit())
                .remark(productStock.getRemark())
                .imageUrl(productStock.getImageUrl())
                .build();

        return productStockVo;
    }
}