package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.InStockParam;
import com.example.model.po.business.InStockPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 18237
 */
@Repository
public interface InStockDao extends BaseMapper<InStockPo> {

    /**
     * 查询入库单列表
     *
     * @param pageParam
     * @return
     */
    Page<InStockPo> queryInStockList(InStockParam pageParam);


    /**
     * 入库单明细
     *
     * @param inStockId
     * @return
     */
    @Select("select * from biz_in_stock where id = #{id}")
    Optional<InStockPo> queryInStock(@Param("id") Long inStockId);
}
