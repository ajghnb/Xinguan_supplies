package com.example.model.vo.system;

import com.example.model.po.system.LoginLog;
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
public class LoginLogVo {

    private Long id;

    private String ip;

    private String username;

    private String location;

    private String userSystem;

    private String userBrowser;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public static LoginLogVo fromLoginLog(LoginLog loginLog) {
        LoginLogVo loginlogVo = LoginLogVo.builder()
                .id(loginLog.getId())
                .username(loginLog.getUsername())
                .location(loginLog.getLocation())
                .ip(loginLog.getIp())
                .userSystem(loginLog.getUserSystem())
                .userBrowser(loginLog.getUserBrowser())
                .loginTime(loginLog.getLoginTime())
                .build();
        return loginlogVo;
    }
}
