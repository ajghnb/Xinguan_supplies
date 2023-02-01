package com.example.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.system.DepartmentParam;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 18237
 */

@Data
@Builder
@Alias("Department")
@Excel("department")
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_department")
public class DepartmentPo {
    @Id
    @ExcelField(value = "编号", width = 50)
    private Long id;

    @ExcelField(value = "部门名称", width = 100)
    private String name;

    @ExcelField(value = "联系电话", width = 120)
    private String phone;

    @ExcelField(value = "部门地址", width = 150)
    private String address;

    @ExcelField(value = "创建时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date createTime;

    @ExcelField(value = "修改时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date modifiedTime;

    public static DepartmentPo fromDepartmentParam(DepartmentParam param){
        DepartmentPo department = DepartmentPo.builder()
                .id(param.getId())
                .name(param.getName())
                .phone(param.getPhone())
                .address(param.getAddress())
                .build();
        return department;
    }
}
