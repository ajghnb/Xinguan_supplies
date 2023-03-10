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
     * ????????????
     *
     * @param param
     * @return
     */
    @Override
    public Page<UserPo> queryUserList(UserParam param) {
        LogUtils.LOGGER.debug("????????????????????????: ????????????:{}", param);
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return userDao.queryUserList(param);
    }


    /**
     * ??????????????????
     *
     * @param
     * @return
     */
    @Override
    public List<UserPo> findAll() {
        LogUtils.LOGGER.debug("??????????????????????????????");
        return userDao.selectList(null);
    }


    /**
     * ??????????????????
     *
     * @param userId ??????ID
     * @return
     */
    @Override
    public List<RolePo> queryRolesById(Long userId) {
        LogUtils.LOGGER.debug("????????????????????????: userId:{}", userId);
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
     * ????????????
     *
     * @param roleParams ???????????????
     * @return
     */
    @Override
    public List<MenuPo> queryMenuByRoles(List<RoleParam> roleParams) {
        LogUtils.LOGGER.debug("????????????????????????: ????????????:{}", roleParams);
        List<MenuPo> menus = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleParams)) {
            //?????????????????????id
            Set<Long> menuIds = new HashSet<>();
            List<RoleMenuPo> roleMenus;
            for (RoleParam roleParam : roleParams) {
                //????????????ID????????????ID
                roleMenus = roleMenuDao.queryRoleMenusById(roleParam.getId());
                if (!CollectionUtils.isEmpty(roleMenus)) {
                    for (RoleMenuPo roleMenu : roleMenus) {
                        menuIds.add(roleMenu.getMenuId());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(menuIds)) {
                for (Long menuId : menuIds) {
                    //????????????????????????
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
     * ????????????
     *
     * @return
     */
    @Override
    public List<MenuNodePo> queryMenu() {
        LogUtils.LOGGER.debug("????????????????????????????????????");
        List<MenuNodePo> menuNodes = new ArrayList<>();
        //????????????????????????
        List<MenuPo> menus = checkUserOwnRoles();
        if (!CollectionUtils.isEmpty(menus)) {
            menuNodes = MenuConverter.converterToMenuNodePos(menus);
        }
        //??????????????????
        return MenuTreeBuilder.build(menuNodes);
    }


    /**
     * ????????????
     *
     * @param name ?????????
     * @return
     */
    @Override
    public UserPo queryUserByName(String name) {
        LogUtils.LOGGER.debug("??????????????????: username:{}", name);
        UserPo user = userDao.queryUserByName(name).orElseThrow(() -> {
            LogUtils.LOGGER.error("??????????????????, ????????????????????????, username: {}", name);
            return new ApiRuntimeException(Assert.PARAMETER, "????????????????????????");
        });
        return user;
    }


    /**
     * ????????????????????????
     *
     * @param userId
     * @param userStatus
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(Long userId, Boolean userStatus) {
        LogUtils.LOGGER.debug("????????????????????????: userId:{}, userStatus:{}", userId, userStatus);
        UserPo user = checkUserIsExit(userId);

        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (user.getId().equals(activeUser.getUser().getId())) {
            throw new ApiRuntimeException(Assert.PARAMETER, "??????????????????????????????");
        } else {

            LambdaUpdateWrapper<UserPo> wrapper = new LambdaUpdateWrapper<UserPo>()
                    .eq(true, UserPo::getId, userId);
            user.setStatus(userStatus ? UserStatusEnum.DISABLE.getStatusCode() :
                    UserStatusEnum.AVAILABLE.getStatusCode());
            Assert.DB_OPERATE.sqlSuccess(userDao.update(user, wrapper)).orThrow(iAssert -> {
                LogUtils.LOGGER.error("????????????????????????, userId: {}, userPo: {}", userId, user);
                return new ApiRuntimeException(iAssert);
            });

        }
    }


    /**
     * ????????????
     *
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(UserParam param) {
        LogUtils.LOGGER.debug("??????????????????: User:{}", param);
        checkAllowAddUser(param);
        UserPo user = new UserPo();
        BeanUtils.copyProperties(param, user);
        //????????????32?????????
        String salt = RandomUtils.randomString(32);
        user.setPassword(MD5Utils.md5Encryption(user.getPassword() + salt));
        user.setSalt(salt);
        //????????????????????????????????????
        user.setType(UserTypeEnum.SYSTEM_USER.getTypeCode());
        //???????????????????????????
        user.setStatus(UserStatusEnum.AVAILABLE.getStatusCode());
        user.setAvatar("");
        Assert.DB_OPERATE.sqlSuccess(userDao.insert(user))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("??????????????????: {}", user);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * ????????????
     *
     * @param userId ??????ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long userId) {
        LogUtils.LOGGER.debug("????????????????????????: userId:{}", userId);
        //??????????????????????????????
        UserPo user = checkAllowDelete(userId);

        //??????????????????
        Assert.DB_OPERATE.sqlSuccess(userDao.deleteById(userId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  ????????????????????????, userId: {}", userId, user);
            return new ApiRuntimeException(isAssert);
        });

        //????????????????????????(????????????????????????,????????????)
        LambdaQueryWrapper<UserRolePo> wrapper = new LambdaQueryWrapper<UserRolePo>()
                .eq(true, UserRolePo::getUserId, userId);
        userRoleDao.delete(wrapper);
    }


    /**
     * ????????????
     *
     * @param userId ??????ID
     */
    @Override
    public UserPo editById(Long userId) {
        LogUtils.LOGGER.debug("??????????????????: userId:{}", userId);
        UserPo user = checkUserIsExit(userId);
        return user;
    }


    /**
     * ??????????????????
     *
     * @param userId
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(Long userId, UserParam param) {
        LogUtils.LOGGER.debug("????????????????????????: userId:{}, User:{}", userId, param);
        //????????????????????????????????????
        checkAllowUpdate(userId, param);
        //?????????????????????
        UserPo user = new UserPo();
        BeanUtils.copyProperties(param, user);
        //??????????????????
        LambdaUpdateWrapper<UserPo> wrapper = new LambdaUpdateWrapper<UserPo>()
                .eq(true, UserPo::getId, userId);
        Assert.DB_OPERATE.sqlSuccess(userDao.update(user, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("????????????????????????, userId: {}, userPo: {}", userId, user);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * ?????????????????????ID
     *
     * @param userId ??????id
     * @return
     */
    @Override
    public List<Long> queryRoleIds(Long userId) {
        LogUtils.LOGGER.debug("????????????????????????id: userId:{}", userId);
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
     * ????????????
     *
     * @param userId
     * @param roleIds ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignRoles(Long userId, Long[] roleIds) {
        LogUtils.LOGGER.debug("????????????????????????: userId:{}, roleIds:{}", userId, roleIds);
        //????????????????????????
        checkUserIsExit(userId);

        //?????????????????????????????????(??????????????????????????????)????????????
        LambdaQueryWrapper<UserRolePo> wrapper = new LambdaQueryWrapper<UserRolePo>()
                .eq(true, UserRolePo::getUserId, userId);
        userRoleDao.delete(wrapper);

        //???????????????????????????
        if (roleIds.length > 0) {
            for (Long roleId : roleIds) {
                RolePo role = roleDao.selectById(roleId);
                if (role == null) {
                    throw new ApiRuntimeException(Assert.PARAMETER, "roleId=" + roleId + ",??????????????????");
                }

                //??????????????????
                if (role.getStatus() == 0) {
                    throw new ApiRuntimeException(Assert.PARAMETER, "roleName=" + role.getRoleName() + ",??????????????????");
                }

                //?????????????????????????????????
                UserRolePo userRole = new UserRolePo();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                Assert.DB_OPERATE.sqlSuccess(userRoleDao.insert(userRole))
                        .orThrow(isAssert -> {
                            LogUtils.LOGGER.error("????????????????????????: {}", userRole);
                            return new ApiRuntimeException(isAssert);
                        });

            }
        }
    }


    /**
     * ????????????
     *
     * @param username
     * @param password
     * @return token
     */
    @Override
    public String login(String username, String password) {
        LogUtils.LOGGER.debug("[{" + username + "}]???????????????????????????");
        UserPo user = queryUserByName(username);
        if (user == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "??????????????????");
        }
        //????????????
        String salt = user.getSalt();
        //??????????????????
        String target = MD5Utils.md5Encryption(password + salt);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        //??????Token
        String jwtToken = JwtUtils.createJWT(username, userMap, jwtInfo.getAudience(),
                jwtInfo.getIssuer(), jwtInfo.getExpiresSecond() * 1000, jwtInfo.getBase64Secret());
        Token token = new Token(jwtToken);
        //shiro????????????
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            throw new ApiRuntimeException(Assert.PARAMETER, e.getMessage());
        }
        //???????????????token?????????
        String tokenStr = token.getToken();
        return target + "," + tokenStr;
    }


    /**
     * ????????????????????????
     *
     * @param userId ??????ID
     * @return
     */
    public UserPo checkUserIsExit(Long userId) {

        UserPo user = userDao.selectById(userId);
        if (user == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "??????????????????");
        }
        return user;
    }


    /**
     * ??????????????????????????????
     *
     * @param
     * @return
     */
    public List<MenuPo> checkUserOwnRoles() {
        List<MenuPo> menus = null;
        //????????????????????????
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        //????????????????????????
        if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode()) {
            //???????????????
            menus = menuDao.selectList(null);
        } else if (activeUser.getUser().getType() == UserTypeEnum.SYSTEM_USER.getTypeCode()) {
            //??????????????????
            menus = activeUser.getMenus();
        }
        return menus;
    }


    /**
     * ??????????????????????????????????????????
     *
     * @param user
     * @return
     */
    public void checkAllowAddUser(UserParam user) {
        @NotBlank(message = "?????????????????????") String username = user.getUsername();
        @NotNull(message = "??????id????????????") Long departmentId = user.getDepartmentId();
        //????????????????????????????????????
        int userCount = userDao.queryCountUserByName(username);
        if (userCount != 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, "????????????????????????");
        }
        //???????????????????????????????????????
        DepartmentPo department = departmentDao.selectById(departmentId);
        if (department == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "??????????????????");
        }
    }


    /**
     * ??????????????????????????????
     *
     * @param userId ??????ID
     * @param param
     * @return
     */
    public UserPo checkAllowUpdate(Long userId, UserParam param) {
        UserPo user = checkUserIsExit(userId);
        DepartmentPo department = departmentDao.selectById(param.getDepartmentId());
        if (department == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "??????????????????");
        }
        return user;
    }


    /**
     * ??????????????????????????????
     *
     * @param userId ??????ID
     */
    public UserPo checkAllowDelete(Long userId) {
        //??????????????????????????????
        UserPo user = checkUserIsExit(userId);
        //???????????????????????????????????????
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (user.getId().equals(activeUser.getUser().getId())) {
            throw new ApiRuntimeException(Assert.PARAMETER, "??????????????????????????????");
        }
        //???????????????????????????????????????
        List<UserPo> users = userDao.queryUsersByName(user.getUsername());
        if (!CollectionUtils.isEmpty(users)) {
            if (!users.get(0).getId().equals(userId)) {
                throw new ApiRuntimeException(Assert.PARAMETER, "????????????????????????");
            }
        }
        return user;
    }
}




