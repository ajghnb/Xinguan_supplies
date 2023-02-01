package com.example.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.utils.LogUtils;
import com.example.dao.system.MenuDao;
import com.example.dao.system.RoleDao;
import com.example.dao.system.RoleMenuDao;
import com.example.enums.RoleStatusEnum;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.system.RoleParam;
import com.example.model.po.system.MenuPo;
import com.example.model.po.system.RoleMenuPo;
import com.example.model.po.system.RolePo;
import com.example.service.system.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleMenuDao roleMenuDao;


    /**
     * 角色列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<RolePo> queryRoleList(RoleParam param) {
        LogUtils.LOGGER.debug("角色列表: 分页参数:{}", param);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return roleDao.queryRoleList(param);
    }

    @Override
    public List<RolePo> findAll() {
        LogUtils.LOGGER.debug("开始查询所有角色");

        return roleDao.selectList(null);
    }


    /**
     * 获取角色已有权限id
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Long> queryMenuIdsByRoleId(Long roleId) {
        LogUtils.LOGGER.debug("查询指定角色id对应菜单: roleId:{}", roleId);

        //核查指定角色是否存在
        checkRoleIsExit(roleId);
        List<Long> roleIds = new ArrayList<>();

        List<RoleMenuPo> roleMenus = roleMenuDao.queryRoleMenusById(roleId);
        if (!CollectionUtils.isEmpty(roleMenus)) {
            roleMenus.stream().forEach((roleMenu) -> {
                roleIds.add(roleMenu.getMenuId());
            });
        }
        return roleIds;
    }


    /**
     * 添加角色
     *
     * @param roleParam
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRole(RoleParam roleParam) {
        LogUtils.LOGGER.debug("开始新增角色: 角色:{}", roleParam);

        String roleName = roleParam.getRoleName();
        int roleNameCount = roleDao.queryRoleCountByName(roleName);
        if (roleNameCount != 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该角色名已被占用");
        }
        RolePo role = RolePo.fromRoleParam(roleParam);
        //默认启用添加的角色
        role.setStatus(RoleStatusEnum.AVAILABLE.getStatusCode());

        Assert.DB_OPERATE.sqlSuccess(roleDao.insert(role))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增角色失败: {}", role);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 编辑角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public RolePo editById(Long roleId) {
        LogUtils.LOGGER.debug("开始编辑 角色信息, roleId: {}", roleId);
        RolePo role = checkRoleIsExit(roleId);
        return role;
    }


    /**
     * 更新角色信息
     *
     * @param roleId
     * @param roleParam
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(Long roleId, RoleParam roleParam) {
        LogUtils.LOGGER.debug("开始更新角色信息, roleId: {}, roleParam: {}", roleId, roleParam);

        String roleName = roleParam.getRoleName();
        //核查指定角色是否存在
        checkRoleIsExit(roleId);
        List<RolePo> roles = roleDao.queryRoleByName(roleName);
        if (!CollectionUtils.isEmpty(roles)) {
            RolePo rolePo = roles.get(0);
            //判断角色名是否重复
            if (rolePo.getId().equals(roleId)) {
                throw new ApiRuntimeException(Assert.PARAMETER, "该角色名已被占用");
            }
        }
        RolePo role = RolePo.fromRoleParam(roleParam);
        //更新角色信息
        LambdaUpdateWrapper<RolePo> wrapper = new LambdaUpdateWrapper<RolePo>()
                .eq(true, RolePo::getId, roleId);
        Assert.DB_OPERATE.sqlSuccess(roleDao.update(role, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新角色信息失败, roleId: {}, RolePo: {}", roleId, role);
            return new ApiRuntimeException(iAssert);
        });

    }


    /**
     * 角色状态
     *
     * @param roleId
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleStatus(Long roleId, Boolean status) {
        LogUtils.LOGGER.debug("开始更新角色状态, roleId: {}, status: {}", roleId, status);

        //核查角色是否存在
        RolePo role = checkRoleIsExit(roleId);
        //更新角色状态
        role.setStatus(status ? RoleStatusEnum.DISABLE.getStatusCode() :
                RoleStatusEnum.AVAILABLE.getStatusCode());

        //更新角色信息
        LambdaUpdateWrapper<RolePo> wrapper = new LambdaUpdateWrapper<RolePo>()
                .eq(true, RolePo::getId, roleId);
        Assert.DB_OPERATE.sqlSuccess(roleDao.update(role, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新角色状态失败, roleId: {}, rolePo: {}", roleId, role);
            return new ApiRuntimeException(iAssert);
        });

    }


    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long roleId) {
        LogUtils.LOGGER.debug("开始删除角色, roleId: {}", roleId);
        //核查指定id角色是否存在
        RolePo role = checkRoleIsExit(roleId);

        //删除角色信息
        Assert.DB_OPERATE.sqlSuccess(roleDao.deleteById(roleId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除角色信息失败, roleId: {}", role, roleId);
            return new ApiRuntimeException(isAssert);
        });

        //删除对应的[角色-菜单]记录(可能存在多条一起删除)
        LambdaQueryWrapper<RoleMenuPo> wrapper = new LambdaQueryWrapper<RoleMenuPo>()
                .eq(true, RoleMenuPo::getRoleId, roleId);
        roleMenuDao.delete(wrapper);
    }


    /**
     * 角色授权
     *
     * @param roleId
     * @param menuIds
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void authority(Long roleId, Long[] menuIds) {
        LogUtils.LOGGER.debug("开始重新授权, roleId: {}, menuIds: {}", roleId, menuIds);

        //核查指定角色信息是否存在
        RolePo role = checkRoleIsExit(roleId);

        //删除角色原来权限(可能存在多种权限)
        QueryWrapper<RoleMenuPo> wrapper = new QueryWrapper<RoleMenuPo>()
                .eq(true, "role_id", roleId);
        roleMenuDao.delete(wrapper);

        //增加现在分配的角色权限
        if (menuIds.length > 0) {
            for (Long menuId : menuIds) {
                MenuPo menu = menuDao.selectById(menuId);
                if (menu == null) {
                    throw new ApiRuntimeException(Assert.PARAMETER, "menuId=" + menuId + ",菜单权限不存在");
                } else {
                    addRoleMenu(roleId, menuId);
                }
            }
        }
    }


    /**
     * 检查指定角色是否存在
     *
     * @param roleId
     * @return
     */
    public RolePo checkRoleIsExit(Long roleId) {

        RolePo role = roleDao.selectById(roleId);
        if (role == null) {
            throw new ApiRuntimeException(Assert.IS_EXIST, "该角色不存在");
        }
        return role;
    }


    /**
     * 新增角色权限
     *
     * @param roleId
     * @param menuId
     * @return
     */
    public void addRoleMenu(Long roleId, Long menuId) {
        LogUtils.LOGGER.debug("开始重新授权:[{角色授权}], roleId: {}, menuIds: {}", roleId, menuId);

        RoleMenuPo roleMenu = new RoleMenuPo();
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(menuId);

        Assert.DB_OPERATE.sqlSuccess(roleMenuDao.insert(roleMenu))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增角色权限失败: {}", roleMenu);
                    return new ApiRuntimeException(isAssert);
                });
    }
}
