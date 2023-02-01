package com.example.model.po.business;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.business.HealthParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@Alias("Health")
@TableName("biz_health")
public class HealthPo {
    @Id
    private Long id;

    private Long userId;

    private Integer touch;

    private Integer passby;

    private String address;

    private Integer situation;

    private Integer reception;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public static HealthPo fromHealthParam(HealthParam param){
        HealthPo health = HealthPo.builder()
                .id(param.getId())
                .userId(param.getUserId())
                .touch(param.getTouch())
                .passby(param.getPassby())
                .address(param.getAddress())
                .situation(param.getSituation())
                .reception(param.getReception())
                .build();
        return health;
    }



}
