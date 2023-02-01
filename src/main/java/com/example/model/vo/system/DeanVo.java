package com.example.model.vo.system;

import com.example.model.po.system.DeanPo;
import lombok.Builder;
import lombok.Data;

/**
 * @author 18237
 */
@Data
@Builder
public class DeanVo {

    private Long id;

    private String name;

    public static DeanVo fromDeanPo(DeanPo dean){

        DeanVo deanVo = DeanVo.builder()
                .id(dean.getId())
                .name(dean.getName())
                .build();
        return deanVo;
    }
}
