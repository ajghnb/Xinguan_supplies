package com.example.enums;

/**
 * 用户状态
 *
 * @author 18237
 */

public enum UserStatusEnum {

    DISABLE(0),
    AVAILABLE(1);

    private int statusCode;

    UserStatusEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}