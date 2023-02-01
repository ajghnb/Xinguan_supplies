package com.example.common.converter;

import com.example.dao.system.UserDao;
import com.example.enums.UserTypeEnum;
import com.example.model.po.system.DepartmentPo;
import com.example.model.vo.system.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 18237
 */
@Component
public class DepartmentConverter {


    private static UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao){
        DepartmentConverter.userDao = userDao;
    }

    public static DepartmentVo converterToDepartmentVo(DepartmentPo department){
        DepartmentVo departmentVo = new DepartmentVo();
        //类型转换
        BeanUtils.copyProperties(department, departmentVo);
        //处理vo层数据单独字段total
        int userCount = userDao.queryCountUser(department.getId(),
                UserTypeEnum.SYSTEM_ADMIN.getTypeCode());
        departmentVo.setTotal(userCount);
        return departmentVo;

    }

    public static DepartmentVo converterToDepartmentVo(DepartmentPo department, int userNum) {
        DepartmentVo departmentVo = new DepartmentVo();
        BeanUtils.copyProperties(department, departmentVo);
        departmentVo.setTotal(userNum);
        return departmentVo;
    }

}
