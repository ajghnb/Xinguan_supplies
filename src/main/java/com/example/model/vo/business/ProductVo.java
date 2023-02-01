package com.example.model.vo.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author 18237
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVo {

    private Long id;

    private String pNum;

    private String name;

    private String model;

    private String unit;

    private String remark;

    private Integer sort;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date modifiedTime;

    private String imageUrl;

    private Long[] categoryKeys;

    private Long oneCategoryId;

    private Long twoCategoryId;

    private Long threeCategoryId;

    /**
     * 是否已经进入回收站:1:逻辑删除,0:正常数据,2:添加待审核
     */
    private Integer status;

}