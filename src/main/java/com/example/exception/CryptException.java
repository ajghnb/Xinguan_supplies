package com.example.exception;

import com.example.consts.GeneralConst;
import com.example.exception.base.BaseException;


/**
 * @author 18237
 */
public class CryptException extends BaseException {
    public CryptException(String msg, Throwable e) {
        super(GeneralConst.ERROR, msg, null, e);
    }

    @Override
    public String toString() {
        return "CryptException: " + getMessage();
    }
}
