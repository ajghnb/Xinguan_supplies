package com.example.service.system.impl;

import com.example.common.utils.LogUtils;
import com.example.dao.system.LogDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.system.LogParam;
import com.example.model.po.system.Log;
import com.example.service.system.LogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 18237
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    LogDao logDao;


    /**
     * 日志信息列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<Log> queryLogList(LogParam param) {
        LogUtils.LOGGER.debug("系统日志列表: 日志参数:{}", param);
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return logDao.queryLogList(param);
    }


    /**
     * 保存登入日志
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLog(LogParam param) {
        LogUtils.LOGGER.debug("保存系统日志: Log:{}", param);
        Log log = Log.fromLogParam(param);

        Assert.DB_OPERATE.sqlSuccess(logDao.insert(log))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增系统日志失败: {}", log);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 删除操作日志
     *
     * @param logId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long logId) {
        LogUtils.LOGGER.debug("开始删除系统日志, log: {}", logId);
        //核查指定系统日志是否存在
        checkLogIsExit(logId);
        Assert.DB_OPERATE.sqlSuccess(logDao.deleteById(logId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除系统日志失败, log: {}", logId);
            return new ApiRuntimeException(isAssert);
        });
    }


    /**
     * 批量删除
     *
     * @param logIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDeleteByIds(List<Long> logIds) {
        logIds.stream().forEach((logId) -> {
            //核查指定id的系统日志是否存在
            checkLogIsExit(logId);
            //执行删除操作
            deleteById(logId);
        });
    }


    /**
     * 检查指导日志记录是否存在
     *
     * @param logId
     * @return
     */
    public void checkLogIsExit(Long logId) {
        Log log = logDao.selectById(logId);
        if (log == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "指定日志不存在");
        }
    }
}
