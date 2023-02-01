package com.example.service.business;

import com.example.model.param.business.ProductParam;
import com.example.model.dto.ProductStock;;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author 18237
 */
public interface ProductStockService {

    /**
     * 查询物资库存列表
     *
     * @param pageParam
     * @return 所有物资库存信息
     */
    Page<ProductStock> queryProductStockList(ProductParam pageParam);

    /**
     * 库存信息(饼图使用)
     *
     * @param productParam
     * @return 物资库存信息
     */
    List<ProductStock> findAllStocks(ProductParam productParam);

}
