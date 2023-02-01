package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.SupplierParam;
import com.example.model.po.business.SupplierPo;
import com.example.model.vo.business.SupplierVo;
import com.example.service.business.SupplierService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Api(tags = "业务模块-物资来源相关接口")
@RestController
@RequestMapping("/business/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 来源列表
     *
     * @param supplier
     * @return
     */
    @ApiOperation(value = "来源列表", notes = "来源列表,根据来源名模糊查询")
    @GetMapping("/page")
    public R<PageData<SupplierVo>> supplierPage(SupplierParam supplier) {
        Page<SupplierPo> suppliers = supplierService.querySupplierList(supplier);
        return R.ofSuccess(new PageData<>(suppliers)
                .convert(SupplierVo::fromSupplierPo));
    }


    /**
     * 所有来源
     *
     * @return
     */
    @ApiOperation(value = "所有来源", notes = "所有来源列表")
    @GetMapping("/all")
    public R<List<SupplierVo>> supplierList() {
        List<SupplierVo> supplierVoList = supplierService.findAll()
                .stream()
                .map(SupplierVo::fromSupplierPo)
                .collect(Collectors.toList());

        return R.ofSuccess(supplierVoList);
    }

    /**
     * 添加来源
     *
     * @param supplier
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资来源添加失败", operation = "物资来源添加")
    @RequiresPermissions({"supplier:add"})
    @ApiOperation(value = "添加来源")
    @PostMapping("/add")
    public R<Void> addSupplier(@RequestBody @Validated({Default.class, Add.class}) SupplierParam supplier) {
        supplierService.addSupplier(supplier);
        return R.ofSuccess();
    }

    /**
     * 编辑来源
     *
     * @param supplierId
     * @return
     */
    @ApiOperation(value = "编辑来源", notes = "编辑来源信息")
    @RequiresPermissions({"supplier:edit"})
    @GetMapping("/edit/{id}")
    public R<SupplierVo> editSupplier(@PathVariable("id") Long supplierId) {
        SupplierPo supplier = supplierService.editById(supplierId);
        SupplierVo supplierVo = SupplierVo.fromSupplierPo(supplier);
        return R.ofSuccess(supplierVo);
    }

    /**
     * 更新来源
     *
     * @param supplierId
     * @param supplier
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资来源更新失败", operation = "物资来源更新")
    @ApiOperation(value = "更新来源", notes = "更新来源信息")
    @RequiresPermissions({"supplier:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateSupplier(@PathVariable("id") Long supplierId,
                                  @RequestBody @Validated({Add.class,Default.class}) SupplierParam supplier) {

        supplierService.updateSupplier(supplierId, supplier);
        return R.ofSuccess();
    }

    /**
     * 删除来源
     *
     * @param supplierId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资来源删除失败", operation = "物资来源删除")
    @ApiOperation(value = "删除来源", notes = "删除来源信息")
    @RequiresPermissions({"supplier:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteSupplier(@PathVariable("id") Long supplierId) {
        supplierService.deleteById(supplierId);
        return R.ofSuccess();
    }


}
