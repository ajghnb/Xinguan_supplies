package com.example.service.business;

import com.example.model.param.business.ConsumerParam;
import com.example.model.po.business.ConsumerPo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author 18237
 */
public interface ConsumerService {

    /**
     * 物资去向列表
     *
     * @param pageParam
     * @return
     */
    Page<ConsumerPo> queryConsumerList(ConsumerParam pageParam);


    /**
     * 查询所有物资去向
     *
     * @return
     */
    List<ConsumerPo> findAll();

    /**
     * 新增物资去向
     *
     * @param param
     * @return
     */
    void addConsumer(ConsumerParam param);

    /**
     * 更新物资去向
     *
     * @param consumerId
     * @param param
     */
    void updateConsumer(Long consumerId, ConsumerParam param);

    /**
     * 编辑物资去向
     *
     * @param consumerId
     * @return
     */
    ConsumerPo editById(Long consumerId);

    /**
     * 删除物资去向
     *
     * @param consumerId
     */
    void deleteById(Long consumerId);


}
