package com.example.model.param.system;

import com.example.model.vo.base.PageQueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @author 18237
 */
@Data
/*@NoArgsConstructor
@AllArgsConstructor*/
@Alias("LoginLogParam")
public class LoginLogParam extends PageQueryParam {

    private Long id;

    private String ip;

    private String username;

    private String location;

    private String userSystem;

    private String userBrowser;

    private Date loginTime;
}
