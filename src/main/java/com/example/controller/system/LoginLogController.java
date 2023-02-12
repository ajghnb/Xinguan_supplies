package com.example.controller.system;

import com.example.annotation.ControllerEndpoint;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.system.LoginLogParam;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.LoginLog;
import com.example.model.vo.system.LoginLogVo;
import com.example.service.system.LoginLogService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 18237
 */
@Api(tags = "系统模块-登入日志相关接口")
@RestController
@RequestMapping("/system/loginLog")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 登录日志列表
     *
     * @param loginLog
     * @return
     */
    @ApiOperation(value = "日志列表", notes = "登入日志列表，模糊查询")
    @GetMapping("/page")
    public R<PageData<LoginLogVo>> loginLogPage(LoginLogParam loginLog) {
        Page<LoginLog> loginLogs = loginLogService.queryLoginLogList(loginLog);
        return R.ofSuccess(new PageData<>(loginLogs)
                .convert(LoginLogVo::fromLoginLog));
    }

    /**
     * 删除日志
     *
     * @param loginLogId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除登入日志失败", operation = "删除登入日志")
    @ApiOperation(value = "删除日志")
    @RequiresPermissions({"loginLog:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteLoginLog(@PathVariable("id") Long loginLogId) {
        loginLogService.deleteById(loginLogId);
        return R.ofSuccess();
    }

    /**
     * 批量删除
     *
     * @param loginLogIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除登入日志失败", operation = "批量删除登入日志")
    @ApiOperation(value = "批量删除")
    @RequiresPermissions({"loginLog:batchDelete"})
    @DeleteMapping("/batch/delete/{ids}")
    public R<Void> batchDeleteLoginLog(@PathVariable("ids") String loginLogIds) {
        String[] ids = loginLogIds.split(",");
        List<Long> logIdList = new ArrayList<>();
        if (ids.length > 0) {
            for (String id : ids) {
                logIdList.add(Long.parseLong(id));
            }
        }
        loginLogService.batchDeleteByIds(logIdList);
        return R.ofSuccess();
    }

    /**
     * 登入报表
     *
     * @param user
     * @return
     */
    @PostMapping("/loginReport")
    @ApiOperation(value = "登入报表", notes = "用户登入报表")
    public R<Map<String, Object>> loginReport(@RequestBody UserParam user) {
        List<Map<String, Object>> loginMapList = loginLogService.loginReport(user);
        Map<String, Object> map = new HashMap<>();
        user.setUsername(null);
        List<Map<String, Object>> meLoginList = loginLogService.loginReport(user);
        map.put("me", loginMapList);
        map.put("all", meLoginList);
        return R.ofSuccess(map);
    }

}