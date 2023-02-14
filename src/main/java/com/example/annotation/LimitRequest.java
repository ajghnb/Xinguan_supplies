package com.example.annotation;

import java.lang.annotation.*;

/**
 * @author 18237
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequest {

    /**
     * 限制时间 单位：毫秒
     */
    long time() default 6000;

    /**
     * 允许请求的次数
     */
    int count() default 2;

}
 
