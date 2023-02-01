package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("InStockInfo")
@TableName("biz_in_stock_info")
public class InStockInfoPo {

    @Id
    private Long id;

    private String inNum;

    private String pNum;

    private Integer productNumber;

    private Date createTime;

    private Date modifiedTime;

}
