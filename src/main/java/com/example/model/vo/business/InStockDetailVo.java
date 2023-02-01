package com.example.model.vo.business;

import com.example.model.po.business.InStockPo;
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
public class InStockDetailVo {
    private String inNum;

    private Integer status;

    private Integer type;

    private String operator;

    private SupplierVo supplierVo;

    /** 物资总数 */
    private long total;

    private List<InStockItemVo> itemVos = new ArrayList<>();

    /**
     * 其中对itemVos进行赋值是因为避免二级list集合空引用问题
     *
     */
    public static InStockDetailVo fromInStockPo(InStockPo inStock){
        InStockDetailVo inStockDetail = InStockDetailVo.builder()
                .inNum(inStock.getInNum())
                .status(inStock.getStatus())
                .type(inStock.getType())
                .operator(inStock.getOperator())
                .itemVos(new ArrayList<InStockItemVo>())
                .build();

        return inStockDetail;
    }
}


