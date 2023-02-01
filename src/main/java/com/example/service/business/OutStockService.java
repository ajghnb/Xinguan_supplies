package com.example.service.business;

import com.example.model.param.business.OutStockParam;
import com.example.model.po.business.OutStockPo;
import com.github.pagehelper.Page;

/**
 * @author 18237
 */
public interface OutStockService {

    /**
     * 出库单列表
     *
     * @param pageParam
     * @return
     */
    Page<OutStockPo> queryOutStockList(OutStockParam pageParam);

    /**
     * 发放单详情
     *
     * @param outStockId
     * @return
     */
    OutStockPo queryOutStock(Long outStockId);


    /**
     * 提交物资发放单
     *
     * @param outStock
     * @return
     */
    void addOutStock(OutStockParam outStock);

    /**
     * 移入回收站
     *
     * @param outStockId
     * @return
     */
    void removeOutStock(Long outStockId);

    /**
     * 恢复发放单
     *
     * @param outStockId
     * @return
     */
    void rollbackOutStock(Long outStockId);

    /**
     * 删除发放单
     *
     * @param outStockId
     * @return
     */
    void deleteById(Long outStockId);

    /**
     * 发放单审核
     *
     * @param outStockId
     * @return
     */
    void checkOutStock(Long outStockId);
}
