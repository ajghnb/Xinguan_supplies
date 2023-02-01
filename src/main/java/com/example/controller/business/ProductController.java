package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.common.converter.ProductConverter;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.ProductParam;
import com.example.model.po.business.ProductPo;
import com.example.model.vo.business.ProductVo;
import com.example.service.business.ProductService;
import com.example.annotation.valid.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * @author 18237
 */
@RestController
@Api(tags = "业务模块-物资资料相关接口")
@RequestMapping("/business/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * 全部物资列表
     * @param product
     * @return
     */
    @ApiOperation(value = "物资列表", notes = "物资列表,根据物资名模糊查询")
    @GetMapping("/page")
    public R<PageData<ProductVo>> productPage(@Validated({Default.class, Page.class}) ProductParam product) {
        System.out.println(product.getStatus());
        product.buildCategorySearch(product.getCategorys());
        List<ProductPo> products = productService.queryProductList(product);

        return R.ofSuccess(new PageData<>(products)
                .convert(ProductConverter::converterToProductVo));
    }


    /**
     * 可入库物资(入库页面使用)
     *
     * @param product
     * @return
     */
    @ApiOperation(value = "可入库物资列表", notes = "物资列表,根据物资名模糊查询")
    @GetMapping("/storage")
    public R<PageData<ProductVo>> findProducts(@Validated({Default.class, Page.class}) ProductParam product) {
        product.setStatus(0);
        product.buildCategorySearch(product.getCategorys());
        List<ProductPo> products = productService.queryProductList(product);

        return R.ofSuccess(new PageData<>(products)
                .convert(ProductConverter::converterToProductVo));
    }


    /**
     * 添加物资
     *
     * @param product
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加物资失败", operation = "物资资料添加")
    @ApiOperation(value = "添加物资")
    @RequiresPermissions({"product:add"})
    @PostMapping("/add")
    public R<Void> addProduct(@RequestBody @Validated({Default.class, Add.class}) ProductParam product) {
        System.out.println("开始准备添加物资");
        productService.addProduct(product);
        return R.ofSuccess();
    }


    /**
     * 编辑物资
     *
     * @param productId
     * @return
     */
    @ApiOperation(value = "编辑物资", notes = "编辑物资信息")
    @RequiresPermissions({"product:edit"})
    @GetMapping("/edit/{id}")
    public R<ProductVo> editProduct(@PathVariable("id") Long productId) {
        ProductPo product = productService.editById(productId);
        return R.ofSuccess(ProductConverter.converterToProductVo(product));
    }


    /**
     * 删除物资
     *
     * @param productId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除物资失败", operation = "物资资料删除")
    @ApiOperation(value = "删除物资", notes = "删除物资信息")
    @RequiresPermissions({"product:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteById(productId);
        return R.ofSuccess();
    }


    /**
     * 更新物资
     *
     * @param productId
     * @param product
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新物资失败", operation = "物资资料更新")
    @ApiOperation(value = "更新物资", notes = "更新物资信息")
    @RequiresPermissions({"product:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateProduct(@PathVariable("id") Long productId, @RequestBody @Validated({Default.class, Edit.class})  ProductParam product) {
        productService.updateProduct(productId, product);
        return R.ofSuccess();
    }


    /**
     * 移入回收站
     *
     * @param productId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "回收物资失败", operation = "物资资料回收")
    @ApiOperation(value = "移入回收站", notes = "移入回收站")
    @RequiresPermissions({"product:remove"})
    @PutMapping("/remove/{id}")
    public R<Void> removeProduct(@PathVariable("id") Long productId) {
        productService.removeProduct(productId);
        return R.ofSuccess();
    }


    /**
     * 恢复数据从回收站
     *
     * @param productId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "恢复物资失败", operation = "物资资料恢复")
    @ApiOperation(value = "恢复物资", notes = "从回收站中恢复物资")
    @RequiresPermissions({"product:back"})
    @PutMapping("/back/{id}")
    public R<Void> rollbackProduct(@PathVariable("id") Long productId) {
        productService.rollbackProduct(productId);
        return R.ofSuccess();
    }


    /**
     * 物资添加审核
     *
     * @param productId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资添加审核失败", operation = "物资资料审核")
    @ApiOperation(value = "物资添加审核", notes = "物资添加审核")
    @RequiresPermissions({"product:publish"})
    @PutMapping("/publish/{id}")
    public R<Void> checkProduct(@PathVariable("id") Long productId) {
        productService.checkProduct(productId);
        return R.ofSuccess();
    }
}
