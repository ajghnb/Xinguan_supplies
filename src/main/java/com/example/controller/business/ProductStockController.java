package com.example.controller.business;



import com.example.annotation.valid.Page;
import com.example.model.PageData;
import com.example.model.R;
import com.example.model.param.business.ProductParam;
import com.example.model.dto.ProductStock;
import com.example.model.vo.business.ProductStockVo;
import com.example.service.business.ProductStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 18237
 */
@RestController
@Api(tags = "业务模块-物资库存相关接口")
@RequestMapping("/business/stock")
public class ProductStockController {

    @Autowired
    private ProductStockService productStockService;

    /**
     * 库存列表
     *
     * @param product
     * @return
     */
    @ApiOperation(value = "库存列表", notes = "物资列表,根据物资名模糊查询")
    @GetMapping("/page")
    public R<PageData<ProductStockVo>> productStockPage(@Validated({Default.class, Page.class})
                                                                ProductParam product) {

        product.buildCategorySearch(product.getCategorys());
        List<ProductStock> productStocks = productStockService.queryProductStockList(product);

        return R.ofSuccess(new PageData<>(productStocks).convert(ProductStockVo::fromProductStockVo));
    }


    /**
     * 所有库存(饼图使用)
     *
     * @param product
     * @return
     */
    @ApiOperation(value = "全部库存", notes = "物资所有库存信息,饼图使用")
    @GetMapping("/all")
    public R<List<ProductStockVo>> productStockList(@Validated({Default.class, Page.class})
                                                            ProductParam product) {

        product.buildCategorySearch(product.getCategorys());
        List<ProductStockVo> productStocks = productStockService.findAllStocks(product)
                .stream()
                .map(ProductStockVo::fromProductStockVo)
                .collect(Collectors.toList());

        return R.ofSuccess(productStocks);
    }


}
