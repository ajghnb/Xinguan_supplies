package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("InStock")
@TableName("biz_in_stock")
public class InStockPo {

    private Long id;

    private String inNum;

    private Integer type;

    private String operator;

    private Long supplierId;

    private Date createTime;

    private Date modifiedTime;

    private Integer productNumber;

    private String remark;

    private Integer status;


}
