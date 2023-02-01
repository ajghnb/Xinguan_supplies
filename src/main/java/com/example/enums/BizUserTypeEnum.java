package com.example.enums;

/**
 * 业务用户类型
 * @author 18237
 */
public enum BizUserTypeEnum {

    DEAN("部门主任");

    private String val;

    BizUserTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}