package com.example.model.po.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.param.system.MenuParam;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author 18237
 */
@Data
@Builder
@Alias("Menu")
@Excel(value = "菜单表格")
@TableName("tb_menu")
public class MenuPo {

    @Id
    @ExcelField(value = "编号", width = 50)
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @ExcelField(value = "父级id", width = 50)
    private Long parentId;

    @ExcelField(value = "菜单名称", width = 100)
    private String menuName;

    @ExcelField(value = "菜单url", width = 100)
    private String url;

    @ExcelField(value = "菜单图标", width = 80)
    private String icon;

    @ExcelField(value = "是否展开", width = 50)
    private Integer open;

    @ExcelField(value = "菜单类型", width = 80)
    private Integer type;

    @ExcelField(value = "排序", width = 90)
    private Long orderNum;

    @ExcelField(value = "创建时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date createTime;

    @ExcelField(value = "修改时间", dateFormat = "yyyy年MM月dd日 HH:mm:ss", width = 180)
    private Date modifiedTime;

    @ExcelField(value = "是否可用", width = 80)
    private Integer available;

    @ExcelField(value = "权限编码", width = 180)
    private String perms;

    public static MenuPo fromMenuParam(MenuParam param) {

        MenuPo menu = MenuPo.builder()
                .id(param.getId())
                .parentId(param.getParentId())
                .menuName(param.getMenuName())
                .url(param.getUrl())
                .icon(param.getIcon())
                .type(param.getType())
                .orderNum(param.getOrderNum())
                .open(param.getOpen())
                .perms(param.getPerms())
                .build();
        return menu;
    }
}