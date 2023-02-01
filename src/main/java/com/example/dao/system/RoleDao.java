package com.example.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.system.RoleParam;
import com.example.model.po.system.RolePo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 18237
 */
@Repository
public interface RoleDao extends BaseMapper<RolePo> {

    /**
     * 角色列表
     *
     * @param pageParam
     * @return
     */
    Page<RolePo> queryRoleList(RoleParam pageParam);


    /**
     * 角色列表
     *
     * @param roleName
     * @return
     */
    @Select("select count(*) from xinguan.tb_role where role_name = #{name}")
    int queryRoleCountByName(@Param("name") String roleName);


    /**
     * 角色列表
     *
     * @param roleName
     * @return
     */
    @Select("select * from xinguan.tb_role where role_name = #{name}")
    List<RolePo> queryRoleByName(@Param("name") String roleName);


}
