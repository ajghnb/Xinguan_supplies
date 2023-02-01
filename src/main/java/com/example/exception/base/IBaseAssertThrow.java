package com.example.exception.base;

import java.util.function.Function;


/**
 * @author 18237
 */
public interface IBaseAssertThrow {
    /**
     * 自定义异常操作
     * @param t 传入对象
     * @param fun 自定义函数
     * @return
     */
    default <T, R> R apply(T t, Function<? super T, R> fun) {
        return fun.apply(t);
    }
}
