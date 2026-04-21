package com.uneed.common.core.exception;

import com.uneed.common.core.lang.StringUtil;

import static com.uneed.common.core.lang.ObjectUtil.isEmpty;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 异常工具类
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public final class ExceptionUtil {

    private static final String NULL = "null";

    private static final String NULL_POINTER = "null pointer";

    /**
     * 私有化构造函数，禁止实例化该类
     */
    private ExceptionUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获得完整消息，包括异常名、异常消息
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(Throwable e) {
        return isNull(e) ? NULL : StringUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }

    /**
     * 获得简洁消息，只包含异常消息
     *
     * @param e 异常
     * @return 简洁消息
     */
    public static String getSimpleMessage(Throwable e) {
        return isNull(e) ? NULL : isEmpty(e.getMessage()) ? NULL_POINTER : e.getMessage();
    }

    /**
     * 使用运行时异常包装编译异常
     *
     * @param throwable 异常
     * @return 运行时异常
     */
    public static RuntimeException wrapRuntime(Throwable throwable) {
        return (throwable instanceof RuntimeException) ? (RuntimeException) throwable : new RuntimeException(throwable);
    }

    /**
     * 获取当前栈信息
     *
     * @return 当前栈信息
     */
    public static StackTraceElement[] getStackElements() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 获取指定层的堆栈信息
     *
     * @return 指定层的堆栈信息
     */
    public static StackTraceElement getStackElement(int i) {
        return getStackElements()[i];
    }

    /**
     * 获取入口堆栈信息
     *
     * @return 入口堆栈信息
     */
    public static StackTraceElement getRootStackElement() {
        final StackTraceElement[] stackElements = getStackElements();
        return stackElements[stackElements.length - 1];
    }
}
