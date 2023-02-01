package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("OutStock")
@TableName("biz_out_stock")
public class OutStockPo {

    private Long id;

    private String outNum;

    private Integer type;

    private String operator;

    private Long consumerId;

    private String remark;

    private Integer status;

    private Integer priority;

    private Date createTime;

    private Integer productNumber;

}
