package com.example.model.param.business;

import com.example.annotation.valid.Add;
import com.example.model.vo.base.PageQueryParam;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 18237
 */
@Data
@Alias("OutStockParam")
public class OutStockParam extends PageQueryParam {

    private Long id;

    @NotNull(groups = {Add.class, Default.class}, message = "出库类型不能为空")
    private Integer type;

    private String outNum;

    private String operator;

    @NotNull(groups = {Add.class, Default.class}, message = "出库紧急程度不能为空")
    private Integer priority;

    private Integer productNumber;

    /**
     * 发放的物资列表
     **/
    @NotEmpty(groups = {Add.class, Default.class}, message = "出库单物资不能为空不能为空")
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


    public ConsumerParam checkOutStockConsumer() {

        ConsumerParam consumer = ConsumerParam.builder()
                .id(this.getConsumerId())
                .name(this.getName())
                .address(this.getAddress())
                .phone(this.getPhone())
                .sort(this.getSort())
                .contact(this.getContact())
                .build();
        return consumer;

    }


}
