package com.example.exception;

import com.example.exception.asserts.IAssert;
import com.example.exception.base.BaseRuntimeException;
import com.example.exception.asserts.IAssert;
import com.example.exception.base.BaseRuntimeException;


/**
 * @author 18237
 */
public class LoginException extends BaseRuntimeException {
    public <T extends IAssert> LoginException(T iAssert) {
        super(iAssert);
    }
}
