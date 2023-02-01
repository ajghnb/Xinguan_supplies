package com.example.model.vo.business;

import com.example.model.po.business.ConsumerPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


/**
 * @author 18237
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerVo {


    private Long id;

    private String name;

    private String phone;

    private  Integer sort;

    private String contact;

    private String address;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    public static ConsumerVo fromConsumerPo(ConsumerPo consumer) {

        ConsumerVo consumerVo = ConsumerVo.builder()
                .id(consumer.getId())
                .name(consumer.getName())
                .address(consumer.getAddress())
                .phone(consumer.getPhone())
                .sort(consumer.getSort())
                .contact(consumer.getContact())
                .build();
        return consumerVo;
    }
}
