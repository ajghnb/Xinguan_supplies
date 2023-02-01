package com.example.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.converter.DepartmentConverter;
import com.example.common.utils.LogUtils;
import com.example.dao.system.DepartmentDao;
import com.example.dao.system.RoleDao;
import com.example.dao.system.UserDao;
import com.example.dao.system.UserRoleDao;
import com.example.enums.BizUserTypeEnum;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.param.system.DepartmentParam;
import com.example.model.po.system.*;
import com.example.model.vo.system.DepartmentVo;
import com.example.service.system.DepartmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Service("departmentServiceImpl")
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 系别列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public Page<DepartmentPo> queryDepartmentList(DepartmentParam pageParam) {
        LogUtils.LOGGER.debug("系统人员部门列表: 分页参数:{}", pageParam);

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        Page<DepartmentPo> departments = departmentDao.queryDepartmentList(pageParam);
        return departments;
    }

    /**
     * 查找所有系主任
     *
     * @return
     */
    @Override
    public List<DeanPo> queryDeanList() {
        LogUtils.LOGGER.debug("开始查询所有部门主任列表");

        List<RolePo> roles = roleDao.queryRoleByName(BizUserTypeEnum.DEAN.getVal());
        ArrayList<DeanPo> deans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles)) {
            RolePo role = roles.get(0);
            List<UserRolePo> userRoles = userRoleDao.queryUserRoleById(role.getId());
            if (!CollectionUtils.isEmpty(userRoles)) {
                //存放所有系主任的id
                List<Long> deanIds = new ArrayList<>();
                for (UserRolePo userRole : userRoles) {
                    deanIds.add(userRole.getRoleId());
                }
                if (deanIds.size() > 0) {
                    for (Long deanId : deanIds) {
                        UserPo user = userDao.selectById(deanId);
                        DeanPo dean = new DeanPo();
                        dean.setName(user.getUsername());
                        dean.setId(user.getId());
                        deans.add(dean);
                    }
                }
            }

        }
        return deans;
    }

    @Override
    public List<DepartmentVo> findAllVO() {
        LogUtils.LOGGER.debug("查询所有院系部门信息");
        List<DepartmentPo> departments = departmentDao.selectList(null);
        //转department的vo层对象
        List<DepartmentVo> departmentVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(departments)) {
            for (DepartmentPo department : departments) {
                int userNum = userDao.queryCountUser(department.getId(), 0);
                DepartmentVo departmentVo = DepartmentConverter.converterToDepartmentVo(department, userNum);
                departmentVos.add(departmentVo);
            }
        }
        return departmentVos;
    }

    @Override
    public List<DepartmentPo> findAll() {
        LogUtils.LOGGER.debug("查询所有院系部门");
        List<DepartmentPo> department = departmentDao.selectList(null);
        return department;
    }


    /**
     * 新增部门信息
     *
     * @param param
     * @return
     */
    @Override
    public void addDepartment(DepartmentParam param) {
        LogUtils.LOGGER.debug("开始新增院系: 部门:{}", param);

        DepartmentPo department = DepartmentPo.fromDepartmentParam(param);
        Assert.DB_OPERATE.sqlSuccess(departmentDao.insert(department))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增部门院系失败: {}", department);
                    return new ApiRuntimeException(isAssert);
                });
    }


    /**
     * 更新部门信息
     *
     * @param departmentId
     * @param param
     * @return
     */
    @Override
    public void updateDepartment(Long departmentId, DepartmentParam param) {
        LogUtils.LOGGER.debug("开始更新部门院系信息, departmentId: {}, department: {}", departmentId, param);
        //检查院系是否存在
        checkDepartmentIsExit(departmentId);
        DepartmentPo department = DepartmentPo.fromDepartmentParam(param);
        //更新院系信息
        LambdaUpdateWrapper<DepartmentPo> wrapper = new LambdaUpdateWrapper<DepartmentPo>()
                .eq(true, DepartmentPo::getId, departmentId);
        Assert.DB_OPERATE.sqlSuccess(departmentDao.update(department, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新部门信息失败, departmentId: {}, department: {}", departmentId, department);
            return new ApiRuntimeException(iAssert);
        });
    }


    /**
     * 编辑院系
     *
     * @param departmentId
     * @return
     */
    @Override
    public DepartmentPo editById(Long departmentId) {
        LogUtils.LOGGER.debug("编辑部门院系: departmentId:{}", departmentId);
        DepartmentPo department = checkDepartmentIsExit(departmentId);
        return department;
    }


    /**
     * 删除部门信息
     *
     * @param departmentId
     * @return
     */
    @Override
    public void deleteById(Long departmentId) {
        LogUtils.LOGGER.debug("开始删除角色, departmentId: {}", departmentId);
        DepartmentPo department = checkDepartmentIsExit(departmentId);

        Assert.DB_OPERATE.sqlSuccess(departmentDao.deleteById(departmentId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除部门信息失败, departmentId: {}", department, departmentId);
            return new ApiRuntimeException(isAssert);
        });
    }


    /**
     * 检查部门是否存在
     *
     * @param departmentId
     * @return
     */
    public DepartmentPo checkDepartmentIsExit(Long departmentId) {
        DepartmentPo department = departmentDao.selectById(departmentId);
        if (department == null) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该不存在部门");
        }
        return department;
    }
}