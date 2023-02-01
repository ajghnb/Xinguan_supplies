package com.example.service.business;

import com.example.model.PageVo;
import com.example.model.param.business.ProductCategoryParam;
import com.example.model.po.business.ProductCategoryPo;
import com.example.model.po.business.ProductCategoryTreeNodePo;
import com.example.model.vo.base.PageQueryParam;
import com.github.pagehelper.Page;

import java.util.List;


/**
 * @author 18237
 */
public interface ProductCategoryService {

    /**
     * 物资分类列表
     *
     * @param pageParam
     * @return
     */
    Page<ProductCategoryPo> queryProductCategoryList(ProductCategoryParam pageParam);


    /**
     * 查询所物资类别
     *
     * @return
     */
    List<ProductCategoryPo> findAll();


    /**
     * 添加物资类别
     *
     * @param param
     * @return
     */
    void addProductCategory(ProductCategoryParam param);


    /**
     * 编辑物资类别
     *
     * @param productCategoryId
     * @return
     */
    ProductCategoryPo editById(Long productCategoryId);

    /**
     * 更新物资类别
     *
     * @param productCategoryId
     * @param param
     * @return
     */
    void updateProductCategory(Long productCategoryId, ProductCategoryParam param);

    /**
     * 删除物资类别
     *
     * @param productCategoryId
     * @return
     */
    void deleteById(Long productCategoryId);


    /**
     * 分类树形
     *
     * @param pageParam
     * @return
     */
    PageVo<ProductCategoryTreeNodePo> queryCategoryTree(PageQueryParam pageParam);


    /**
     * 获取父级分类（2级树）
     *
     * @return
     */
    List<ProductCategoryTreeNodePo> getParentCategoryTree();


}
