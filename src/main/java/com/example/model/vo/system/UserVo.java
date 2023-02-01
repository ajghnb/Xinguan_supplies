package com.example.model.vo.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo {

    private Long id;

    private Date birth;

    private Integer sex;

    private String email;

    private Boolean status;

    private String username;

    private String nickname;

    private Long departmentId;

    private String phoneNumber;

    private String departmentName;

    private Date createTime;

}
