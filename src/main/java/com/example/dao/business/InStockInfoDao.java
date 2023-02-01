package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.po.business.InStockInfoPo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 18237
 */
@Repository
public interface InStockInfoDao extends BaseMapper<InStockInfoPo> {

    /**
     * 查询指定入库物资
     *
     * @param inNum
     * @return 物资
     */
    @Select("select * from biz_in_stock_info where in_num = #{inNum}")
    List<InStockInfoPo> queryInStockInfos(String inNum);
}
