package com.example.config.shiro;


import com.example.common.utils.JwtUtils;
import com.example.config.JwtInfo;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.ActiveUser;
import com.example.model.param.system.RoleParam;
import com.example.model.po.system.MenuPo;
import com.example.model.po.system.RolePo;
import com.example.model.po.system.UserPo;
import com.example.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Service
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private JwtInfo jwtInfo;

    @Autowired
    private UserService userService;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Token;
    }


    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

        if (activeUser.getUser().getType() == 0) {
            authorizationInfo.addStringPermission("*:*");
        } else {
            List<String> permissions = new ArrayList<>(activeUser.getPermissions());
            List<RolePo> roles = activeUser.getRoles();
            //授权角色
            if (!CollectionUtils.isEmpty(roles)) {
                for (RolePo role : roles) {
                    authorizationInfo.addRole(role.getRoleName());
                }
            }
            //授权权限
            if (!CollectionUtils.isEmpty(permissions)) {
                for (String permission : permissions) {
                    if (permission != null && !"".equals(permission)) {
                        authorizationInfo.addStringPermission(permission);
                    }
                }
            }
        }
        return authorizationInfo;


    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JwtUtils.getUsername(token, jwtInfo.getBase64Secret());
        if (username == null) {
            throw new ApiRuntimeException(Assert.TOKEN, "用户校验信息token错误,请重新登录");
        }
        UserPo user = userService.queryUserByName(username);
        if (user == null) {
            throw new ApiRuntimeException(Assert.IS_EXIST, "该账户用户不存在!");
        }
        if (JwtUtils.isExpire(token, jwtInfo.getBase64Secret())) {
            throw new ApiRuntimeException(Assert.INVALID_LOGIN, "token过期,请重新登入!");
        }
        if (!JwtUtils.validate(token, jwtInfo)) {
            throw new ApiRuntimeException(Assert.PASSWORD_ERROR, "密码错误,请重试");
        }
        if (user.getStatus() == 0) {
            throw new ApiRuntimeException(Assert.USER_COUNT, "用户账户被锁定,请联系管理员");
        }
        //如果验证通过，获取用户的角色
        List<RolePo> roles = userService.queryRolesById(user.getId());
        //查询用户的所有菜单(包括了菜单和按钮)
        List<RoleParam> roleParams = roles.stream()
                .map(RoleParam::fromRolePo)
                .collect(Collectors.toList());
        List<MenuPo> menus = userService.queryMenuByRoles(roleParams);
        Set<String> urls = new HashSet<>();
        Set<String> perms = new HashSet<>();

        if (!CollectionUtils.isEmpty(menus)) {

            for (MenuPo menu : menus) {
                String url = menu.getUrl();
                String per = menu.getPerms();
                if (menu.getType() == 0 && !StringUtils.isEmpty(url)) {
                    urls.add(menu.getUrl());
                }
                if (menu.getType() == 1 && !StringUtils.isEmpty(per)) {
                    perms.add(menu.getPerms());
                }
            }
        }
        //过滤出url,和用户的权限
        ActiveUser activeUser = new ActiveUser();
        activeUser.setRoles(roles);
        activeUser.setUser(user);
        activeUser.setMenus(menus);
        activeUser.setUrls(urls);
        activeUser.setPermissions(perms);
        return new SimpleAuthenticationInfo(activeUser, token, getName());
    }
}
