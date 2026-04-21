package com.uneed.common.core.exception.unchecked;

import com.uneed.common.core.http.HttpCode;

/**
 * Token相关异常
 *
 * @author hcs
 * @date 2020-02-20
 */
public class TokenException extends UncheckedException {
    private static final long serialVersionUID = -1572640165745482068L;
    private static final HttpCode HTTP_CODE = HttpCode.TOKEN_FORBIDDEN;

    public TokenException() {
        super(HTTP_CODE.getCode(), HTTP_CODE.getMessage());
    }

    public TokenException(String message) {
        super(HTTP_CODE.getCode(), message);
    }

    public TokenException(String message, Throwable throwable) {
        super(HTTP_CODE.getCode(), message, throwable);
    }

    public TokenException(Throwable throwable) {
        super(HTTP_CODE.getCode(), HTTP_CODE.getMessage(), throwable);
    }

    public TokenException(Integer code) {
        super(code, HTTP_CODE.getMessage());
    }

    public TokenException(Integer code, String message) {
        super(code, message);
    }

    public TokenException(Integer code, Throwable throwable) {
        super(code, HTTP_CODE.getMessage(), throwable);
    }

    public TokenException(Integer code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
