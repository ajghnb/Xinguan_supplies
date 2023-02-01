package com.example.model.param.system;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.po.system.RolePo;
import com.example.model.vo.base.PageQueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@Alias("RoleParam")
@NoArgsConstructor
@AllArgsConstructor
public class RoleParam extends PageQueryParam {

    private Long id;

    @Length(max = 16, groups = {Add.class, Default.class}, message = "角色名最长为16个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "角色名不能为空")
    private String roleName;

    @Length(max = 64, groups = {Add.class, Default.class}, message = "地址最长为64个字符")
    @NotBlank(groups = {Add.class,Edit.class}, message = "角色描述必填")
    private String remark;

    private Boolean status;

    private Date createTime;

    private Date modifiedTime;

    public static RoleParam fromRolePo(RolePo role){
        RoleParam roleParam = RoleParam.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .remark(role.getRemark())
                .status(role.getStatus() == 0)
                .build();
        return roleParam;
    }

}
