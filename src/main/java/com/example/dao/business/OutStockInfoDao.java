package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.po.business.OutStockInfoPo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 18237
 */
@Repository
public interface OutStockInfoDao extends BaseMapper<OutStockInfoPo> {


    /**
     * 查询物资发放信息
     *
     * @param outStockNum
     * @return
     */
    @Select("select * from xinguan.biz_out_stock_info where out_num = #{outNum}")
    List<OutStockInfoPo> queryOutStockInfoList(@Param("outNum") String outStockNum);




}
