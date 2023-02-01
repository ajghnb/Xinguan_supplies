package com.example.exception.base;

import com.example.exception.asserts.IAssert;
import com.example.exception.asserts.IAssert;

import java.util.Arrays;


/**
 * @author 18237
 */
public class LimitStackRuntimeException extends BaseRuntimeException {
    public LimitStackRuntimeException(int code, String msg, Object data) {
        super(code, msg, data);
    }

    public <T extends IAssert> LimitStackRuntimeException(int code, String msg) {
        super(code, msg);
    }

    public <T extends IAssert> LimitStackRuntimeException(T iAssert, Object data) {
        super(iAssert, data);
    }

    public <T extends IAssert> LimitStackRuntimeException(T iAssert) {
        super(iAssert);
    }

    public LimitStackRuntimeException(int code, String message, Object data, Throwable cause) {
        super(code, message, data, cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        Throwable throwable = super.fillInStackTrace();

        StackTraceElement[] stacks = Arrays.stream(throwable.getStackTrace())
                .filter(e -> e.getClassName().startsWith("com.myqm"))
                .toArray(StackTraceElement[]::new);

        throwable.setStackTrace(stacks);
        return throwable;
    }
}
