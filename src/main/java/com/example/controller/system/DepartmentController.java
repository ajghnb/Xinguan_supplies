package com.example.controller.system;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.common.converter.DepartmentConverter;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.system.DepartmentParam;
import com.example.model.po.system.DeanPo;
import com.example.model.po.system.DepartmentPo;
import com.example.model.vo.system.DeanVo;
import com.example.model.vo.system.DepartmentVo;
import com.example.service.system.DepartmentService;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Api(tags = "系统模块-部门相关接口")
@RestController
@RequestMapping("/system/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    /**
     * 部门列表
     *
     * @param department
     * @return
     */
    @ApiOperation(value = "部门列表", notes = "部门列表,根据部门名模糊查询")
    @GetMapping("/page")
    public R<PageData<DepartmentVo>> departmentPage(DepartmentParam department) {
        Page<DepartmentPo> departments = departmentService.queryDepartmentList(department);
        System.out.println(departments);
        return R.ofSuccess(new PageData<>(departments)
                .convert(DepartmentConverter::converterToDepartmentVo));
    }


    /**
     * 查找部门主任
     *
     * @param
     * @return
     */
    @ApiOperation(value = "部门主任", notes = "查找部门主任,排除掉已经禁用的用户")
    @GetMapping("/deans")
    public R<List<DeanVo>> queryDeanList() {
        List<DeanPo> deans = departmentService.queryDeanList();
        List<DeanVo> deaVos = deans.stream()
                .map(DeanVo::fromDeanPo)
                .collect(Collectors.toList());
        return R.ofSuccess(deaVos);
    }

    /**
     * 所有部门
     *
     * @param
     * @return
     */
    @ApiOperation(value = "所有部门")
    @GetMapping("/all")
    public R<List<DepartmentVo>> findAll() {
        List<DepartmentPo> departments = departmentService.findAll();
        List<DepartmentVo> departmentVos = departments.stream()
                .map(DepartmentConverter::converterToDepartmentVo)
                .collect(Collectors.toList());
        return R.ofSuccess(departmentVos);
    }


    /**
     * 添加部门
     *
     * @param department
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加部门失败", operation = "添加部门")
    @RequiresPermissions({"department:add"})
    @ApiOperation(value = "添加部门")
    @PostMapping("/add")
    public R<Void> addDepartment(@RequestBody @Validated({Add.class, Default.class}) DepartmentParam department) {
        departmentService.addDepartment(department);
        return R.ofSuccess();
    }

    /**
     * 编辑部门
     *
     * @param departmentId
     * @return
     */
    @ApiOperation(value = "编辑部门")
    @RequiresPermissions({"department:edit"})
    @GetMapping("/edit/{id}")
    public R<DepartmentVo> editById(@PathVariable("id") Long departmentId) {
        DepartmentPo department = departmentService.editById(departmentId);
        DepartmentVo departmentVo = DepartmentConverter.converterToDepartmentVo(department);
        return R.ofSuccess(departmentVo);
    }

    /**
     * 更新部门
     *
     * @param departmentId
     * @param department
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新部门失败", operation = "更新部门")
    @ApiOperation(value = "更新部门")
    @RequiresPermissions({"department:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateDepartment(@PathVariable("id") Long departmentId, @RequestBody @Validated({Edit.class, Default.class}) DepartmentParam department) {
        departmentService.updateDepartment(departmentId, department);
        return R.ofSuccess();
    }

    /**
     * 删除部门
     *
     * @param departmentId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除部门失败", operation = "删除部门")
    @ApiOperation(value = "删除部门")
    @RequiresPermissions({"department:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteDepartment(@PathVariable("id") Long departmentId) {
        departmentService.deleteById(departmentId);
        return R.ofSuccess();
    }

    /**
     * 导出excel
     *
     * @description: 导出文件后流关闭,不需要返回相应信息
     * @param response
     * @return
     */
    @ApiOperation(value = "导出excel", notes = "导出所有部门的excel表格")
    @PostMapping("/excel")
    @RequiresPermissions("department:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败", operation = "导出部门excel")
    public void export(HttpServletResponse response) {
        List<DepartmentPo> departments = this.departmentService.findAll();
        ExcelKit.$Export(DepartmentPo.class, response).downXlsx(departments, false);
    }

}