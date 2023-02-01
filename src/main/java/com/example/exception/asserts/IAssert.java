package com.example.exception.asserts;

import com.example.exception.base.IBaseAssert;
import com.example.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author 18237
 * Date: 2020/10/20 9:49
 * Description: 断言实现接口
 */
public interface IAssert extends IBaseAssert {
    default <T> AssertWrapper<IAssert, T> notNull(T object) {
        return new AssertWrapper<>(object == null, this, object);
    }

    default <T> AssertWrapper<IAssert, T[]> notEmpty(T[] array) {
        return new AssertWrapper<>(ObjectUtils.isEmpty(array), this, array);
    }

    default AssertWrapper<IAssert, Collection<?>> notEmpty(Collection<?> list) {
        return new AssertWrapper<>(CollectionUtils.isEmpty(list), this, list);
    }

    default AssertWrapper<IAssert, Map<?, ?>> notEmpty(Map<?, ?> map) {
        return new AssertWrapper<>(CollectionUtils.isEmpty(map), this, map);
    }

    default AssertWrapper<IAssert, Void> notBlank(String str) {
        return new AssertWrapper<>(StringUtils.isBlank(str), this, null);
    }

    default AssertWrapper<IAssert, Void> success(boolean flag) {
        return new AssertWrapper<>(!flag, this, null);
    }

    default AssertWrapper<IAssert, Void> sqlSuccess(int count) {
        boolean flag = count != 1;
        if (flag) {
            LogUtils.LOGGER.error("sql执行失败, 受影响行数'{}'与预期'1'不一致", count);
        }
        return new AssertWrapper<>(flag, this, null);
    }

    default AssertWrapper<IAssert, Void> sqlSuccess(int count, int expect) {
        boolean flag = count != expect;
        if (flag) {
            LogUtils.LOGGER.error("sql执行失败, 受影响行数'{}'与预期'{}'不一致", count, expect);
        }
        return new AssertWrapper<>(flag, this, null);
    }

    class AssertWrapper<T extends IAssert, R> {
        private final boolean isThrow;
        private final T iAssert;
        private final R data;

        protected AssertWrapper(boolean isThrow, T iAssert, R data) {
            this.isThrow = isThrow;
            this.iAssert = iAssert;
            this.data = data;
        }

        public <E extends Exception> R orThrow(Function<T, E> orElse) throws E {
            if (isThrow) {
                throw orElse.apply(iAssert);
            }

            return data;
        }

        public <E extends Exception> R orThrow(BiFunction<T, R, E> orElse) throws E {
            if (isThrow) {
                throw orElse.apply(iAssert, data);
            }

            return data;
        }
    }
}
