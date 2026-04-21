package com.uneed.common.core.exception;

import com.uneed.common.core.lang.StringUtil;

/**
 * 工具类异常
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class UtilException extends RuntimeException {

    private static final long serialVersionUID = -7650542600829946688L;

    public UtilException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UtilException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params), throwable);
    }
}
