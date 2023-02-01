package com.example.service.system;

import com.example.model.param.system.RoleParam;
import com.example.model.po.system.RolePo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author 18237
 */
public interface RoleService {

    /**
     * 角色列表
     *
     * @param pageParam
     * @return
     */
    Page<RolePo> queryRoleList(RoleParam pageParam);

    /**
     * 查询所有的角色
     *
     * @return
     */
    List<RolePo> findAll();


    /**
     * 查询角色拥有的菜单权限id
     *
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(Long roleId);

    /**
     * 添加角色
     *
     * @param roleParam
     */
    void addRole(RoleParam roleParam);

    /**
     * 删除角色
     *
     * @param roleId
     */
    void deleteById(Long roleId);

    /**
     * 编辑角色
     *
     * @param roleId
     * @return
     */
    RolePo editById(Long roleId);

    /**
     * 更新角色
     *
     * @param roleId
     * @param roleParam
     * @return
     */
    void updateRole(Long roleId, RoleParam roleParam);

    /**
     * 根据角色状态
     *
     * @param roleId
     * @param status
     */
    void updateRoleStatus(Long roleId, Boolean status);


    /**
     * 角色授权
     *
     * @param roleId
     * @param menuIds
     */
    void authority(Long roleId, Long[] menuIds);


}
