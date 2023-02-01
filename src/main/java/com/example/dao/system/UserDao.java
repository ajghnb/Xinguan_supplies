package com.example.dao.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.system.UserParam;
import com.example.model.po.system.UserPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 18237
 */
@Repository
public interface UserDao extends BaseMapper<UserPo> {
    /**
     * 根据指定条件查询用户数量
     *
     * @param param
     * @return
     */
    Page<UserPo> queryUserList(UserParam param);


    /**
     * 根据指定条件查询用户数量
     *
     * @param departmentId
     * @param userType
     * @return
     */
    @Select("select count(*) from xinguan.tb_user where department_id = #{id} and type != #{type}")
    int queryCountUser(@Param("id") Long departmentId, @Param("type") int userType);

    /**
     * 根据指定条件查询用户数量
     *
     * @param username
     * @return
     */
    @Select("select count(*) from xinguan.tb_user where username = #{username}")
    int queryCountUserByName(String username);


    /**
     * 查询指定用户
     *
     * @param username
     * @return
     */
    @Select("select * from xinguan.tb_user where username = #{username}")
    Optional<UserPo> queryUserByName(String username);

    /**
     * 查询指定用户
     *
     * @param username
     * @return
     */
    @Select("select * from xinguan.tb_user where username = #{username}")
    List<UserPo> queryUsersByName(String username);


}

