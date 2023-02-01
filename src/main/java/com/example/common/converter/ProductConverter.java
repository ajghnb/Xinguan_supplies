package com.example.common.converter;

import com.example.model.param.business.ProductParam;
import com.example.model.po.business.ProductPo;
import com.example.model.vo.business.ProductVo;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * @author 18237
 */
public class ProductConverter {

    public static ProductPo converterToProductPo(ProductParam param) {
        ProductPo product = new ProductPo();
        BeanUtils.copyProperties(param, product);
        @NotNull(message = "分类不能为空") Long[] categoryKeys = param.getCategoryKeys();
        if (categoryKeys.length == 3) {
            product.setOneCategoryId(categoryKeys[0]);
            product.setTwoCategoryId(categoryKeys[1]);
            product.setThreeCategoryId(categoryKeys[2]);
        }
        return product;
    }

    public static ProductVo converterToProductVo(ProductPo product) {
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(product, productVo);
        return productVo;
    }

}
