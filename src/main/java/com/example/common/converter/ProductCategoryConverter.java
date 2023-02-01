package com.example.common.converter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.model.po.business.ProductCategoryPo;
import com.example.model.po.business.ProductCategoryTreeNodePo;
import com.example.model.vo.base.PageQueryParam;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;

import java.util.List;


/**
 * @author 18237
 */
public class ProductCategoryConverter {

    public static ProductCategoryTreeNodePo converterToTreeNodePo(ProductCategoryPo productCategory) {
        ProductCategoryTreeNodePo productCategoryTreeNode = new ProductCategoryTreeNodePo();
        BeanUtils.copyProperties(productCategory, productCategoryTreeNode);
        return productCategoryTreeNode;
    }
}
