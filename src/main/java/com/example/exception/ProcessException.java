package com.example.exception;

import com.example.exception.asserts.IAssert;
import com.example.exception.base.LimitStackRuntimeException;


public class ProcessException extends LimitStackRuntimeException {
    public ProcessException(int code, String msg, Throwable cause) {
        super(code, msg, null, cause);
    }

    public <T extends IAssert> ProcessException(int code, String msg) {
        super(code, msg);
    }

    public <T extends IAssert> ProcessException(T iAssert, Throwable cause) {
        super(iAssert.getCode(), iAssert.getMessage(), null, cause);
    }

    public <T extends IAssert> ProcessException(T iAssert) {
        super(iAssert);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
