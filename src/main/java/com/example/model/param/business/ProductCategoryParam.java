package com.example.model.param.business;

import com.example.annotation.valid.Add;
import com.example.annotation.valid.Edit;
import com.example.model.vo.base.PageQueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Alias("ProductCategoryParam")
public class ProductCategoryParam extends PageQueryParam {

    private Long id;

    @Length(max = 32, groups = {Add.class, Edit.class}, message = "名字最长为32个字符")
    @NotBlank(groups = {Add.class, Edit.class }, message = "类目名称不能为空")
    private String name;

    @Length(max = 64, groups = {Add.class, Edit.class}, message = "类目备注最多为64个字符")
    @NotBlank(groups = {Add.class, Edit.class}, message = "类目备注不能为空")
    private String remark;


    @NotNull(groups = {Add.class, Edit.class}, message = "排序号不能为空")
    private Integer sort;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date modifiedTime;

    /**
     * 父级分类id
     */
    @NotNull(groups = {Add.class, Edit.class}, message = "父级菜单不能为空")
    private Long pid;

}
