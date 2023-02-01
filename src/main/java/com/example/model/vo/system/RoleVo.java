package com.example.model.vo.system;

import com.example.model.po.system.RolePo;
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
public class RoleVo {

    private Long id;

    private String remark;

    private Boolean status;

    private String roleName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date modifiedTime;

    public static RoleVo fromRolePo(RolePo role) {
        RoleVo roleVo = RoleVo.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .remark(role.getRemark())
                .status(role.getStatus() == 0)
                .createTime(role.getCreateTime())
                .modifiedTime(role.getModifiedTime())
                .build();
        return roleVo;
    }
}
