package com.example.model.po.business;


import com.example.model.vo.business.ConsumerVo;
import com.example.model.vo.business.OutStockItemVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Data
public class OutStockDetailPo {

    private String outNum;

    private Integer status;

    private Integer type;

    private String operator;

    private ConsumerVo consumerVo;

    /** 总数**/
    private long total;

    private List<OutStockItemVo> itemVos = new ArrayList<>();


}
