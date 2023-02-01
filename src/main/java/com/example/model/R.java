package com.example.model;

import com.example.enums.ServerEnum;
import com.example.exception.asserts.IAssert;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class R<T> {
    @ApiModelProperty(value = "错误码")
    private final int code;

    @ApiModelProperty(value = "详细信息")
    private final String msg;

    @ApiModelProperty(value = "返回数据")
    private final T data;

    public static <T, E extends IAssert> R<T> of(E serverEnum) {
        return of(serverEnum, null);
    }

    public static <T, E extends IAssert> R<T> of(E serverEnum, T data) {
        return new R<>(serverEnum.getCode(), serverEnum.getMessage(), data);
    }

    public static <T> R<T> ofCustom(int code, String msg) {
        return ofCustom(code, msg, null);
    }

    public static <T> R<T> ofCustom(int code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    public static <T> R<T> ofSuccess(T data) {
        return of(ServerEnum.SUCCESS, data);
    }

    public static <T> R<T> ofSuccess() {
        return ofSuccess(null);
    }

    public static <T> R<T> ofFail(String msg) {
        return ofCustom(ServerEnum.ERROR.code, msg);
    }

    public static <T> R<T> ofFail() {
        return of(ServerEnum.ERROR, null);
    }

    @Deprecated
    public static <T> R<T> ofData(ServerEnum serverEnum, T data) {
        return new R<>(serverEnum.getCode(), serverEnum.getMessage(), data);
    }

    /**
     * 分页返回对象(自动分页)
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<PageData<T>> withPage(List<T> data) {
        return R.ofData(ServerEnum.SUCCESS, new PageData<>(data));
    }

    private R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private <E extends IAssert> R(E iAssert, T data) {
        this(iAssert.getCode(), iAssert.getMessage(), data);
    }

    /**
     * 仅返回状态码
     */
    @Deprecated
    public static <T> R<T> ofStateOnly(ServerEnum serverEnum) {
        return new R<>(serverEnum, null);
    }

    /**
     * 判断sql影响条数与预计的条数是否相同
     *
     * @param actualNum   实际条数
     * @param exceptedNum 预计条数
     */
    @Deprecated
    public static <T> R<T> ofSqlExpected(int actualNum, int exceptedNum) {

        return ofSqlExpected(actualNum, exceptedNum, null);
    }

    @Deprecated
    public static <T> R<T> ofSqlExpected(int actualNum, int exceptedNum, T data) {
        if (actualNum == exceptedNum) {
            return new R<>(ServerEnum.SUCCESS, data);
        }

        return new R<>(ServerEnum.DELETE_FAIL, data);
    }

    /**
     * 判断sql影响条数是否为1
     */
    @Deprecated
    public static <T> R<T> ofSqlExpected(int actualNum) {
        return ofSqlExpected(actualNum, 1);
    }

    /**
     * 通过boolean返回结果
     *
     * @param result
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> R<T> ofBoolean(boolean result, T data) {
        if (result) {
            return R.ofSuccess(data);
        } else {
            return R.of(ServerEnum.ERROR, data);
        }
    }

    @Deprecated
    public static <T> R<T> ofBoolean(boolean result) {
        return ofBoolean(result, null);
    }
}
