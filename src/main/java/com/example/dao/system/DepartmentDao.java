package com.example.dao.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.system.DepartmentParam;
import com.example.model.po.system.DepartmentPo;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

/**
 * @author 18237
 */
@Repository
public interface DepartmentDao extends BaseMapper<DepartmentPo> {

    /**
     * 查询部门列表
     *
     * @param pageParam
     * @return
     */
    Page<DepartmentPo> queryDepartmentList(DepartmentParam pageParam);

}
