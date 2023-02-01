package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author 18237
 */
@Data
@TableName("biz_product_stock")
public class ProductStockPo {
    @Id
    private Long id;

    private String pNum;

    private Long stock;

}

