package com.example.model.po.system;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.system.RoleParam;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */

@Data
@Builder
@Alias("Role")
@Excel(value = "角色表格")
@TableName("tb_role")
public class RolePo {

    @ExcelField(value = "编号", width = 50)
    private Long id;

    @ExcelField(value = "角色名称", width = 100)
    private String roleName;

    @ExcelField(value = "备注信息", width = 180)
    private String remark;

    @ExcelField(value = "创建时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date createTime;

    @ExcelField(value = "修改时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date modifiedTime;

    @ExcelField(value = "禁用状态", width = 50)
    private Integer status;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    public static RolePo fromRoleParam(RoleParam param){
        RolePo role = RolePo.builder()
                .id(param.getId())
                .roleName(param.getRoleName())
                .remark(param.getRemark())
                .build();
        return role;
    }
}
