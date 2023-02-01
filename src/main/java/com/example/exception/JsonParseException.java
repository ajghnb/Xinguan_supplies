package com.example.exception;

import com.example.consts.GeneralConst;
import com.example.exception.base.LimitStackRuntimeException;


/**
 * @author 18237
 */
public class JsonParseException extends LimitStackRuntimeException {
    public JsonParseException(String message, Throwable cause) {
        super(GeneralConst.ERROR, message, null, cause);
    }
}
