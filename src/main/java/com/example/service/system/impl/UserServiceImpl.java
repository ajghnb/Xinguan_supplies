package com.example.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.converter.MenuConverter;
import com.example.common.converter.MenuTreeBuilder;
import com.example.common.utils.JwtUtils;
import com.example.common.utils.LogUtils;
import com.example.common.utils.MD5Utils;
import com.example.common.utils.RandomUtils;
import com.example.config.JwtInfo;
import com.example.config.shiro.Token;
import com.example.dao.system.*;
import com.example.enums.UserStatusEnum;
import com.example.enums.UserTypeEnum;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.param.system.RoleParam;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.*;
import com.example.service.system.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 18237
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private JwtInfo jwtInfo;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private DepartmentDao departmentDao;


    /**
     * 用户列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<UserPo> queryUserList(UserParam param) {
        LogUtils.LOGGER.debug("查询系统用户列表: 查询参数:{}", param);
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return userDao.queryUserList(param);
    }


    /**
     * 查询所有用户
     *
     * @param
     * @return
     */
    @Override
    public List<UserPo> findAll() {
        LogUtils.LOGGER.debug("开始查询所有系统用户");
        return userDao.selectList(null);
    }


    /**
     * 查询用户角色
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<RolePo> queryRolesById(Long userId) {
        LogUtils.LOGGER.debug("查询用户角色信息: userId:{}", userId);
        UserPo user = checkUserIsExit(userId);
        List<UserRolePo> userRoles = userRoleDao.queryRoleById(user.getId());
        ArrayList<RolePo> roles = new ArrayList<>();
        ArrayList<Long> roleIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            for (UserRolePo userRole : userRoles) {
                roleIds.add(userRole.getRoleId());
            }
            if (!CollectionUtils.isEmpty(roleIds)) {
                for (Long roleId : roleIds) {
                    RolePo role = roleDao.selectById(roleId);
                    if (role != null) {
                        roles.add(role);
                    }
                }
            }
        }
        return roles;
    }


    /**
     * 查询权限
     *
     * @param roleParams 用户的角色
     * @return
     */
    @Override
    public List<MenuPo> queryMenuByRoles(List<RoleParam> roleParams) {
        LogUtils.LOGGER.debug("查询角色菜单权限: 角色信息:{}", roleParams);
        List<MenuPo> menus = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleParams)) {
            //存放用户的菜单id
            Set<Long> menuIds = new HashSet<>();
            List<RoleMenuPo> roleMenus;
            for (RoleParam roleParam : roleParams) {
                //根据角色ID查询权限ID
                roleMenus = roleMenuDao.queryRoleMenusById(roleParam.getId());
                if (!CollectionUtils.isEmpty(roleMenus)) {
                    for (RoleMenuPo roleMenu : roleMenus) {
                        menuIds.add(roleMenu.getMenuId());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(menuIds)) {
                for (Long menuId : menuIds) {
                    //该用户所有的菜单
                    MenuPo menu = menuDao.selectById(menuId);
                    if (menu != null) {
                        menus.add(menu);
                    }
                }
            }
        }
        return menus;
    }


    /**
     * 获取菜单
     *
     * @return
     */
    @Override
    public List<MenuNodePo> queryMenu() {
        LogUtils.LOGGER.debug("查询当前用户角色菜单信息");
        List<MenuNodePo> menuNodes = new ArrayList<>();
        //检查当前用户角色
        List<MenuPo> menus = checkUserOwnRoles();
        if (!CollectionUtils.isEmpty(menus)) {
            menuNodes = MenuConverter.converterToMenuNodePos(menus);
        }
        //构建树形菜单
        return MenuTreeBuilder.build(menuNodes);
    }


    /**
     * 查询用户
     *
     * @param name 用户名
     * @return
     */
    @Override
    public UserPo queryUserByName(String name) {
        LogUtils.LOGGER.debug("查询用户信息: username:{}", name);
        UserPo user = userDao.queryUserByName(name).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询用户失败, 找不到对应的用户, username: {}", name);
            return new ApiRuntimeException(Assert.PARAMETER, "找不到对应的用户");
        });
        return user;
    }


    /**
     * 更新用户禁用状态
     *
     * @param userId
     * @param userStatus
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(Long userId, Boolean userStatus) {
        LogUtils.LOGGER.debug("更新用户当前状态: userId:{}, userStatus:{}", userId, userStatus);
        UserPo user = checkUserIsExit(userId);

        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (user.getId().equals(activeUser.getUser().getId())) {
            throw new ApiRuntimeException(Assert.PARAMETER, "无法改变当前用户状态");
        } else {

            LambdaUpdateWrapper<UserPo> wrapper = new LambdaUpdateWrapper<UserPo>()
                    .eq(true, UserPo::getId, userId);
            user.setStatus(userStatus ? UserStatusEnum.DISABLE.getStatusCode() :
                    UserStatusEnum.AVAILABLE.getStatusCode());
            Assert.DB_OPERATE.sqlSuccess(userDao.update(user, wrapper)).orThrow(iAssert -> {
                LogUtils.LOGGER.error("更新用户状态失败, userId: {}, userPo: {}", userId, user);
                return new ApiRuntimeException(iAssert);
            });

        }
    }


    /**
     * 添加用户
     *
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(UserParam param) {
        LogUtils.LOGGER.debug("新增系统用户: User:{}", param);
        checkAllowAddUser(param);
        UserPo user = new UserPo();
        BeanUtils.copyProperties(param, user);
        //随机生成32位盐值
        String salt = RandomUtils.randomString(32);
        user.setPassword(MD5Utils.md5Encryption(user.getPassword() + salt));
        user.setSalt(salt);
        //添加的用户默认是普通用户
        user.setType(UserTypeEnum.SYSTEM_USER.getTypeCode());
        //添加的用户默认启用
        user.setStatus(UserStatusEnum.AVAILABLE.getStatusCode());
        user.setAvatar("");
        Assert.DB_OPERATE.sqlSuccess(userDao.insert(user))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增用户失败: {}", user);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long userId) {
        LogUtils.LOGGER.debug("删除指定用户信息: userId:{}", userId);
        //检查用户是否允许删除
        UserPo user = checkAllowDelete(userId);

        //删除用户信息
        Assert.DB_OPERATE.sqlSuccess(userDao.deleteById(userId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除用户信息失败, userId: {}", userId, user);
            return new ApiRuntimeException(isAssert);
        });

        //删除用户角色记录(可能存在多条记录,一起删除)
        LambdaQueryWrapper<UserRolePo> wrapper = new LambdaQueryWrapper<UserRolePo>()
                .eq(true, UserRolePo::getUserId, userId);
        userRoleDao.delete(wrapper);
    }


    /**
     * 编辑用户
     *
     * @param userId 用户ID
     */
    @Override
    public UserPo editById(Long userId) {
        LogUtils.LOGGER.debug("编辑用户信息: userId:{}", userId);
        UserPo user = checkUserIsExit(userId);
        return user;
    }


    /**
     * 更新用户信息
     *
     * @param userId
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(Long userId, UserParam param) {
        LogUtils.LOGGER.debug("更新指定用户信息: userId:{}, User:{}", userId, param);
        //检查是否用户更新是否合法
        checkAllowUpdate(userId, param);
        //数据层类型转换
        UserPo user = new UserPo();
        BeanUtils.copyProperties(param, user);
        //更新用户信息
        LambdaUpdateWrapper<UserPo> wrapper = new LambdaUpdateWrapper<UserPo>()
                .eq(true, UserPo::getId, userId);
        Assert.DB_OPERATE.sqlSuccess(userDao.update(user, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新用户信息失败, userId: {}, userPo: {}", userId, user);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 用户拥有的角色ID
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<Long> queryRoleIds(Long userId) {
        LogUtils.LOGGER.debug("查询指定用户角色id: userId:{}", userId);
        UserPo user = checkUserIsExit(userId);
        List<UserRolePo> userRoles = userRoleDao.queryRoleById(userId);
        List<Long> roleIds = new ArrayList<>();

        if (!CollectionUtils.isEmpty(userRoles)) {
            for (UserRolePo userRole : userRoles) {
                RolePo role = roleDao.selectById(userRole.getRoleId());
                if (role != null) {
                    roleIds.add(role.getId());
                }
            }
        }
        return roleIds;
    }


    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds 角色数组
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignRoles(Long userId, Long[] roleIds) {
        LogUtils.LOGGER.debug("分配用户角色信息: userId:{}, roleIds:{}", userId, roleIds);
        //检查用户是否存在
        checkUserIsExit(userId);

        //删除之前用户分配的角色(可能存在多个角色权限)一起删除
        LambdaQueryWrapper<UserRolePo> wrapper = new LambdaQueryWrapper<UserRolePo>()
                .eq(true, UserRolePo::getUserId, userId);
        userRoleDao.delete(wrapper);

        //增加现在分配的角色
        if (roleIds.length > 0) {
            for (Long roleId : roleIds) {
                RolePo role = roleDao.selectById(roleId);
                if (role == null) {
                    throw new ApiRuntimeException(Assert.PARAMETER, "roleId=" + roleId + ",该角色不存在");
                }

                //判断角色状态
                if (role.getStatus() == 0) {
                    throw new ApiRuntimeException(Assert.PARAMETER, "roleName=" + role.getRoleName() + ",该角色已禁用");
                }

                //设置用户和角色对应关系
                UserRolePo userRole = new UserRolePo();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                Assert.DB_OPERATE.sqlSuccess(userRoleDao.insert(userRole))
                        .orThrow(isAssert -> {
                            LogUtils.LOGGER.error("新增用户角色失败: {}", userRole);
                            return new ApiRuntimeException(isAssert);
                        });

            }
        }
    }


    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return token
     */
    @Override
    public String login(String username, String password) {
        LogUtils.LOGGER.debug("[{" + username + "}]的用户正在执行登录");
        UserPo user = queryUserByName(username);
        if (user == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该用户不存在");
        }
        //秘钥为盐
        String salt = user.getSalt();
        //存入用户信息
        String target = MD5Utils.md5Encryption(password + salt);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        //生成Token
        String jwtToken = JwtUtils.createJWT(username, userMap, jwtInfo.getAudience(),
                jwtInfo.getIssuer(), jwtInfo.getExpiresSecond() * 1000, jwtInfo.getBase64Secret());
        Token token = new Token(jwtToken);
        //shiro执行登录
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            throw new ApiRuntimeException(Assert.PARAMETER, e.getMessage());
        }
        //返回密钥和token字符串
        String tokenStr = token.getToken();
        return target + "," + tokenStr;
    }


    /**
     * 检查用户是否存在
     *
     * @param userId 用户ID
     * @return
     */
    public UserPo checkUserIsExit(Long userId) {

        UserPo user = userDao.selectById(userId);
        if (user == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "此用户不存在");
        }
        return user;
    }


    /**
     * 检查当前用户所属权限
     *
     * @param
     * @return
     */
    public List<MenuPo> checkUserOwnRoles() {
        List<MenuPo> menus = null;
        //获取当前登录用户
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        //获取用户角色权限
        if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode()) {
            //超级管理员
            menus = menuDao.selectList(null);
        } else if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_USER.getTypeCode()) {
            //普通系统用户
            menus = activeUser.getMenus();
        }
        return menus;
    }


    /**
     * 检查是否新增用户参数是否合法
     *
     * @param user
     * @return
     */
    public void checkAllowAddUser(UserParam user) {
        @NotBlank(message = "用户名不能为空") String username = user.getUsername();
        @NotNull(message = "部门id不能为空") Long departmentId = user.getDepartmentId();
        //检查新用户用户名是否存在
        int userCount = userDao.queryCountUserByName(username);
        if (userCount != 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该用户名已被占用");
        }
        //检查新用户所属部门是否存在
        DepartmentPo department = departmentDao.selectById(departmentId);
        if (department == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该部门不存在");
        }
    }


    /**
     * 检查是否允许更新用户
     *
     * @param userId 用户ID
     * @param param
     * @return
     */
    public UserPo checkAllowUpdate(Long userId, UserParam param) {
        UserPo user = checkUserIsExit(userId);
        DepartmentPo department = departmentDao.selectById(param.getDepartmentId());
        if (department == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该部门不存在");
        }
        return user;
    }


    /**
     * 检查是否允许删除用户
     *
     * @param userId 用户ID
     */
    public UserPo checkAllowDelete(Long userId) {
        //检查删除用户是否存在
        UserPo user = checkUserIsExit(userId);
        //检查删除用户是否为当前用户
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (user.getId().equals(activeUser.getUser().getId())) {
            throw new ApiRuntimeException(Assert.PARAMETER, "不能删除当前登入用户");
        }
        //检查更新用户用户名是否重复
        List<UserPo> users = userDao.queryUsersByName(user.getUsername());
        if (!CollectionUtils.isEmpty(users)) {
            if (!users.get(0).getId().equals(userId)) {
                throw new ApiRuntimeException(Assert.PARAMETER, "该用户名已被占用");
            }
        }
        return user;
    }
}




