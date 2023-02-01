package com.example.model.vo.business;

import com.example.model.po.business.HealthPo;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
public class HealthVo {

    private Long id;

    @NotBlank(message="地址不能为空")
    private String address;

    private Long userId;

    @NotNull(message = "当前情况不能为空")
    private Integer situation;

    @NotNull(message = "是否接触不能为空")
    private Integer touch;

    @NotNull(message = "是否路过不能为空")
    private Integer passby;

    @NotNull(message = "是否招待不能为空")
    private Integer reception;

    private Date createTime;

    public static HealthVo fromHealthPo(HealthPo health){

        HealthVo healthVo = HealthVo.builder()
                .id(health.getId())
                .userId(health.getUserId())
                .touch(health.getTouch())
                .passby(health.getPassby())
                .address(health.getAddress())
                .situation(health.getSituation())
                .reception(health.getReception())
                .build();

        return healthVo;
    }
}
