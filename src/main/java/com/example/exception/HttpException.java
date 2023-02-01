package com.example.exception;

import com.example.exception.asserts.IAssert;
import com.example.exception.base.LimitStackRuntimeException;


/**
 * @author 18237
 */
public class HttpException extends LimitStackRuntimeException {
    public <T extends IAssert> HttpException(T iAssert, Object data, Throwable cause) {
        super(iAssert.getCode(), iAssert.getMessage(), data, cause);
    }
}
