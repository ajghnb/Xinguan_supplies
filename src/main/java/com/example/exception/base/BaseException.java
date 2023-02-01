package com.example.exception.base;

import com.example.exception.asserts.IAssert;
import com.example.exception.asserts.IAssert;
import lombok.Getter;


/**
 * @author 18237
 */
@Getter
public class BaseException extends Exception {
    /** 错误码 */
    public final int code;
    /** 错误数据 */
    public final Object data;

    public BaseException(int code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.data = data;
    }

    public BaseException(int code, String msg) {
        this(code, msg, null);
    }

    public BaseException(int code, String msg, Object data, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.data = data;
    }

    public <T extends IAssert> BaseException(T iAssert, Object data) {
        this(iAssert.getCode(), iAssert.getMessage(), data);
    }

    public <T extends IAssert> BaseException(T iAssert) {
        this(iAssert.getCode(), iAssert.getMessage(), null);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{code: " + code +
                "; msg: " + super.getMessage() +
                "; data: " + data +
                '}';
    }
}
