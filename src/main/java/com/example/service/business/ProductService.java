package com.example.service.business;


import com.example.model.param.business.ProductParam;
import com.example.model.po.business.ProductPo;
import com.github.pagehelper.Page;

/**
 * @author 18237
 */

public interface ProductService {

    /**
     * 查询物资列表
     *
     * @param pageParam
     * @return 所有物资信息
     */
    Page<ProductPo> queryProductList(ProductParam pageParam);

    /**
     * 新增物资
     *
     * @param productParam
     * @return
     */
    void addProduct(ProductParam productParam);

    /**
     * 编辑指定物资
     *
     * @param productId
     * @return
     */
    ProductPo editById(Long productId);


    /**
     * 删除指定物资
     *
     * @param productId
     * @return
     */
    void deleteById(Long productId);


    /**
     * 更新指定物资
     *
     * @param productId
     * @param param
     * @return
     */
    void updateProduct(Long productId, ProductParam param);


    /**
     * 移入回收站
     *
     * @param productId
     */
    void removeProduct(Long productId);

    /**
     * 从回收站恢复数据
     *
     * @param productId
     */
    void rollbackProduct(Long productId);


    /**
     * 物资审核
     *
     * @param productId
     */
    void checkProduct(Long productId);


}
