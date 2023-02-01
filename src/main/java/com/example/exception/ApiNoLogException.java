package com.example.exception;

import com.example.exception.base.BaseRuntimeException;


/**
 * @author 18237
 */
public class ApiNoLogException extends BaseRuntimeException {
    public ApiNoLogException(int code, String msg, Object data) {
        super(code, msg, data);
    }

    public ApiNoLogException(int code, String msg) {
        super(code, msg);
    }
}
