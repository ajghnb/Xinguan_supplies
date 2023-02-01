package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.ProductCategoryParam;
import com.example.model.po.business.ProductCategoryPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author 18237
 */
@Repository
public interface ProductCategoryDao extends BaseMapper<ProductCategoryPo> {

    /**
     * 物资分类列表
     *
     * @param pageParam
     * @return
     */
    Page<ProductCategoryPo> queryProductCategoryList(ProductCategoryParam pageParam);

    /**
     * 查询物资分类
     *
     * @param productCategoryId
     * @return
     */
    @Select("select * from xinguan.biz_product_category where id = #{id}")
    Optional<ProductCategoryPo> queryById(@Param("id") Long productCategoryId);

}
