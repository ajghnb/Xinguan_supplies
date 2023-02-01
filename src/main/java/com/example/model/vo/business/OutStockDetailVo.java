package com.example.model.vo.business;


import com.example.model.po.business.OutStockPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutStockDetailVo {

    private String outNum;

    private Integer status;

    private Integer type;

    private String operator;

    private ConsumerVo consumerVo;

    /** 总数 **/
    private long total;

    private List<OutStockItemVo> itemVos = new ArrayList<>();


    public static OutStockDetailVo fromOuStockPo(OutStockPo outStock){
        ArrayList<OutStockItemVo> itemVos = new ArrayList<>();
        OutStockDetailVo outStockDetailVo = OutStockDetailVo.builder()
                .outNum(outStock.getOutNum())
                .status(outStock.getStatus())
                .type(outStock.getType())
                .operator(outStock.getOperator())
                .itemVos(itemVos)
                .build();

        return outStockDetailVo;
    }


}
