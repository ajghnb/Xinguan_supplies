package com.example.model.param.business;


import com.example.model.vo.business.HealthVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
public class HealthParam {
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

    public static HealthParam fromHealthVo(HealthVo healthVo){
        HealthParam healthParam = HealthParam.builder()
                .id(healthVo.getId())
                .userId(healthVo.getUserId())
                .touch(healthVo.getTouch())
                .passby(healthVo.getPassby())
                .address(healthVo.getAddress())
                .situation(healthVo.getSituation())
                .reception(healthVo.getReception())
                .build();
        return healthParam;
    }

}
