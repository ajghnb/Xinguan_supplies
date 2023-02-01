package com.example.controller.business;

import com.example.annotation.ControllerEndpoint;
import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.PageData;
import com.example.model.PageVo;
import com.example.model.R;
import com.example.model.param.business.ProductCategoryParam;
import com.example.model.po.business.ProductCategoryPo;
import com.example.model.po.business.ProductCategoryTreeNodePo;
import com.example.model.vo.base.PageQueryParam;
import com.example.model.vo.business.ProductCategoryTreeNodeVo;
import com.example.model.vo.business.ProductCategoryVo;
import com.example.service.business.ProductCategoryService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Api(tags = "业务模块-物资类别相关接口")
@RestController
@RequestMapping("/business/productCategory")
public class ProductCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    /**
     * 物资分类列表
     *
     * @param productCategory
     * @return
     */
    @ApiOperation(value = "分类列表", notes = "物资分类列表,根据物资分类名模糊查询")
    @GetMapping("/page")
    public R<PageData<ProductCategoryVo>> productCategoryPage(ProductCategoryParam productCategory) {
        Page<ProductCategoryPo> productCategories = productCategoryService.queryProductCategoryList(productCategory);
        return R.ofSuccess(new PageData<>(productCategories)
                .convert(ProductCategoryVo::fromProductCategoryPo));

    }

    /**
     * 查询所有分类
     *
     * @param
     * @return
     */
    @ApiOperation(value = "所有分类")
    @GetMapping("/all")
    public R<List<ProductCategoryVo>> findAll() {
        List<ProductCategoryPo> productCategories = productCategoryService.findAll();
        List<ProductCategoryVo> productCategoryVos = productCategories.stream()
                .map(ProductCategoryVo::fromProductCategoryPo)
                .collect(Collectors.toList());
        return R.ofSuccess(productCategoryVos);
    }


    /**
     * 添加物资分类
     *
     * @param productCategory
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资分类添加失败", operation = "物资分类添加")
    @RequiresPermissions({"productCategory:add"})
    @ApiOperation(value = "添加分类")
    @PostMapping("/add")
    public R<Void> addProductCategory(@RequestBody @Validated({Add.class, Default.class}) ProductCategoryParam productCategory) {
        productCategoryService.addProductCategory(productCategory);
        return R.ofSuccess();
    }

    /**
     * 编辑物资分类
     *
     * @param productCategoryId
     * @return
     */
    @ApiOperation(value = "编辑分类")
    @RequiresPermissions({"productCategory:edit"})
    @GetMapping("/edit/{id}")
    public R<ProductCategoryVo> editProductCategory(@PathVariable("id") Long productCategoryId) {
        ProductCategoryPo productCategory = productCategoryService.editById(productCategoryId);
        ProductCategoryVo productCategoryVo = ProductCategoryVo.fromProductCategoryPo(productCategory);
        return R.ofSuccess(productCategoryVo);
    }

    /**
     * 更新物资分类
     *
     * @param productCategoryId
     * @param productCategory
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资分类更新失败", operation = "物资分类更新")
    @ApiOperation(value = "更新分类")
    @RequiresPermissions({"productCategory:update"})
    @PutMapping("/update/{id}")
    public R<Void> updateProductCategory(@PathVariable("id") Long productCategoryId, @RequestBody @Validated({Edit.class, Default.class}) ProductCategoryParam productCategory) {
        productCategoryService.updateProductCategory(productCategoryId, productCategory);
        return R.ofSuccess();
    }

    /**
     * 删除物资分类
     *
     * @param productCategoryId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "物资分类删除失败", operation = "物资分类删除")
    @ApiOperation(value = "删除分类")
    @RequiresPermissions({"productCategory:delete"})
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteProductCategory(@PathVariable("id") Long productCategoryId) {
        productCategoryService.deleteById(productCategoryId);
        return R.ofSuccess();
    }

    /**
     * 分类树形结构(分页)
     *
     * @param page
     * @return
     */
    @ApiOperation(value = "分类树形结构")
    @GetMapping("/getTree")
    public R<PageData<ProductCategoryTreeNodeVo>> getCategoryTree(PageQueryParam page) {
        PageVo<ProductCategoryTreeNodePo> productCategoryTreeNodes = productCategoryService.queryCategoryTree(page);

        return R.ofSuccess(new PageData<>(productCategoryTreeNodes)
                .convert(ProductCategoryTreeNodeVo::fromProductCategoryNodePo));
    }


    /**
     * 获取父级分类树：2级树
     *
     * @param
     * @return
     */
    @ApiOperation(value = "父级分类树")
    @GetMapping("/getParentTree")
    public R<List<ProductCategoryTreeNodeVo>> getParentCategoryTree() {
        List<ProductCategoryTreeNodePo> parentTreeNodes = productCategoryService.getParentCategoryTree();
        List<ProductCategoryTreeNodeVo> nodeVos = parentTreeNodes.stream()
                .map(ProductCategoryTreeNodeVo::fromProductCategoryNodePo)
                .collect(Collectors.toList());

        return R.ofSuccess(nodeVos);
    }


}
