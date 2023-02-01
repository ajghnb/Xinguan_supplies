package com.example.model.param.business;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.vo.base.PageQueryParam;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

/**
 * @author 18237
 */
@Data
@Alias("ProductParam")
public class ProductParam extends PageQueryParam {

    private Long id;

    private String pNum;

    @Length(max = 32, groups = {Default.class}, message = "名字最长为32个字符")
    @NotBlank(groups = {Add.class}, message = "物资产品名字不能为空")
    private String name;

    @NotBlank(groups = {Add.class}, message = "物资产品规格不能为空")
    private String model;

    @NotBlank(groups = {Add.class}, message = "物资产品存储方式不能为空")
    private String unit;

    @Length(max = 64, groups = {Default.class}, message = "备注最长为32个字符")
    @NotBlank(groups = {Add.class}, message = "物资产品用途不能为空")
    private String remark;

    private Integer sort;

    private String imageUrl;

    private String categorys;

    @Size(min = 3, max = 3, groups = {Add.class,Edit.class}, message = "物资需要三级分类")
    @NotNull(groups = {Add.class, Edit.class}, message = "分类不能为空")
    private Long[] categoryKeys;

    private Long oneCategoryId;

    private Long twoCategoryId;

    private Long threeCategoryId;

    /**
     * 是否已经进入回收站:1:逻辑删除,0:正常数据,2:添加待审核
     */
    private Integer status;

    /**
     * 封装物资查询条件
     *
     * @param categorys
     */
    public void buildCategorySearch(@RequestParam(value = "categorys", required = false) String categorys) {
        if (categorys != null && !"".equals(categorys)) {
            String[] split = categorys.split(",");
            switch (split.length) {
                case 1:
                    this.setOneCategoryId(Long.parseLong(split[0]));
                    break;
                case 2:
                    this.setOneCategoryId(Long.parseLong(split[0]));
                    this.setTwoCategoryId(Long.parseLong(split[1]));
                    break;
                case 3:
                    this.setOneCategoryId(Long.parseLong(split[0]));
                    this.setTwoCategoryId(Long.parseLong(split[1]));
                    this.setThreeCategoryId(Long.parseLong(split[2]));
                    break;
                default:
                    break;
            }
        }
    }

}
