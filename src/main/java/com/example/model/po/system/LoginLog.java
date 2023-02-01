package com.example.model.po.system;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("LoginLog")
@TableName("tb_login_log")
public class LoginLog {

    private Long id;

    private String username;

    private Date loginTime;

    private String location;

    private String ip;

    private String userSystem;

    private String userBrowser;

}
