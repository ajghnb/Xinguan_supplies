package com.example.model.param.system;

import com.example.model.po.system.Log;
import com.example.model.vo.base.PageQueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@Alias("LogParam")
@NoArgsConstructor
@AllArgsConstructor
public class LogParam extends PageQueryParam {

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


    public static LogParam fromLog(Log log){
        LogParam logParam = LogParam.builder()
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
        return logParam;

    }
}
