package com.example.service.system;

import com.example.model.param.system.LoginLogParam;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.LoginLog;
import com.github.pagehelper.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 18237
 */
public interface LoginLogService {


    /**
     * 登入日志列表
     *
     * @param param
     * @return
     */
    Page<LoginLog> queryLoginLogList(LoginLogParam param);


    /**
     * 用户登入报表
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> loginReport(UserParam param);


    /**
     * 添加登入日志
     *
     * @param request
     */
    void addLoginLog(HttpServletRequest request);


    /**
     * 删除登入日志
     *
     * @param loginLogId
     */
    void deleteById(Long loginLogId);


    /**
     * 批量删除登入日志
     *
     * @param logIds
     */
    void batchDeleteByIds(List<Long> logIds);
}
