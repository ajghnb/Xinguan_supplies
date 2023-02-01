package com.example.consts;

public class GeneralConst {

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 服务器异常
     */
    public static final int ERROR = -1;

    /**
     * 数据库操作异常
     */
    public static final int DB_OPERATE_ERROR = -2;

    /**
     * 参数错误
     */
    public static final int PARAMETER_ERROR = -3;

    /**
     * 权限不足
     */
    public static final int ACCESS_DENIED = -4;

    /**
     * 未登录
     */
    public static final int NOT_LOGIN = -5;

    /**
     * 对象不存在
     */
    public static final int NOT_EXIST = -6;

    /**
     * 对象已存在
     */
    public static final int ALREADY_EXIST = -7;

    /**
     * 登录信息过期
     */
    public static final int LOGIN_EXPIRED = -8;

    /**
     * 用户信息不全
     */
    public static final int USER_UNKNOWN = -9;

    /**
     * 创建文件失败
     */
    public static final int CREATE_FILE_FAIL = -100;

    /**
     * 重命名失败
     */
    public static final int RENAME_FILE_FAIL = -101;

    /**
     * 删除失败
     */
    public static final int DELETE_FILE_FAIL = -102;

    /**
     * 文件操作失败
     */
    public static final int FILE_OPERATE_FAIL = -105;

    /**
     * 文件无后缀名
     */
    public static final int FILE_NO_SUFFIX = -106;


    /**
     * 文件大小超过服务器限制
     */
    public static final int FILE_TO_LARGER = -109;

    /**
     * 大于1的code为非业务错误, 仅不允许
     * 操作失败(请求成功, 仅不允许操作)
     */
    public static final int OPERATE_FAIL = 1;

    /**
     * 网络服务请求失败
     */
    public static final int NETWORK_ERROR = 2;

    /**
     * 物资状态错误
     */
    public static final int PRODUCT_STATUS_ERROR = 3004;

    /**
     * 物资状态错误
     */
    public static final int INSTOCK_STATUS_ERROR  = 3005;

    /**
     * 物资回收
     */
    public static final int PRODUCT_IS_REMOVE = 3006;

    /**
     * 物资待审核
     */
    public static final int PRODUCT_WAIT_PASS = 3006;

    /**
     * 物资回收
     */
    public static final int PRODUCT_INSTOCK_NUMBER_ERROR = 3007;

    /**
     * 入库单物资为空
     */
    public static final int PRODUCT_INSTOCK_EMPTY = 3008;

    /**
     * 物资找不到
     */
    public static final int PRODUCT_NOT_FOUND = 3002;

    /**
     * 物资库存不足
     */
    public static final int PRODUCT_STOCK_ERROR = 3009;


    public static final int PRODUCT_OUTSTOCK_EMPTY = 3001;

    public static final int TOKEN_ERROR = 5001;

    public static final int USER_STATUS = 3010;

    public static final int PASSWORD_ERROR = 3011;

    public static final int VERIFYCODE_ERROR = 5002;


}
