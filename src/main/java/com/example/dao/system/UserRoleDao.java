package com.example.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.po.system.UserRolePo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 18237
 */
@Repository
public interface UserRoleDao extends BaseMapper<UserRolePo> {

    /**
     * 查询用户角色
     *
     * @param roleId
     * @return
     */
    @Select("select * from xinguan.tb_user_role where role_id = #{id}")
    List<UserRolePo> queryUserRoleById(@Param("id") Long roleId);


    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    @Select("select * from xinguan.tb_user_role where user_id = #{id}")
    List<UserRolePo> queryRoleById(@Param("id") Long userId);

    /**
     * 删除用户角色
     *
     * @param userId
     * @return
     */
    @Delete("delete from xinguan.tb_user_role where user_id =#{id}")
    Optional<Void> deleteByUserId(@Param("id") Long userId);


}
