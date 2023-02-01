package com.example.service.system;

import com.example.model.param.system.RoleParam;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.MenuPo;
import com.example.model.po.system.RolePo;
import com.example.model.po.system.UserPo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author 18237
 */
public interface UserService {

    /**
     * 用户列表
     *
     * @param param
     * @return
     */
    Page<UserPo> queryUserList(UserParam param);


    /**
     * 所有用户
     *
     * @return
     */
    List<UserPo> findAll();


    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    List<RolePo> queryRolesById(Long userId);


    /**
     * 根据用户角色查询用户的菜单
     * 菜单: menu+button
     *
     * @param params 用户的角色
     * @return
     */
    List<MenuPo> queryMenuByRoles(List<RoleParam> params);


    /**
     * 加载菜单
     *
     * @return
     */
    List<MenuNodePo> queryMenu();


    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return
     */
    UserPo queryUserByName(String name);


    /**
     * 更新状态
     *
     * @param userId
     * @param userStatus
     */
    void updateStatus(Long userId, Boolean userStatus);


    /**
     * 添加用户
     *
     * @param param
     */
    void addUser(UserParam param);


    /**
     * 删除用户
     *
     * @param userId
     */
    void deleteById(Long userId);


    /**
     * 编辑用户
     *
     * @param userId
     * @return
     */
    UserPo editById(Long userId);


    /**
     * 更新用户
     *
     * @param userId
     * @param param
     */
    void updateUser(Long userId, UserParam param);


    /**
     * 已拥有的角色ids
     *
     * @param userId
     * @return
     */
    List<Long> queryRoleIds(Long userId);


    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     */
    void assignRoles(Long userId, Long[] roleIds);


    /**
     * 用户登入
     *
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

}
