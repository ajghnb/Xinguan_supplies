package com.example.model.po.system;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;

/**
 * @author 18237
 */
@Data
@Alias("RoleMenu")
@TableName("tb_role_menu")
public class RoleMenuPo {

    @TableId(type = IdType.NONE)
    private Long roleId;

    private Long menuId;
}
