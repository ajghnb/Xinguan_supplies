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
@Alias("OutStockInfo")
@TableName("biz_out_stock_info")
public class OutStockInfoPo {

    @Id
    private Long id;

    private String outNum;

    private String pNum;

    private Integer productNumber;

    private Date createTime;

    private Date modifiedTime;

}
