package com.example.common.converter;


import com.example.dao.business.ConsumerDao;
import com.example.model.po.business.ConsumerPo;
import com.example.model.po.business.OutStockPo;
import com.example.model.vo.business.OutStockVo;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 18237
 */
@Component
public class OutStockConverter {


    private static ConsumerDao consumerDao;

    @Autowired
    public void setConsumerDao(ConsumerDao consumerDao){
        OutStockConverter.consumerDao = consumerDao;
    }
    public static OutStockVo converterToOutStockVo(OutStockPo outStock) {

        OutStockVo outStockVo = new OutStockVo();
        BeanUtils.copyProperties(outStock, outStockVo);
        ConsumerPo consumer = consumerDao.selectById(outStock.getConsumerId());
        if (consumer != null) {
            outStockVo.setName(consumer.getName());
            outStockVo.setPhone(consumer.getPhone());
        }

        return outStockVo;
    }
}
