package com.example.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.param.business.ProductParam;
import com.example.model.dto.ProductStock;
import com.example.model.po.business.ProductStockPo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author 18237
 */
@Repository
public interface ProductStockDao extends BaseMapper<ProductStockPo> {

    /**
     * 查询物资库存列表
     *
     * @param pageParam
     * @return 所有物资库存信息
     */
    Page<ProductStock> queryProductStockList(ProductParam pageParam);


    /**
     * 库存信息(饼图使用)
     *
     * @param productParam
     * @return 物资库存信息
     */
    List<ProductStock> findAllStocks(ProductParam productParam);

    /**
     * 库存
     *
     * @param pNum
     * @return 物资库
     */
    @Select("select * from biz_product_stock where p_num = #{pNum}")
    List<ProductStockPo> queryListByPNum(String pNum);


    /**
     * 库存
     *
     * @param pNum
     * @return 物资库
     */
    @Select("select * from biz_product_stock where p_num = #{pNum}")
    ProductStockPo queryByPNum(String pNum);



}
