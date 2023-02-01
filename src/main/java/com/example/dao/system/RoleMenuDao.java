package com.example.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.po.system.RoleMenuPo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 18237
 */
@Repository
public interface RoleMenuDao extends BaseMapper<RoleMenuPo> {

    /**
     * 角色菜单对应关系
     *
     * @param menuId
     * @return
     */
    @Select("select * from xinguan.tb_role_menu where menu_id = #{id}")
    List<RoleMenuPo> queryRoleMenus(@Param("id") Long menuId);

    /**
     * 角色菜单对应关系
     *
     * @param roleId
     * @return
     */
    @Select("select * from xinguan.tb_role_menu where role_id = #{id}")
    List<RoleMenuPo> queryRoleMenusById(@Param("id") Long roleId);


}
