package com.example.controller.system;

import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.system.LogParam;
import com.example.model.po.system.Log;
import com.example.model.vo.system.LogVo;
import com.example.service.system.LogService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Api(tags = "系统模块-操作日志相关接口")
@RestController
@RequestMapping("/system/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 日志列表
     *
     * @param log
     * @return
     */
    @ApiOperation(value = "日志列表", notes = "系统日志列表，模糊查询")
    @GetMapping("/page")
    public R<PageData<LogVo>> logPage(LogParam log) {
        Page<Log> logs = logService.queryLogList(log);
        return R.ofSuccess(new PageData<>(logs)
                .convert(LogVo::fromLog));
    }

    /**
     * 删除日志
     *
     * @param logId
     * @return
     */
    @ApiOperation(value = "删除日志")
    @RequiresPermissions({"log:delete"})
    @DeleteMapping("/delete/{id}")
    public R<String> deleteLog(@PathVariable("id") Long logId) {
        logService.deleteById(logId);
        return R.ofSuccess("删除系统日志成功");
    }

    /**
     * 批量删除
     *
     * @param logIds
     * @return
     */
    @ApiOperation(value = "批量删除")
    @RequiresPermissions({"log:batchDelete"})
    @DeleteMapping("/batch/delete/{ids}")
    public R<String> batchDeleteLogs(@PathVariable("ids") String logIds)  {
        String[] ids = logIds.split(",");
        List<Long> logIdList = new ArrayList<>();
        if(ids.length>0){
            for (String id : ids) {
                logIdList.add(Long.parseLong(id));
            }
        }
        logService.batchDeleteByIds(logIdList);
        return R.ofSuccess("批量删除日志成功");
    }

}