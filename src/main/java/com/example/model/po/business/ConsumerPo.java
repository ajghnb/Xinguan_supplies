package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.business.ConsumerParam;
import com.example.model.vo.business.ConsumerVo;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@Alias("Consumer")
@TableName("biz_consumer")
public class ConsumerPo {

    private Long id;

    private String name;

    private String address;

    private Date createTime;

    private Date modifiedTime;

    private String phone;

    private  Integer sort;

    private String contact;

    public static ConsumerPo fromConsumerParam(ConsumerParam param){
        ConsumerPo consumer = ConsumerPo.builder()
                .id(param.getId())
                .name(param.getName())
                .address(param.getAddress())
                .phone(param.getPhone())
                .sort(param.getSort())
                .contact(param.getContact())
                .build();
        return consumer;
    }

}
