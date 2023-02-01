package com.example.model.po.business;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("Product")
@TableName("biz_product")
public class ProductPo {

    @Id
    private Long id;

    private String pNum;

    private String name;

    private String model;

    private String unit;

    private String remark;

    private Integer sort;

    private String imageUrl;

    private Integer status;

    private Date createTime;

    private Date modifiedTime;

    private Long oneCategoryId;

    private Long twoCategoryId;

    private Long threeCategoryId;

}
