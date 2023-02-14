package com.example.service.business.impl;

import com.example.common.utils.LogUtils;
import com.example.dao.business.ProductStockDao;
import com.example.model.param.business.ProductParam;
import com.example.model.dto.ProductStock;
import com.example.service.business.ProductStockService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author 18237
 */
@Service("productStockService")
public class ProductStockServiceImpl implements ProductStockService {

    @Autowired
    private ProductStockDao productStockDao;


    /**
     * 物资库存列表
     *
     * @param param
     * @return
     */
    @Override
    public Page<ProductStock> queryProductStockList(ProductParam param) {
        LogUtils.LOGGER.debug("物资列表: 分页大小:{}", param);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return productStockDao.queryProductStockList(param);
    }


    /**
     * 所有库存信息
     *
     * @param param
     * @return
     */
    @Override
    public List<ProductStock> findAllStocks(ProductParam param) {
        LogUtils.LOGGER.debug("所有物资: 分页大小:{}", param);

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return productStockDao.findAllStocks(param);
    }
}
