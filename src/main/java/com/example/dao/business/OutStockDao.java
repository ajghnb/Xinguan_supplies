package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.OutStockParam;
import com.example.model.po.business.OutStockPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 18237
 */
@Repository
public interface OutStockDao extends BaseMapper<OutStockPo> {

    /**
     * 查询物资出库列表
     *
     * @param pageParam
     * @return
     */
    Page<OutStockPo> queryOutStockList(OutStockParam pageParam);

    /**
     * 查询指定出库单
     *
     * @param outStockId
     * @return
     */
    @Select("select * from xinguan.biz_out_stock where id = #{id}")
    Optional<OutStockPo> queryOutStock(@Param("id") Long outStockId);
}
