package com.example.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.converter.ProductCategoryBuilder;
import com.example.common.converter.ProductCategoryConverter;
import com.example.common.utils.ListPageUtils;
import com.example.common.utils.LogUtils;
import com.example.dao.business.ProductCategoryDao;
import com.example.dao.business.ProductDao;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import com.example.model.PageVo;
import com.example.model.param.business.ProductCategoryParam;
import com.example.model.po.business.ProductCategoryPo;
import com.example.model.po.business.ProductCategoryTreeNodePo;
import com.example.model.vo.base.PageQueryParam;
import com.example.service.business.ProductCategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 18237
 */
@Service("ProductCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {


    @Resource
    private ProductDao productDao;

    @Resource
    private ProductCategoryDao productCategoryDao;

    /**
     * 商品类别列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public Page<ProductCategoryPo> queryProductCategoryList(ProductCategoryParam pageParam) {
        LogUtils.LOGGER.debug("物资分类列表: 分页参数:{}", pageParam);

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        return productCategoryDao.queryProductCategoryList(pageParam);
    }

    /**
     * 所有商品类别
     *
     * @return
     */
    @Override
    public List<ProductCategoryPo> findAll() {
        LogUtils.LOGGER.debug("查询所有物资分类");

        return productCategoryDao.selectList(null);
    }


    /**
     * 添加商品类别
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProductCategory(ProductCategoryParam param) {
        LogUtils.LOGGER.debug("新增物资分类: 物资类别:{}", param);

        ProductCategoryPo productCategory = ProductCategoryPo.fromProductCategoryParam(param);

        Assert.DB_OPERATE.sqlSuccess(productCategoryDao.insert(productCategory))
                .orThrow(isAssert -> {
                    LogUtils.LOGGER.error("新增物资分类失败: {}", productCategory);
                    return new ApiRuntimeException(isAssert);
                });
    }

    /**
     * 编辑商品类别
     *
     * @param productCategoryId
     * @return
     */
    @Override
    public ProductCategoryPo editById(Long productCategoryId) {
        LogUtils.LOGGER.debug("编辑物资类别: productCategoryId:{}", productCategoryId);

        ProductCategoryPo productCategory = productCategoryDao.queryById(productCategoryId).orElseThrow(() -> {
            LogUtils.LOGGER.error("查询物资分类失败, 找不到对应的类别, productCategoryId: {}", productCategoryId);
            return new ApiRuntimeException(Assert.PARAMETER, "找不到对应的类别");
        });
        return productCategory;
    }

    /**
     * 更新商品类别
     *
     * @param productCategoryId
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProductCategory(Long productCategoryId, ProductCategoryParam param) {
        LogUtils.LOGGER.debug("开始更新物资类别信息, productCategoryId: {}, productCategory: {}", productCategoryId, param);

        //检查物资类别是否存在
        checkProductCategoryIsExit(productCategoryId);
        //参数类型装换
        ProductCategoryPo productCategory = ProductCategoryPo.fromProductCategoryParam(param);
        //更新供应商
        LambdaUpdateWrapper<ProductCategoryPo> wrapper = new LambdaUpdateWrapper<ProductCategoryPo>()
                .eq(true, ProductCategoryPo::getId, productCategoryId);
        Assert.DB_OPERATE.sqlSuccess(productCategoryDao.update(productCategory, wrapper)).orThrow(iAssert -> {
            LogUtils.LOGGER.error("更新物资类别失败, productCategoryId: {}, productCategory: {}", productCategoryId, productCategory);
            return new ApiRuntimeException(iAssert);
        });

    }

    /**
     * 删除商品类别
     *
     * @param productCategoryId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long productCategoryId) {
        LogUtils.LOGGER.debug("开始删除物资类别, productCategoryId: {}", productCategoryId);

        //检查物资类别能否进行删除
        checkIsAllowDeleteProductCategory(productCategoryId);

        Assert.DB_OPERATE.sqlSuccess(productCategoryDao.deleteById(productCategoryId)).orThrow(isAssert -> {
            LogUtils.LOGGER.debug("[{}]  删除物资类别失败, productCategoryId: {}", productCategoryId);
            return new ApiRuntimeException(isAssert);
        });

    }

    /**
     * 分类树形结构
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageVo<ProductCategoryTreeNodePo> queryCategoryTree(PageQueryParam pageParam) {
        LogUtils.LOGGER.debug("开始查询物资分类树形, 分页参数: {}", pageParam);

        List<ProductCategoryPo> productCategories = findAll();
        List<ProductCategoryTreeNodePo> treeNodes = ProductCategoryBuilder.build(productCategories);
        Integer pageNum = pageParam.getPageNum();
        Integer pageSize = pageParam.getPageSize();
        if (pageSize != null && pageNum != null) {
            List<ProductCategoryTreeNodePo> treeNodePage = ListPageUtils.getPage(treeNodes, pageSize, pageNum);
            return new PageVo<>(treeNodes.size(), pageNum, pageSize, treeNodePage);
        } else {
            return new PageVo<>(treeNodes.size(), 1, treeNodes.size(), treeNodes);
        }
    }

    /**
     * 获取父级分类（2级树）
     *
     * @return
     */
    @Override
    public List<ProductCategoryTreeNodePo> getParentCategoryTree() {

        LogUtils.LOGGER.debug("开始查询物资父级分类树");

        List<ProductCategoryPo> productCategories = findAll();
        List<ProductCategoryTreeNodePo> productCategoryTreeNodes = productCategories.stream()
                .map(ProductCategoryConverter::converterToTreeNodePo)
                .collect(Collectors.toList());

        return ProductCategoryBuilder.buildParent(productCategoryTreeNodes);
    }


    /**
     * 检查物资类别是否存在
     *
     * @param productCategoryId
     * @return
     */
    public void checkProductCategoryIsExit(Long productCategoryId) {
        ProductCategoryPo productCategory = productCategoryDao.selectById(productCategoryId);

        if (productCategory == null) {
            LogUtils.LOGGER.error("核对物资分类失败, 找不对应的类别, productCategoryId: {}", productCategoryId);
            throw new ApiRuntimeException(Assert.PARAMETER, "物资类别不存在");
        }
    }

    /**
     * 检查物资类别是否能进行操作(若不符合条件则抛出异常,反之亦然)
     *
     * @param productCategoryId
     * @return
     */
    public void checkIsAllowDeleteProductCategory(Long productCategoryId) {

        //1.检查该物资类别是否存在
        checkProductCategoryIsExit(productCategoryId);

        //2.检查该物资分类是否存在子分类
        QueryWrapper<ProductCategoryPo> wrapper = new QueryWrapper<ProductCategoryPo>()
                .eq(true, "pid", productCategoryId);
        Integer subClassCount = Math.toIntExact(productCategoryDao.selectCount(wrapper));
        if (subClassCount != 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, "存在子节点,无法直接删除");
        }

        //3.检查该分类是否有物资引用
        int parentCount = productDao.queryProductByIdIsExitQuote(productCategoryId);
        if (parentCount != 0) {
            throw new ApiRuntimeException(Assert.PARAMETER, "该分类存在物资引用,无法直接删除");
        }
    }

}
