package com.example.service.system.impl;

import com.example.common.utils.AddressUtil;
import com.example.common.utils.IPUtil;
import com.example.common.utils.LogUtils;
import com.example.dao.system.LoginLogDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.param.system.LoginLogParam;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.LoginLog;
import com.example.service.system.LoginLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 18237
 */
@Service("loginLogService")
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    LoginLogDao loginLogDao;


    /**
     * 登入日志列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<LoginLog> queryLoginLogList(LoginLogParam param) {
        LogUtils.LOGGER.debug("系统登录日志列表: 日志参数:{}", param);
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return loginLogDao.queryLoginLogList(param);
    }


    /**
     * 登入报表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> loginReport(UserParam param) {
        LogUtils.LOGGER.debug("系统登录日志报表: 用户报表参数:{}", param);
        return loginLogDao.userLoginReport(param);
    }


    /**
     * 插入登入日志
     *
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addLoginLog(HttpServletRequest request) {
        LogUtils.LOGGER.debug("新增系统登录日志: 用户请求参数:{}", request);
        LoginLog loginLog = createLoginLog(request);
        Assert.DB_OPERATE.sqlSuccess(loginLogDao.insert(loginLog))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增系统登录日志失败: {}", loginLog);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 删除登入日志
     *
     * @param loginLogId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long loginLogId) {
        LogUtils.LOGGER.debug("开始删除系统日志, loginLog: {}", loginLogId);
        //核查指定系统登录日志是否存在
        checkLoginLogIsExit(loginLogId);

        Assert.DB_OPERATE.sqlSuccess(loginLogDao.deleteById(loginLogId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除系统登录日志失败, log: {}", loginLogId);
            return new ApiRuntimeException(isAssert);
        });
    }


    /**
     * 批量删除日志
     *
     * @param logIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDeleteByIds(List<Long> logIds) {
        for (Long logId : logIds) {
            LoginLog loginLog = loginLogDao.selectById(logId);
            //检查指定系统登录日志是否存在
            checkLoginLogIsExit(logId);
            deleteById(logId);
        }
    }


    /**
     * 创建登入日志
     *
     * @param request
     * @return
     */
    public static LoginLog createLoginLog(HttpServletRequest request) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        //获取登录用户信息
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(activeUser.getUser().getUsername());
        loginLog.setIp(IPUtil.getIpAddr(request));
        loginLog.setLocation(AddressUtil.getCityInfo(IPUtil.getIpAddr(request)));
        // 获取客户端操作系统
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();
        loginLog.setUserSystem(os.getName());
        loginLog.setUserBrowser(browser.getName());
        loginLog.setLoginTime(new Date());
        return loginLog;
    }


    /**
     * 检查指定登入日志是否存在
     *
     * @param loginLogId
     * @return
     */
    public void checkLoginLogIsExit(Long loginLogId) {
        LoginLog loginLog = loginLogDao.selectById(loginLogId);
        if (loginLog == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该登录日志不存在");
        }
    }
}