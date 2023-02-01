package com.example.model.vo.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.swing.plaf.nimbus.State;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 18237
 */
@Getter
@Setter
public class PageQueryParam {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页数")
    @Min(1)
    protected Integer pageNum = 1;
    /**
     * 分页大小
     */
    @ApiModelProperty(value = "分页大小")
    @Min(1)
    @Max(40)
    protected Integer pageSize = 6;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String pageSort;

    /**
     * 排序方式 asc或者desc
     */
    @ApiModelProperty(value = "排序方式, asc或者desc")
    private String order;

    public void setPageSort(String sort) {
        this.pageSort = this.underlineToHump(sort);
    }

    public void setOrder(String order) {
        this.order = this.underlineToHump(order);
    }

    public String underlineToHump(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }
}
