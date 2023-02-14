package com.example.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.utils.LogUtils;
import com.example.dao.business.ConsumerDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.business.ConsumerParam;
import com.example.model.po.business.ConsumerPo;
import com.example.service.business.ConsumerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 18237
 */
@Service("consumerService")
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerDao consumerDao;


    /**
     * 供应商列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<ConsumerPo> queryConsumerList(ConsumerParam param) {
        LogUtils.LOGGER.debug("物资去向列表: 分页参数:{}", param);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return consumerDao.queryConsumerList(param);
    }

    /**
     * 所有供应商
     *
     * @param
     * @return
     */
    @Override
    public List<ConsumerPo> findAll() {
        LogUtils.LOGGER.debug("查询所有物资去向");

        return consumerDao.selectList(null);
    }


    /**
     * 添加供应商
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addConsumer(ConsumerParam param) {
        LogUtils.LOGGER.debug("新增物资去向: 物资去向:{}", param);

        ConsumerPo consumer = ConsumerPo.fromConsumerParam(param);

        Assert.DB_OPERATE.sqlSuccess(consumerDao.insert(consumer))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资去向失败: {}", consumer);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 编辑供应商
     *
     * @param consumerId
     * @return
     */
    @Override
    public ConsumerPo editById(Long consumerId) {
        LogUtils.LOGGER.debug("编辑物资去向: consumerId:{}", consumerId);

        ConsumerPo consumer = consumerDao.queryById(consumerId).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询物资去向失败, 找不到对应的去向, consumerId: {}", consumerId);
            return new ApiRuntimeException(Assert.PARAMETER, "找不到对应的物资去向");
        });
        return consumer;
    }


    /**
     * 更新供应商
     *
     * @param consumerId
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConsumer(Long consumerId, ConsumerParam param) {
        LogUtils.LOGGER.debug("开始更新物资去向信息, consumerId: {}, consumer: {}", consumerId, param);
        //核对物资去向是否存在
        checkConsumerIsExit(consumerId);
        ConsumerPo consumer = ConsumerPo.fromConsumerParam(param);
        //更新物资去向
        LambdaUpdateWrapper<ConsumerPo> wrapper = new LambdaUpdateWrapper<ConsumerPo>()
                .eq(true, ConsumerPo::getId, consumerId);
        Assert.DB_OPERATE.sqlSuccess(consumerDao.update(consumer, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资去向失败, consumerId: {}, consumer: {}", consumerId, consumer);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 删除供应商
     *
     * @param consumerId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long consumerId) {

        LogUtils.LOGGER.debug("开始删除物资去向信息, consumerId: {}", consumerId);
        //检查物资去向
        checkConsumerIsExit(consumerId);

        Assert.DB_OPERATE.sqlSuccess(consumerDao.deleteById(consumerId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除供应商失败, consumer: {}", consumerId);
            return new ApiRuntimeException(isAssert);
        });

    }


    /**
     * 检查物资去向是否存在
     *
     * @param consumerId
     * @return
     */
    public ConsumerPo checkConsumerIsExit(Long consumerId) {
        ConsumerPo consumer = consumerDao.selectById(consumerId);

        if (consumer == null) {
            LogUtils.LOGGER.error("核对物资去向失败, 找不对应的去向, consumerId: {}", consumer);
            throw new ApiRuntimeException(Assert.PARAMETER, "物资去向不存在");
        }
        return consumer;
    }
}
