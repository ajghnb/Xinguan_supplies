package com.example.service.system;

import com.example.model.param.system.DepartmentParam;
import com.example.model.po.system.DeanPo;
import com.example.model.po.system.DepartmentPo;
import com.example.model.vo.system.DepartmentVo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author 18237
 */
public interface DepartmentService {

    /**
     * 部门列表
     *
     * @param param
     * @return
     */
    Page<DepartmentPo> queryDepartmentList(DepartmentParam param);

    /**
     * 查询所有部门主任
     *
     * @return
     */
    List<DeanPo> queryDeanList();


    /**
     * 全部部门
     *
     * @return
     */
    List<DepartmentPo> findAll();


    /**
     * 所有部门
     *
     * @return
     */
    List<DepartmentVo> findAllVO();


    /**
     * 添加院部门
     *
     * @param param
     */
    void addDepartment(DepartmentParam param);


    /**
     * 删除院部门
     *
     * @param departmentId
     */
    void deleteById(Long departmentId);


    /**
     * 编辑院部门
     *
     * @param departmentId
     * @return
     */
    DepartmentPo editById(Long departmentId);

    /**
     * 更新院部门
     *
     * @param departmentId
     * @param param
     */
    void updateDepartment(Long departmentId, DepartmentParam param);
}
