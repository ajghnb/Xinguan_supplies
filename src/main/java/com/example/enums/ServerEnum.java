package com.example.enums;

import com.example.consts.GeneralConst;
import com.example.exception.asserts.IAssert;
import lombok.Getter;


/**
 * @author 18237
 */
@Getter
public enum ServerEnum implements IAssert {
    /**
     * 通用枚举
     */
    SUCCESS(GeneralConst.SUCCESS, "成功"),
    ERROR(GeneralConst.ERROR, "服务器异常"),
    DB_OPERATE_ERROR(GeneralConst.DB_OPERATE_ERROR, "数据库操作异常"),
    PARAMETER_ERROR(GeneralConst.PARAMETER_ERROR, "请求参数错误"),
    ACCESS_DENIED(GeneralConst.ACCESS_DENIED, "权限不足"),
    NOT_LOGIN(GeneralConst.NOT_LOGIN, "未登录"),
    LOGIN_FAIL(GeneralConst.NOT_LOGIN, "登录失败"),
    INVALID_LOGIN(GeneralConst.NOT_LOGIN, "登录过期"),
    RE_LOGIN(GeneralConst.LOGIN_EXPIRED, "登录信息不完整"),
    UNKNOWN_USER(GeneralConst.USER_UNKNOWN, "用户信息不完整"),
    NOT_EXIST(GeneralConst.NOT_EXIST, "对象不存在"),
    ALREADY_EXIST(GeneralConst.ALREADY_EXIST, "对象已存在"),
    CREATE_FAIL(GeneralConst.CREATE_FILE_FAIL, "创建文件失败"),
    RENAME_FAIL(GeneralConst.RENAME_FILE_FAIL, "重命名失败"),
    DELETE_FAIL(GeneralConst.DELETE_FILE_FAIL, "删除失败"),
    NETWORK_ERROR(GeneralConst.NETWORK_ERROR, "请求第三方服务失败"),
    PRODUCT_STATUS_ERROR(GeneralConst.PRODUCT_STATUS_ERROR, "物资状态错误"),
    PRODUCT_STOCK_ERROR(GeneralConst.PRODUCT_STOCK_ERROR, "物资库存不足"),
    INSTOCK_STATUS_ERROR(GeneralConst.INSTOCK_STATUS_ERROR, "入库单状态错误"),
    PRODUCT_IS_REMOVE(GeneralConst.PRODUCT_IS_REMOVE, "物资已被回收"),
    PRODUCT_INSTOCK_NUMBER_ERROR(GeneralConst.PRODUCT_INSTOCK_NUMBER_ERROR, "物资入库数量不合法"),
    PRODUCT_WAIT_PASS(GeneralConst.PRODUCT_WAIT_PASS, "物资待待审核"),
    PRODUCT_OUTSTOCK_EMPTY(GeneralConst.PRODUCT_OUTSTOCK_EMPTY, "物资发放不能为空"),
    PRODUCT_NOT_FOUND(GeneralConst.PRODUCT_NOT_FOUND, "物资未找到"),
    TOKEN_ERROR(GeneralConst.TOKEN_ERROR, "用户未认证"),
    USER_COUNT_ERROR(GeneralConst.USER_STATUS, "用户账户异常"),
    USER_PASSWORD_ERROR(GeneralConst.PASSWORD_ERROR, "密码错误"),
    VERIFYCODE_ERROR(GeneralConst.VERIFYCODE_ERROR, "验证码已过期"),
    OPERATE_FREQUENCY(GeneralConst.OPERATE_FREQUENCY, "操作频繁");


    /**
     * 状态码
     */
    public final int code;
    /**
     * 信息
     */
    public final String message;

    ServerEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
