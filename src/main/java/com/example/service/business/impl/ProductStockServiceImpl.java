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


    @Override
    public Page<ProductStock> queryProductStockList(ProductParam pageParam) {
        LogUtils.LOGGER.debug("物资列表: 分页大小:{}", pageParam);

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        return productStockDao.queryProductStockList(pageParam);
    }

    @Override
    public List<ProductStock> findAllStocks(ProductParam productParam) {
        LogUtils.LOGGER.debug("所有物资: 分页大小:{}", productParam);

        PageHelper.startPage(productParam.getPageNum(), productParam.getPageSize());
        return productStockDao.findAllStocks(productParam);
    }
}
