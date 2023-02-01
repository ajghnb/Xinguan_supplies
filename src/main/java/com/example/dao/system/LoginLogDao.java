package com.example.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.system.LoginLogParam;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.LoginLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 18237
 */
@Repository
public interface LoginLogDao extends BaseMapper<LoginLog> {

    /**
     * 查询登录日志列表
     *
     * @param param
     * @return
     */
    Page<LoginLog> queryLoginLogList(LoginLogParam param);


    /**
     * 用户登入报表
     * @param param
     * @return
     */
    @MapKey("id")
    List<Map<String,Object>> userLoginReport(UserParam param);


}
