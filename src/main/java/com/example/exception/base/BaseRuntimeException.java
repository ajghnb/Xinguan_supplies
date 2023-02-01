package com.example.exception.base;

import com.example.exception.asserts.IAssert;
import com.example.exception.asserts.IAssert;
import lombok.Getter;


/**
 * @author 18237
 */
@Getter
public class BaseRuntimeException extends RuntimeException {
    /** 错误码 */
    public final int code;
    /** 错误数据 */
    public final Object data;

    public BaseRuntimeException(int code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.data = data;
    }

    public BaseRuntimeException(int code, String msg) {
        this(code, msg, null);
    }

    public <T extends IAssert> BaseRuntimeException(T iAssert, Object data) {
        this(iAssert.getCode(), iAssert.getMessage(), data);
    }

    public <T extends IAssert> BaseRuntimeException(T iAssert) {
        this(iAssert.getCode(), iAssert.getMessage(), null);
    }

    public <T extends IAssert> BaseRuntimeException(int code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
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
