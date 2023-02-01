package com.example.controller.system;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.system.RoleParam;
import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.RolePo;
import com.example.model.vo.system.MenuNodeVo;
import com.example.model.vo.system.RoleVo;
import com.example.service.system.MenuService;
import com.example.service.system.RoleService;
import com.github.pagehelper.Page;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Api(tags = "系统模块-角色相关接口")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @Autowired
    private MenuService menuService;

    /**
     * 角色列表
     *
     * @param role
     * @return
     */
    @ApiOperation(value = "角色列表")
    @GetMapping("/page")
    public R<PageData<RoleVo>> rolePage(RoleParam role) {
        Page<RolePo> roles = roleService.queryRoleList(role);

        return R.ofSuccess(new PageData<>(roles)
                .convert(RoleVo::fromRolePo));

    }

    /**
     * 角色拥有的菜单权限id和菜单树
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "角色菜单")
    @GetMapping("/query/roleMenu/{id}")
    public R<Map<String, Object>> roleMenuMap(@PathVariable("id") Long roleId) {
        List<MenuNodePo> menuNodes = menuService.getMenuTree();
        List<MenuNodeVo> menuTree = menuNodes.stream()
                .map(MenuNodeVo::fromMenuNodePo)
                .collect(Collectors.toList());

        List<Long> menuIds = roleService.queryMenuIdsByRoleId(roleId);
        List<Long> menuOpenIds = menuService.queryOpenMenuIds();

        Map<String, Object> roleMenuMap = new HashMap<>();
        roleMenuMap.put("menuTree", menuTree);
        roleMenuMap.put("menuIds", menuIds);
        roleMenuMap.put("menuOpenIds", menuOpenIds);
        return R.ofSuccess(roleMenuMap);
    }

    /**
     * 添加角色信息
     *
     * @param role
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加角色失败", operation = "添加角色")
    @ApiOperation(value = "添加角色")
    @RequiresPermissions({"role:add"})
    @PostMapping("/add")
    public R<Void> addRole(@RequestBody @Validated({Add.class, Default.class}) RoleParam role) {
        roleService.addRole(role);
        return R.ofSuccess();
    }


    /**
     * 编辑角色信息
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "编辑用户", notes = "根据id更新角色信息")
    @GetMapping("/edit/{id}")
    @RequiresPermissions({"role:edit"})
    public R<RoleVo> editRole(@PathVariable("id") Long roleId) {
        RolePo role = roleService.editById(roleId);
        RoleVo roleVo = RoleVo.fromRolePo(role);
        return R.ofSuccess(roleVo);
    }


    /**
     * 更新角色
     *
     * @param roleId
     * @param role
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新角色失败", operation = "更新角色")
    @ApiOperation(value = "更新角色", notes = "根据id更新角色信息")
    @RequiresPermissions({"role:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateRole(@PathVariable("id") Long roleId, @RequestBody @Validated({Edit.class, Default.class}) RoleParam role) {
        roleService.updateRole(roleId, role);
        return R.ofSuccess();
    }


    /**
     * 更新角色状态
     *
     * @param roleId
     * @param roleStatus
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "角色更新状态失败", operation = "角色|禁用/启用")
    @ApiOperation(value = "更新状态", notes = "禁用和更新两种状态")
    @RequiresPermissions({"role:status"})
    @PutMapping("/updateStatus/{id}/{status}")
    public R<Void> updateRoleStatus(@PathVariable("id") Long roleId, @PathVariable("status") Boolean roleStatus) {
        roleService.updateRoleStatus(roleId, roleStatus);
        return R.ofSuccess();
    }


    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除角色失败", operation = "删除角色")
    @ApiOperation(value = "删除角色", notes = "根据id删除角色信息")
    @RequiresPermissions({"role:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteRole(@PathVariable("id") Long roleId) {
        roleService.deleteById(roleId);
        return R.ofSuccess();
    }


    /**
     * 角色授权
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "角色授权失败", operation = "角色授权")
    @ApiOperation(value = "角色授权")
    @RequiresPermissions({"role:authority"})
    @PostMapping("/authority/{id}")
    public R<Void> authority(@PathVariable("id") Long roleId, @RequestBody Long[] menuIds) {
        roleService.authority(roleId, menuIds);
        return R.ofSuccess();
    }


    /**
     * 导出excel
     *
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有角色的excel表格")
    @PostMapping("/excel")
    @RequiresPermissions("role:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败", operation = "导出角色excel")
    public void exportExcel(HttpServletResponse response) {
        List<RolePo> roles = roleService.findAll();
        ExcelKit.$Export(RolePo.class, response).downXlsx(roles, false);
    }


}
