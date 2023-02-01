package com.example.service.business.impl;

import com.example.common.utils.LogUtils;
import com.example.dao.business.HealthDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.business.HealthParam;
import com.example.model.po.business.HealthPo;
import com.example.model.vo.base.PageQueryParam;
import com.example.service.business.HealthService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 18237
 */
@Service("healthService")
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthDao healthDao;

    /**
     * 今日是否已报备打卡
     *
     * @param userId
     * @return
     */
    @Override
    public HealthPo isReport(Long userId) {
        List<HealthPo> healths = healthDao.queryById(userId);
        if (healths.size() > 0) {
            //最新一条打卡记录
            return healths.get(0);
        }
        //该用户从未打卡
        return null;
    }

    /**
     * 健康上报
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void report(HealthParam param) {
        LogUtils.LOGGER.debug("开始健康上报: 健康申报:{}", param);

        HealthPo healthReport = isReport(param.getUserId());
        if (healthReport != null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "今日已经打卡,无需重复打卡！");
        }
        HealthPo health = HealthPo.fromHealthParam(param);
        Assert.DB_OPERATE.sqlSuccess(healthDao.insert(health))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("健康申报失败: {}", health);
                    return new ApiRuntimeException(isAssert);
                });
    }

    /**
     * 签到历史记录
     *
     * @param userId
     * @param pageParam
     * @return
     */
    @Override
    public Page<HealthPo> historyRecord(Long userId, PageQueryParam pageParam) {
        LogUtils.LOGGER.debug("签到历史记录列表: 分页参数:{}", pageParam);

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        return healthDao.queryHealthList(userId);
    }
}
