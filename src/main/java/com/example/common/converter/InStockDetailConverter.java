package com.example.common.converter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.utils.LogUtils;
import com.example.dao.business.InStockInfoDao;
import com.example.dao.business.ProductDao;
import com.example.dao.business.SupplierDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.po.business.InStockInfoPo;
import com.example.model.po.business.InStockPo;
import com.example.model.po.business.ProductPo;
import com.example.model.po.business.SupplierPo;
import com.example.model.vo.business.InStockDetailVo;
import com.example.model.vo.business.InStockItemVo;
import com.example.model.vo.business.SupplierVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 18237
 */
@Component
public class InStockDetailConverter {

    @Resource
    private ProductDao productDao;

    @Resource
    private SupplierDao supplierDao;

    @Resource
    private InStockInfoDao inStockInfoDao;

    /**
     * 核对入库明细
     *
     * @param inStock
     * @param pageNum
     * @param pageSize
     * @return
     */
    public InStockDetailVo converterInStockDetail(InStockPo inStock, int pageNum, int pageSize) {
        InStockDetailVo inStockDetailVo = InStockDetailVo.fromInStockPo(inStock);
        //核对匹配入库物资及其对应的物资提供方
        SupplierPo supplier = supplierDao.queryById(inStock.getSupplierId()).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询物资提供方失败, 找不到对应的物资供给, inStockId: {}", inStock.getId());
            return new ApiRuntimeException(Assert.PARAMETER, "找不到对应的物资提供方");
        });
        SupplierVo supplierVo = SupplierVo.fromSupplierPo(supplier);
        inStockDetailVo.setSupplierVo(supplierVo);
        //查询入库物资信息
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<InStockInfoPo> wrapper = new QueryWrapper<InStockInfoPo>()
                .eq(true, "in_num", inStock.getInNum());
        List<InStockInfoPo> inStockInfos = inStockInfoDao.selectList(wrapper);
        //匹配入库物资总数
        inStockDetailVo.setTotal(new PageInfo<>(inStockInfos).getTotal());
        //匹配入库物资及其对应的物品
        if (!CollectionUtils.isEmpty(inStockInfos)) {
            for (InStockInfoPo inStockInfo : inStockInfos) {

                QueryWrapper<ProductPo> productWrapper = new QueryWrapper<ProductPo>()
                        .eq(true, "p_num", inStockInfo.getPNum());
                List<ProductPo> products = productDao.selectList(productWrapper);
                if (!CollectionUtils.isEmpty(products)) {
                    ProductPo product = products.get(0);
                    InStockItemVo inStockItemVo = InStockItemVo.fromProductPo(product);
                    inStockItemVo.setCount(inStockInfo.getProductNumber());
                    inStockDetailVo.getItemVos().add(inStockItemVo);

                } else {
                    throw new ApiRuntimeException(Assert.PARAMETER, "编号为:[" + inStockInfo.getPNum() + "]的物资找不到,或已被删除");
                }
            }
        } else {
            throw new ApiRuntimeException(Assert.PARAMETER, "入库编号为:[" + inStock.getInNum() + "]的明细找不到,或已被删除");
        }
        return inStockDetailVo;
    }
}
