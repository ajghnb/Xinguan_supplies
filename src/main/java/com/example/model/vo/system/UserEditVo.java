package com.example.model.vo.system;

import com.example.model.po.system.UserPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
public class UserEditVo {

    private Long id;

    private Long departmentId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日")
    private Date birth;

    private Integer sex;

    private String email;

    private String username;

    private String nickname;

    private String phoneNumber;

    public static UserEditVo fromUserPo(UserPo user) {
        UserEditVo userEditVo = UserEditVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .sex(user.getSex())
                .birth(user.getBirth())
                .departmentId(user.getDepartmentId())
                .build();
        return userEditVo;
    }

}
