package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.common.converter.InStockConverter;
import com.example.common.converter.InStockDetailConverter;
import com.example.common.utils.ValidationUtils;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.InStockParam;
import com.example.model.param.business.SupplierParam;
import com.example.model.po.business.InStockPo;
import com.example.model.vo.base.PageQueryParam;
import com.example.model.vo.business.InStockDetailVo;
import com.example.model.vo.business.InStockVo;
import com.example.model.vo.business.SupplierVo;
import com.example.service.business.InStockService;
import com.example.service.business.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * @author 18237
 */
@Api(tags = "业务模块-物资入库相关接口")
@RestController
@RequestMapping("/business/inStock")
public class InStockController {

    @Autowired
    private InStockService inStockService;

    @Autowired
    private InStockDetailConverter inStockDetailConverter;


    /**
     * 入库单列表
     *
     * @param inStock
     * @return
     */
    @ApiOperation(value = "入库单列表")
    @GetMapping("/page")
    public R<PageData<InStockVo>> inStockPage(InStockParam inStock) {
        List<InStockPo> inStocks = inStockService.queryInStockList(inStock);
        return R.ofSuccess(new PageData<>(inStocks).convert(InStockConverter::converterToInStockVo));
    }


    /**
     * 物资入库单详细
     *
     * @param inStockId
     * @param page
     * @return
     */
    @RequiresPermissions({"inStock:detail"})
    @ApiOperation(value = "入库单明细")
    @GetMapping("/detail/{id}")
    public R<InStockDetailVo> queryInStockDetail(@PathVariable("id") Long inStockId, PageQueryParam page) {
        InStockPo instock = inStockService.queryInStock(inStockId);

        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        InStockDetailVo inStockDetailVo = inStockDetailConverter
                .converterInStockDetail(instock, pageNum, pageSize);
        return R.ofSuccess(inStockDetailVo);

    }


    /**
     * 物资入库
     *
     * @param inStock
     * @return
     */
    //@ControllerEndpoint(exceptionMessage = "入库单申请失败", operation = "入库单申请")
    @ApiOperation(value = "物资入库")
    @PostMapping("/add")
    //@RequiresPermissions({"inStock:in"})
    public R<Void> addInStock(@RequestBody @Validated({Add.class, Default.class}) InStockParam inStock) {
        //新增物资入库记录
        inStockService.addInStock(inStock);
        return R.ofSuccess();
    }


    /**
     * 删除物资入库单
     *
     * @param inStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "入库单删除失败", operation = "入库单删除")
    @RequiresPermissions({"inStock:delete"})
    @ApiOperation(value = "删除物资入库单")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteInStock(@PathVariable("id") Long inStockId) {
        inStockService.deleteById(inStockId);
        return R.ofSuccess();
    }

    /**
     * 移入回收站
     *
     * @param inStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "入库单回收失败", operation = "入库单回收")
    @ApiOperation(value = "移入回收站", notes = "移入回收站")
    @RequiresPermissions({"inStock:remove"})
    @PutMapping("/remove/{id}")
    public R<Void> removeInStock(@PathVariable("id") Long inStockId) {
        inStockService.removeInStock(inStockId);
        return R.ofSuccess();
    }

    /**
     * 恢复数据从回收站
     *
     * @param inStockId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "入库单恢复失败", operation = "入库单恢复")
    @ApiOperation(value = "恢复数据", notes = "从回收站中恢复入库单")
    @RequiresPermissions({"inStock:back"})
    @PutMapping("/back/{id}")
    public R<Void> rollbackInStock(@PathVariable("id") Long inStockId) {
        inStockService.rollbackInStock(inStockId);
        return R.ofSuccess();
    }

    /**
     * 入库审核
     *
     * @param inStockId
     * @return
     */
    //@ControllerEndpoint(exceptionMessage = "入库单审核失败", operation = "入库单审核")
    @ApiOperation(value = "入库审核")
    @PutMapping("/publish/{id}")
    //@RequiresPermissions({"inStock:publish"})
    public R<Void> checkInStock(@PathVariable("id") Long inStockId) {
        inStockService.checkInStock(inStockId);
        return R.ofSuccess();
    }
}
