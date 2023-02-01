package com.example.common.utils;

import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 1. @author: 18237
 * 2. @create: 2022-09-05 15:50
 * 3. @Description: 对象参数验证工具, 解决非controller层数据校验问题， @Validated、@Valid
 */
public class ValidationUtils {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws ApiRuntimeException 校验不通过，则报业务异常
     */
    public static void validateEntity(Object object, Class<?>... groups) throws IllegalArgumentException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            String msg = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("||"));
            throw new ApiRuntimeException(Assert.PARAMETER, "错误信息:{" + msg + "}," + "[" + object + "]" + "参数不合法");
        }
    }
}
