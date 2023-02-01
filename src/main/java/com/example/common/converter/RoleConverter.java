package com.example.common.converter;

import com.example.model.po.system.RolePo;
import com.example.model.vo.system.RoleTransferItemVo;

/**
 * @author 18237
 */
public class RoleConverter {

    public static RoleTransferItemVo converterToRoleTransferItem(RolePo role) {
        RoleTransferItemVo roleItem = new RoleTransferItemVo();
        roleItem.setLabel(role.getRoleName());
        roleItem.setDisabled(role.getStatus() == 0);
        roleItem.setKey(role.getId());
        return roleItem;
    }
}
