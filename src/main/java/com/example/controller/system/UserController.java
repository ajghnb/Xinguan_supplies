package com.example.controller.system;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.annotation.valid.Login;
import com.example.common.converter.RoleConverter;
import com.example.common.converter.UserConverter;
import com.example.common.utils.RedisUtil;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.dto.LoginInfo;
import com.example.model.param.system.UserParam;
import com.example.model.po.VerifyCode;
import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.RolePo;
import com.example.model.po.system.UserPo;
import com.example.model.vo.system.*;
import com.example.service.system.LoginLogService;
import com.example.service.system.RoleService;
import com.example.service.system.UserService;
import com.github.pagehelper.Page;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "系统模块-用户相关接口")
public class UserController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 用户列表
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户列表", notes = "模糊查询用户列表")
    @GetMapping("/page")
    public R<PageData<UserVo>> userPage(UserParam user) {
        Page<UserPo> userPos = userService.queryUserList(user);
        return R.ofSuccess(new PageData<>(userPos)
                .convert(UserConverter::converterToUserVo));
    }


    /**
     * 查询当前用户信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "用户信息", notes = "用户登入信息")
    @GetMapping("/info")
    public R<UserInfoVo> queryUserInfo() {
        UserInfoVo userInfoVo = userConverter.converterToUserInfoVo();
        return R.ofSuccess(userInfoVo);
    }


    /**
     * 导出excel
     *
     * @param response
     * @return
     */
    @ApiOperation(value = "导出excel", notes = "导出所有用户的excel表格")
    @PostMapping("/excel")
    @RequiresPermissions("user:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败", operation = "导出用户excel")
    public void exportExcel(HttpServletResponse response) {
        List<UserPo> users = this.userService.findAll();
        ExcelKit.$Export(UserPo.class, response).downXlsx(users, false);
    }


    /**
     * 加载当前用户所属菜单
     *
     * @param
     * @return
     */
    @ApiOperation(value = "加载菜单", notes = "用户登入后,根据角色加载菜单树")
    @GetMapping("/findMenu")
    public R<List<MenuNodeVo>> queryMenuList() {
        List<MenuNodePo> menuNodes = userService.queryMenu();
        List<MenuNodeVo> menuNodeVos = menuNodes.stream()
                .map(MenuNodeVo::fromMenuNodePo)
                .collect(Collectors.toList());
        return R.ofSuccess(menuNodeVos);
    }


    /**
     * 更新状态
     *
     * @param userId
     * @param userStatus
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新用户状态失败", operation = "用户|禁用/启用")
    @ApiOperation(value = "用户状态", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"user:status"})
    @PutMapping("/updateStatus/{id}/{status}")
    public R<Void> updateUserStatus(@PathVariable("id") Long userId,
                                @PathVariable("status") Boolean userStatus) {
        userService.updateStatus(userId, userStatus);
        return R.ofSuccess();
    }


    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加用户失败", operation = "添加用户")
    @ApiOperation(value = "添加用户", notes = "添加用户信息")
    @RequiresPermissions({"user:add"})
    @PostMapping("/add")
    public R<Void> addUser(@RequestBody @Validated({Add.class, Default.class}) UserParam user) {
        userService.addUser(user);
        return R.ofSuccess();
    }


    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除用户失败", operation = "删除用户")
    @RequiresPermissions({"user:delete"})
    @ApiOperation(value = "删除用户", notes = "删除用户信息，根据用户ID")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteById(userId);
        return R.ofSuccess();
    }


    /**
     * 编辑用户
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "编辑用户", notes = "获取用户的详情，编辑用户信息")
    @RequiresPermissions({"user:edit"})
    @GetMapping("/edit/{id}")
    public R<UserEditVo> editUser(@PathVariable("id") Long userId) {
        UserPo user = userService.editById(userId);
        UserEditVo userEditVo = UserEditVo.fromUserPo(user);
        return R.ofSuccess(userEditVo);
    }


    /**
     * 更新用户
     *
     * @param userId
     * @param user
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新用户失败", operation = "更新用户")
    @ApiOperation(value = "更新用户", notes = "更新用户信息")
    @RequiresPermissions({"user:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateUser(@PathVariable("id") Long userId,
                              @RequestBody @Validated({Edit.class}) UserParam user) {
        userService.updateUser(userId, user);
        return R.ofSuccess();
    }


    /**
     * 查询用户角色信息
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "已有角色", notes = "根据用户id，获取用户已经拥有的角色")
    @GetMapping("/roles/{id}")
    public R<Map<String, Object>> queryRoleByIds(@PathVariable("id") Long userId) {
        List<Long> roleIds = userService.queryRoleIds(userId);
        //转成前端需要的角色Item
        List<RolePo> roles = roleService.findAll();
        List<RoleTransferItemVo> items = roles.stream()
                .map(RoleConverter::converterToRoleTransferItem)
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("roles", items);
        map.put("values", roleIds);
        return R.ofSuccess(map);
    }


    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "分配角色失败", operation = "分配角色")
    @ApiOperation(value = "分配角色", notes = "角色分配给用户")
    @RequiresPermissions({"user:assign"})
    @PostMapping("/assignRoles/{id}")
    public R<Void> assignRoles(@PathVariable("id") Long userId,
                               @RequestBody Long[] roleIds) {
        userService.assignRoles(userId, roleIds);
        return R.ofSuccess();
    }


    /**
     * 用户登入
     *
     * @param request
     * @param loginInfo
     * @return
     */
    @ApiOperation(value = "用户登入", notes = "接收参数用户名和密码,登入成功后,返回JWTToken")
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request,
                           @RequestBody @Validated({Login.class, Default.class}) LoginInfo loginInfo) {
        //验证码校验
        checkVerifyCode(loginInfo);
        //token及密钥处理返回
        String tokenSalt = userService.login(loginInfo.getUsername(), loginInfo.getPassword());
        redisUtil.delete("verifyCode");
        String[] split = tokenSalt.split(",");
        request.getSession().setAttribute("signKey", split[0]);
        loginLogService.addLoginLog(request);
        return R.ofSuccess(split[1]);
    }



    /**
     * 验证码校验
     *
     * @param loginInfo
     * @return
     */
    public void checkVerifyCode(LoginInfo loginInfo) {

        VerifyCode verifyCode = (VerifyCode) redisUtil.get("verifyCode");
        if(verifyCode == null){
            throw new ApiRuntimeException(Assert.VERIFYCODE_ERROR, "验证码已过期,请刷新重试");
        }

        Date expireDate = verifyCode.getExpireDate();
        //若过期后移除对应的key-value
        if (expireDate.before(new Date())) {
            redisUtil.delete("verifyCode");
            throw new ApiRuntimeException(Assert.VERIFYCODE_ERROR, "验证码已过期,请刷新重试");
        }
        //验证码校验
        String code = verifyCode.getCode().toLowerCase();
        if (!loginInfo.getCaptcha().equals(code)) {
            throw new ApiRuntimeException(Assert.PARAMETER, "验证码错误,请重新输入");
        }
    }
}
