package com.example.service.system;


import com.example.model.param.system.LogParam;
import com.example.model.po.system.Log;

import com.github.pagehelper.Page;
import org.springframework.scheduling.annotation.Async;

import java.util.List;


/**
 * @author 18237
 */
public interface LogService {


    /**
     * 登入日志列表
     *
     * @param param
     * @return
     */
    Page<Log> queryLogList(LogParam param);

    /**
     * 异步保存操作日志
     *
     * @param param
     * @return
     */
    @Async("CodeAsyncThreadPool")
    void saveLog(LogParam param);


    /**
     * 删除登入日志
     *
     * @param logId
     */
    void deleteById(Long logId);


    /**
     * 批量删除登入日志
     *
     * @param logIds
     */
    void batchDeleteByIds(List<Long> logIds);


}
