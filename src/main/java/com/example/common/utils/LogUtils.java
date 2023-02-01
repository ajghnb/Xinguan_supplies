package com.example.common.utils;

import com.example.exception.asserts.Assert;
import com.example.exception.asserts.IAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;


/**
 * @author 18237
 */
@SuppressWarnings("unused")
public class LogUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);

    private static final int DEFAULT_STACK_LINE = 18;

    private static final String[] IGNORE_CLASS = {
            LogUtils.class.getName(),
            Assert.class.getName(),
            IAssert.class.getName()
    };

    private static Object[] format(Object... objects) {
        if(objects == null) {
            return new Object[]{null};
        }
        return objects;
    }

    public static String toLogStr(String format, Object... args) {
        return MessageFormatter
                .arrayFormat(format, format(args)).getMessage();
    }

    public static String getMark() {
        StackTraceElement[] trace = new Throwable().getStackTrace();
        for (StackTraceElement element : trace) {
            if (!isIgnoreElement(element)) {
                return element.toString();
            }
        }
        return "获取堆栈失败";
    }

    private static boolean isIgnoreElement(StackTraceElement element) {
        for (String ignoreName : IGNORE_CLASS) {
            if (ignoreName.equalsIgnoreCase(element.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private static void applyCause(Throwable t, StringBuilder builder, int limit) {
        builder.append("\n\t\t")
                .append(t.getClass().getSimpleName())
                .append(": ")
                .append(t.getMessage());
        if (t.getCause() != null&&limit>1) {
            applyCause(t.getCause(), builder, --limit);
        }
    }

    public static String toLimitString(Throwable e) {
        return toLimitString(e, DEFAULT_STACK_LINE);
    }

    /**
     * 转换错误信息以及部分堆栈追踪为String
     * @param e 错误
     * @param limit 限制堆栈追踪的数量
     */
    public static String toLimitString(Throwable e, int limit) {
        StackTraceElement[] traces = e.getStackTrace();
        int line = Math.min(traces.length, limit);

        StringBuilder builder = new StringBuilder(e.getClass().getName())
                .append(": ")
                .append(e.getMessage())
                .append(" => {");

        if (e.getCause() != null) {
            builder.append("\n\tCauseBy:");
            applyCause(e.getCause(), builder, 4);
        }

        for (int i = 0; i < line; i++) {
            builder.append("\n\t").append(traces[i]);
        }

        builder.append("\n}");

        return builder.toString();
    }

    public static void limitedStackTrace(Throwable e, int limit, String message) {
        LOGGER.error(message+"\n"+toLimitString(e, limit));
    }

    public static void limitedStackTrace(Throwable e, int limit, String format, Object... args) {
        limitedStackTrace(e, limit, toLogStr(format, args));
    }

    public static void limitedStackTrace(Throwable e, String format, Object... args) {
        limitedStackTrace(e, DEFAULT_STACK_LINE, toLogStr(format, args));
    }

    public static void limitedStackTrace(Throwable e, String message) {
        limitedStackTrace(e, DEFAULT_STACK_LINE, message);
    }

    public static void limitedStackTrace(Throwable e, int limit) {
        limitedStackTrace(e, limit, "StackTrace ↓");
    }

    public static void limitedStackTrace(Throwable e) {
        limitedStackTrace(e, DEFAULT_STACK_LINE, "StackTrace ↓");
    }
}
