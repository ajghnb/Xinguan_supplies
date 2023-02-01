package com.example.model.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentVo {

    private Long id;

    /**
     * 部门内人数
     **/
    private int total;

    private String name;

    private String phone;

    private String address;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date modifiedTime;


}
