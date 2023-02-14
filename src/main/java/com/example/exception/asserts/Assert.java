package com.example.exception.asserts;

import com.example.enums.ServerEnum;
import com.example.exception.ApiException;


/**
 * @author 18237
 */

public enum Assert implements IAssert {
    // Api断言
    ERROR(ServerEnum.ERROR),
    DB_OPERATE(ServerEnum.DB_OPERATE_ERROR),
    PARAMETER(ServerEnum.PARAMETER_ERROR),
    ACCESS(ServerEnum.ACCESS_DENIED),
    LOGIN(ServerEnum.LOGIN_FAIL),
    IS_LOGIN(ServerEnum.NOT_LOGIN),
    INVALID_LOGIN(ServerEnum.INVALID_LOGIN),
    RE_LOGIN(ServerEnum.RE_LOGIN),
    UNKNOWN_USER(ServerEnum.UNKNOWN_USER),
    IS_EXIST(ServerEnum.NOT_EXIST),
    IS_NOT_EXIST(ServerEnum.ALREADY_EXIST),
    CREATE_FILE(ServerEnum.CREATE_FAIL),
    RENAME_FILE(ServerEnum.RENAME_FAIL),
    DELETE_FILE(ServerEnum.DELETE_FAIL),
    NETWORK(ServerEnum.NETWORK_ERROR),
    PRODUCT_STOCk(ServerEnum.PRODUCT_STOCK_ERROR),
    PRODUCT_STATUS(ServerEnum.PRODUCT_STATUS_ERROR),
    INSTOCK_STATUS(ServerEnum.INSTOCK_STATUS_ERROR),
    PRODUCT_REMOVE(ServerEnum.PRODUCT_IS_REMOVE),
    PRODUCT_NOT_FOUND(ServerEnum.PRODUCT_NOT_FOUND),
    PRODUCT_WAIT(ServerEnum.PRODUCT_WAIT_PASS),
    PRODUCT_INSTOCK_NUMBER(ServerEnum.PRODUCT_INSTOCK_NUMBER_ERROR),
    PRODUCT_OUTSTOCK_EMPTY(ServerEnum.PRODUCT_OUTSTOCK_EMPTY),
    TOKEN(ServerEnum.TOKEN_ERROR),
    USER_COUNT(ServerEnum.USER_COUNT_ERROR),
    PASSWORD_ERROR(ServerEnum.USER_PASSWORD_ERROR),
    VERIFYCODE_ERROR(ServerEnum.VERIFYCODE_ERROR),
    OPERATE_FREQUENCY(ServerEnum.OPERATE_FREQUENCY);


    public final int code;
    public final String message;

    Assert(ServerEnum enums) {
        this.code = enums.getCode();
        this.message = enums.getMessage();
    }

    public static <T> AssertWrapper<IAssert, T> nonNull(T object) {
        return new AssertWrapper<>(object == null, ERROR, object);
    }

    public static AssertWrapper<IAssert, Void> sqlExecSuccess(int count) {
        return new AssertWrapper<>(count != 1, ERROR, null);
    }

    public static AssertWrapper<IAssert, Void> sqlExecSuccess(int count, int expect) {
        return new AssertWrapper<>(count != expect, ERROR, null);
    }

    public static void sqlExecSuccess(int count, String msg, String detail) throws ApiException {
        if (count < 1) {
            throw new ApiException(DB_OPERATE.code, msg, detail, null);
        }
    }

    public static void sqlExecSuccess(int count, int excepted, String msg, String detail) throws ApiException {
        if (count != excepted) {
            throw new ApiException(DB_OPERATE.code, msg, detail, null);
        }
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
