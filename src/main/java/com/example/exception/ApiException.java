package com.example.exception;

import com.example.exception.asserts.IAssert;
import com.example.exception.base.BaseException;
import lombok.Getter;


/**
 * @author 18237
 */
@Getter
public class ApiException extends BaseException {
    private final String detail;

    public ApiException(int code, String msg, String detail, Object data) {
        super(code, msg, data);
        this.detail = detail;
    }

    public ApiException(int code, String msg, String detail) {
        super(code, msg);
        this.detail = detail;
    }

    public ApiException(int code, String msg) {
        super(code, msg);
        this.detail = null;
    }

    public <T extends IAssert> ApiException(T iAssert, String detail, Object data) {
        super(iAssert, data);
        this.detail = detail;
    }

    public <T extends IAssert> ApiException(T iAssert, String detail) {
        super(iAssert);
        this.detail = detail;
    }

    public <T extends IAssert> ApiException(T iAssert) {
        super(iAssert);
        this.detail = null;
    }
}