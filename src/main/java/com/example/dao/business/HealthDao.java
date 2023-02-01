package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.po.business.HealthPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 18237
 */
@Repository
public interface HealthDao extends BaseMapper<HealthPo> {


    /**
     * 今日是否打卡(根据用户id查询打卡记录)
     *
     * @param userId
     * @return
     */
    @Select("select * from biz_health where create_time < (CURDATE()+1) " +
            " and create_time > CURDATE() and user_id = #{id}")
    List<HealthPo> queryById(@Param("id") Long userId);


    /**
     * 查询指定用户签到记录
     * @param userId
     * @return
     */
    @Select("select * from biz_health as bih where user_id = #{id} order by bih.create_time desc")
    Page<HealthPo> queryHealthList(@Param("id") Long userId);

}
