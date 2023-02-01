package com.example.service.business;

import com.example.model.param.business.HealthParam;
import com.example.model.po.business.HealthPo;
import com.example.model.vo.base.PageQueryParam;
import com.github.pagehelper.Page;

/**
 * @author 18237
 */
public interface HealthService {

    /**
     * 今日是否已经报备
     *
     * @param userId
     * @return
     */
    HealthPo isReport(Long userId);

    /**
     * 健康上报
     *
     * @param param
     * @return
     */
    void report(HealthParam param);

    /**
     * 签到记录
     *
     * @param userId
     * @param pageParam
     * @return
     */
    Page<HealthPo> historyRecord(Long userId,PageQueryParam pageParam);

}
