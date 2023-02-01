package com.example.model.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;


/**
 1. @author: 18237
 2. @create: 2023-01-08 15:50
 3. @Description: 用于匹配数据库库存表和物资表级联查询对应对象
 */
@Data
@Alias("ProductStock")
public class ProductStock {

    private Long id;

    private Long stock;

    private String name;

    private String pNum;

    private String model;

    private String unit;

    private String remark;

    private String imageUrl;


}