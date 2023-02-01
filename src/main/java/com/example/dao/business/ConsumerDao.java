package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.ConsumerParam;
import com.example.model.po.business.ConsumerPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 18237
 */
@Repository
public interface ConsumerDao extends BaseMapper<ConsumerPo> {

    /**
     * 物资去向列表
     *
     * @param pageParam
     * @return
     */
    Page<ConsumerPo> queryConsumerList(ConsumerParam pageParam);

    /**
     * 查询物资去向
     *
     * @param consumerId
     * @return
     */
    @Select("select * from biz_consumer where id = #{id}")
    Optional<ConsumerPo> queryById(Long consumerId);


}
