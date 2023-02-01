package com.example.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.system.LogParam;
import com.example.model.po.system.Log;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

/**
 * @author 18237
 */
@Repository
public interface LogDao extends BaseMapper<Log> {

    /**
     * 查询登录日志列表
     *
     * @param param
     * @return
     */
    Page<Log> queryLogList(LogParam param);
}
