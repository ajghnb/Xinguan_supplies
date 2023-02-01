package com.example.service.business;

import com.example.model.param.business.InStockParam;
import com.example.model.po.business.InStockPo;
import com.github.pagehelper.Page;

/**
 * @author 18237
 */
public interface InStockService {
    /**
     * 查询入库单列表
     *
     * @param pageParam
     * @return
     */
    Page<InStockPo> queryInStockList(InStockParam pageParam);

    /**
     * 入库单
     *
     * @param inStockId
     * @return
     */
    InStockPo queryInStock(Long inStockId);

    /**
     * 物资入库
     *
     * @param param
     */
    void addInStock(InStockParam param);

    /**
     * 删除入库单
     *
     * @param inStockId
     */
    void deleteById(Long inStockId);

    /**
     * 移入回收站
     *
     * @param inStockId
     */
    void removeInStock(Long inStockId);

    /**
     * 还原从回收站中
     *
     * @param inStockId
     */
    void rollbackInStock(Long inStockId);

    /**
     * 入库审核
     *
     * @param inStockId
     */
    void checkInStock(Long inStockId);

}
