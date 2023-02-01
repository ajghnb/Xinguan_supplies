package com.example.common.converter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dao.business.ConsumerDao;
import com.example.dao.business.OutStockInfoDao;
import com.example.dao.business.ProductDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.po.business.*;
import com.example.model.vo.business.ConsumerVo;
import com.example.model.vo.business.OutStockDetailVo;
import com.example.model.vo.business.OutStockItemVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 18237
 */
@Component
public class OutStockDetailConverter {

    @Autowired
    ProductDao productDao;

    @Autowired
    ConsumerDao consumerDao;

    @Autowired
    OutStockInfoDao outStockInfoDao;


    /**
     * 核对出库明细
     *
     * @param outStock
     * @param pageNum
     * @param pageSize
     * @return
     */
    public OutStockDetailVo converterInStockDetail(OutStockPo outStock, int pageNum, int pageSize) {
        OutStockDetailVo outStockDetailVo = OutStockDetailVo.fromOuStockPo(outStock);

        ConsumerPo consumer = consumerDao.selectById(outStock.getConsumerId());
        if (consumer == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "物资领取方不存在,或已被删除");
        }
        ConsumerVo consumerVo = ConsumerVo.fromConsumerPo(consumer);
        outStockDetailVo.setConsumerVo(consumerVo);
        PageHelper.startPage(pageNum, pageSize);
        //发放单号
        String outStockNum = outStock.getOutNum();
        QueryWrapper<OutStockInfoPo> wrapper = new QueryWrapper<OutStockInfoPo>()
                .eq(true, "out_num", outStockNum);
        List<OutStockInfoPo> outStockInfos = outStockInfoDao.selectList(wrapper);
        //设置出库物资总数
        outStockDetailVo.setTotal(new PageInfo<>(outStockInfos).getTotal());
        if (!CollectionUtils.isEmpty(outStockInfos)) {
            for (OutStockInfoPo outStockInfo : outStockInfos) {
                String outStockPNum = outStockInfo.getPNum();
                List<ProductPo> products = productDao.queryByPNum(outStockPNum);
                if (!CollectionUtils.isEmpty(products)) {
                    ProductPo product = products.get(0);
                    OutStockItemVo outStockItemVo = OutStockItemVo.fromProductPO(product);
                    outStockItemVo.setCount(outStockInfo.getProductNumber());
                    outStockDetailVo.getItemVos().add(outStockItemVo);

                } else {
                    throw new ApiRuntimeException(Assert.PARAMETER, "编号为:[" + outStockPNum + "]的物资找不到,或已被删除");
                }
            }
        } else {
            throw new ApiRuntimeException(Assert.PARAMETER, "发放编号为:[" + outStockNum + "]的明细找不到,或已被删除");
        }
        return outStockDetailVo;
    }
}
