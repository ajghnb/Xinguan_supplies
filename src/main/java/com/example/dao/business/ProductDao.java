package com.example.dao.business;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.ProductParam;
import com.example.model.po.business.ProductPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @author 18237
 */
@Repository
public interface ProductDao extends BaseMapper<ProductPo> {

    /**
     * 查询物资列表
     *
     * @param pageParam
     * @return 所有物资信息
     */
    Page<ProductPo> queryProductList(ProductParam pageParam);

    /**
     * 编辑指定物资
     *
     * @param productId
     * @return 物资信息
     */
    @Select("select * from biz_product where id = #{id}")
    Optional<ProductPo> editById(Long productId);

    /**
     * 查询指定物资
     *
     * @param pNum
     * @return 物资
     */
    @Select("select * from biz_product where p_num = #{pNum}")
    List<ProductPo> queryByPNum(String pNum);


    /**
     * 查询物资分类存在引用数量
     *
     * @param productCategoryId
     * @return
     */
    @Select("select count(*) from xinguan.biz_product where one_category_id = #{id} or two_category_id = #{id} or three_category_id = #{id}")
    int queryProductByIdIsExitQuote(@Param("id") Long productCategoryId);




}
