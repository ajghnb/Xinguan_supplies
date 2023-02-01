package com.example.exception;

import com.example.exception.asserts.IAssert;
import com.example.exception.base.LimitStackRuntimeException;


/**
 * @author 18237
 */
public class ApiRuntimeException extends LimitStackRuntimeException {

    public <T extends IAssert> ApiRuntimeException(T iAssert, String msg) {
        super(iAssert.getCode(), msg);
    }

    public <T extends IAssert> ApiRuntimeException(T iAssert) {
        super(iAssert);
    }
}
