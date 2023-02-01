package com.example.common.converter;


import com.example.dao.business.SupplierDao;
import com.example.model.po.business.InStockPo;
import com.example.model.po.business.SupplierPo;
import com.example.model.vo.business.InStockVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 18237
 */
@Component
public class InStockConverter {

    private static SupplierDao supplierDao;

    /**
     * 使用set方法注入,避免注入组件为空
     */
    @Autowired
    public void setSupplierDao(SupplierDao supplierDao) {
        InStockConverter.supplierDao = supplierDao;
    }


    public static InStockVo converterToInStockVo(InStockPo inStock) {
        InStockVo inStockVo = new InStockVo();
        BeanUtils.copyProperties(inStock, inStockVo);
        SupplierPo supplier = supplierDao.selectById(inStock.getSupplierId());
        if (supplier != null) {
            inStockVo.setSupplierName(supplier.getName());
            inStockVo.setPhone(supplier.getPhone());
        }
        return inStockVo;
    }

}
