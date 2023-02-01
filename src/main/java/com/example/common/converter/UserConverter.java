package com.example.common.converter;

import com.example.dao.system.DepartmentDao;
import com.example.enums.UserTypeEnum;
import com.example.model.ActiveUser;
import com.example.model.po.system.DepartmentPo;
import com.example.model.po.system.RolePo;
import com.example.model.po.system.UserPo;
import com.example.model.vo.system.UserInfoVo;
import com.example.model.vo.system.UserVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Component
public class UserConverter {


    private static DepartmentDao departmentDao;

    @Autowired
    public void setDepartmentDao(DepartmentDao departmentDao) {
        UserConverter.departmentDao = departmentDao;
    }

    public UserInfoVo converterToUserInfoVo() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        UserInfoVo UserInfoVo = new UserInfoVo();
        UserInfoVo.setAvatar(activeUser.getUser().getAvatar());
        UserInfoVo.setUsername(activeUser.getUser().getUsername());
        UserInfoVo.setUrl(activeUser.getUrls());
        UserInfoVo.setNickname(activeUser.getUser().getNickname());
        List<String> roleNames = activeUser.getRoles()
                .stream().map(RolePo::getRoleName)
                .collect(Collectors.toList());
        UserInfoVo.setRoles(roleNames);
        UserInfoVo.setPerms(activeUser.getPermissions());
        UserInfoVo.setIsAdmin(activeUser.getUser().getType() == UserTypeEnum.SYSTEM_ADMIN.getTypeCode());
        DepartmentPo department = departmentDao.selectById(activeUser.getUser().getDepartmentId());
        if (department != null) {
            UserInfoVo.setDepartment(department.getName());
        }
        return UserInfoVo;
    }

    public static UserVo converterToUserVo(UserPo user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setStatus(user.getStatus() == 0);
        DepartmentPo department = departmentDao.selectById(user.getDepartmentId());
        if (department != null && department.getName() != null) {
            userVo.setDepartmentName(department.getName());
            userVo.setDepartmentId(department.getId());
        }
        return userVo;
    }
}
