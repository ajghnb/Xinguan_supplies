package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.common.converter.OutStockConverter;
import com.example.common.converter.OutStockDetailConverter;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.OutStockParam;
import com.example.model.po.business.OutStockPo;
import com.example.model.vo.base.PageQueryParam;
import com.example.model.vo.business.OutStockDetailVo;
import com.example.model.vo.business.OutStockVo;
import com.example.service.business.ConsumerService;
import com.example.service.business.OutStockService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * @author 18237
 */
@Api(tags = "业务模块-物资出库相关接口")
@RestController
@RequestMapping("/business/outStock")
public class OutStockController {

    @Autowired
    private OutStockService outStockService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private OutStockDetailConverter outStockDetailConverter;


    /**
     * 发放单列表
     *
     * @return
     */
    @ApiOperation(value = "出库单列表")
    @GetMapping("/page")
    public R<PageData<OutStockVo>> outStockPage(OutStockParam outStock) {
        Page<OutStockPo> outStocks = outStockService.queryOutStockList(outStock);

        return R.ofSuccess(new PageData<>(outStocks)
                .convert(OutStockConverter::converterToOutStockVo));
    }


    /**
     * 物资发放单详细
     *
     * @param outStockId
     * @param page
     * @return
     */
    @RequiresPermissions({"outStock:detail"})
    @ApiOperation(value = "发放单明细")
    @GetMapping("/detail/{id}")
    public R<OutStockDetailVo> queryOutStockDetail(@PathVariable("id") Long outStockId, PageQueryParam page) {
        OutStockPo outStock = outStockService.queryOutStock(outStockId);
        OutStockDetailVo outStockDetailVo = outStockDetailConverter.converterInStockDetail(outStock,
                page.getPageNum(), page.getPageSize());
        return R.ofSuccess(outStockDetailVo);
    }

    /**
     * 提交物资发放单
     *
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "发放单申请失败", operation = "发放单申请")
    @ApiOperation("提交发放单")
    @PostMapping("/add")
    @RequiresPermissions({"outStock:out"})
    public R<Void> addOutStock(@RequestBody @Validated({Add.class, Default.class}) OutStockParam outStock) {
        //添加物资出库记录
        outStockService.addOutStock(outStock);
        return R.ofSuccess();
    }

    /**
     * 移入回收站
     *
     * @param outStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "发放单回收失败", operation = "发放单回收")
    @ApiOperation(value = "移入回收站", notes = "移入回收站")
    @RequiresPermissions({"outStock:remove"})
    @PutMapping("/remove/{id}")
    public R<Void> removeOutStock(@PathVariable("id") Long outStockId) {
        outStockService.removeOutStock(outStockId);
        return R.ofSuccess();
    }


    /**
     * 恢复数据从回收站
     *
     * @param outStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "发放单恢复失败", operation = "发放单恢复")
    @ApiOperation(value = "恢复数据", notes = "从回收站中恢复入库单")
    @RequiresPermissions({"outStock:back"})
    @PutMapping("/back/{id}")
    public R<Void> rollbackOutStock(@PathVariable("id") Long outStockId) {
        outStockService.rollbackOutStock(outStockId);
        return R.ofSuccess();
    }


    /**
     * 删除物资发放单
     *
     * @param outStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "发放单删除失败", operation = "发放单删除")
    @RequiresPermissions({"outStock:delete"})
    @ApiOperation(value = "删除物资发放单")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteOutStock(@PathVariable("id") Long outStockId) {
        outStockService.deleteById(outStockId);
        return R.ofSuccess();
    }


    /**
     * 发放审核
     *
     * @param outStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "发放单审核失败", operation = "发放单审核")
    @ApiOperation(value = "入库审核")
    @PutMapping("/publish/{id}")
    @RequiresPermissions({"outStock:publish"})
    public R<Void> checkOutStock(@PathVariable("id") Long outStockId) {
        outStockService.checkOutStock(outStockId);
        return R.ofSuccess();
    }


}
