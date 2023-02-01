package com.example.model.po.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 18237
 */
@Data
@Alias("UserRole")
@TableName("tb_user_role")
public class UserRolePo {

    @TableId(type = IdType.NONE)
    private Long userId;

    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
