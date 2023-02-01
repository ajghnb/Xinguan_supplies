package com.example.controller.system;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.R;
import com.example.model.param.system.MenuParam;
import com.example.model.po.system.MenuNodePo;
import com.example.model.po.system.MenuPo;
import com.example.model.vo.system.MenuVo;
import com.example.service.system.MenuService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 18237
 */
@Api(tags = "系统模块-菜单权限相关接口")
@RequestMapping("/system/menu")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 加载菜单树
     *
     * @param
     * @return
     */
    @ApiOperation(value = "加载菜单树", notes = "获取所有菜单树，以及展开项")
    @GetMapping("/tree")
    public R<Map<String, Object>> loadMenuTree() {
        List<MenuNodePo> menuTree = menuService.getMenuTree();
        List<Long> menuIds = menuService.queryOpenMenuIds();

        Map<String, Object> menuTreeMap = new HashMap<>();
        menuTreeMap.put("tree", menuTree);
        menuTreeMap.put("openIds", menuIds);

        return R.ofSuccess(menuTreeMap);
    }


    /**
     * 新增菜单/按钮
     *
     * @param menu
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "新增菜单/按钮失败", operation = "新增菜单/按钮")
    @ApiOperation(value = "新增菜单")
    @RequiresPermissions({"menu:add"})
    @PostMapping("/add")
    public R<Map<String, Object>> addMenu(@RequestBody @Validated({Add.class, Edit.class}) MenuParam menu) {
        MenuPo menuPo = menuService.addMenu(menu);
        Map<String, Object> menuTreeMap = new HashMap<>();
        menuTreeMap.put("id", menuPo.getId());
        menuTreeMap.put("menuName", menuPo.getMenuName());
        menuTreeMap.put("children", new ArrayList<>());
        menuTreeMap.put("icon", menuPo.getIcon());
        return R.ofSuccess(menuTreeMap);
    }


    /**
     * 菜单详情
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "菜单详情", notes = "根据id编辑菜单，获取菜单详情")
    @RequiresPermissions({"menu:edit"})
    @GetMapping("/edit/{id}")
    public R<MenuVo> editMenu(@PathVariable("id") Long menuId) {
        MenuPo menu = menuService.editById(menuId);

        MenuVo menuVo = MenuVo.fromMenuPo(menu);
        return R.ofSuccess(menuVo);
    }

    /**
     * 更新菜单
     *
     * @param menuId
     * @param menu
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新菜单失败", operation = "更新菜单")
    @ApiOperation(value = "更新菜单", notes = "根据id更新菜单节点")
    @RequiresPermissions({"menu:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateMenu(@PathVariable("id") Long menuId, @RequestBody @Validated({Edit.class, Default.class}) MenuParam menu) {
        menuService.updateMenu(menuId, menu);

        return R.ofSuccess();
    }


    /**
     * 删除菜单/按钮
     *
     * @param menuId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除菜单/按钮失败", operation = "删除菜单/按钮")
    @ApiOperation(value = "删除菜单", notes = "根据id删除菜单节点")
    @RequiresPermissions({"menu:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteMenu(@PathVariable("id") Long menuId) {
        menuService.deleteById(menuId);
        return R.ofSuccess();
    }


    /**
     * 导出excel
     *
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有菜单的excel表格")
    @PostMapping("/excel")
    @RequiresPermissions("menu:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败", operation = "导出菜单excel")
    public void exportExcel(HttpServletResponse response) {
        List<MenuPo> menus = menuService.findAll();
        ExcelKit.$Export(MenuPo.class, response).downXlsx(menus, false);
    }
}