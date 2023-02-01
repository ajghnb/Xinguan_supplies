package com.example.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.system.LogParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("Log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_log")
public class Log {

    @Id
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

    public static Log fromLogParam(LogParam logParam){
        Log log = Log.builder()
                .id(logParam.getId())
                .time(logParam.getTime())
                .ip(logParam.getIp())
                .method(logParam.getMethod())
                .params(logParam.getParams())
                .username(logParam.getUsername())
                .location(logParam.getLocation())
                .operation(logParam.getOperation())
                .createTime(logParam.getCreateTime())
                .build();
        return log;
    }
}
