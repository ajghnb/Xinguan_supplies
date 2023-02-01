package com.example.model.vo.system;

import com.example.model.po.system.Log;
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
public class LogVo {

    private Long id;

    private Long time;

    private String ip;

    private String method;

    private String params;

    private String username;

    private String location;

    private String operation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public static LogVo fromLog(Log log) {
        LogVo logVo = LogVo.builder()
                .id(log.getId())
                .username(log.getUsername())
                .time(log.getTime())
                .ip(log.getIp())
                .location(log.getLocation())
                .operation(log.getOperation())
                .method(log.getMethod())
                .params(log.getParams())
                .createTime(log.getCreateTime())
                .build();
        return logVo;
    }
}
