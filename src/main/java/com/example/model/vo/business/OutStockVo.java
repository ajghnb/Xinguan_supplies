package com.example.model.vo.business;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 18237
 */
@Data
public class OutStockVo {

    private Long id;

    private Integer type;

    private String outNum;

    private String operator;

    private Integer priority;

    private Integer productNumber;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 发放的物资列表
     **/
    private List<Object> products = new ArrayList<>();

    private String remark;

    /**
     * 发放单的状态
     **/
    private Integer status;


    /**
     * 如果consumerId不为空
     **/

    private Long consumerId;

    /**
     * 去向名
     **/
    private String name;

    /**
     * 地址
     **/
    private String address;

    /**
     * 联系电话
     **/
    private String phone;

    /**
     * 联系人
     **/
    private String contact;

    /**
     * 排序
     **/
    private Integer sort;


}
