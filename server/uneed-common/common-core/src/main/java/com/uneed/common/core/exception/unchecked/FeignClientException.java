package com.uneed.common.core.exception.unchecked;

import com.uneed.common.core.http.HttpCode;

/**
 * feign client 请求异常.
 *
 * @author diablo
 * @date 2020/5/2
 * @since 1.1.0
 */
public class FeignClientException extends UncheckedException {

    private static final long serialVersionUID = -340611234487220247L;

    private static final HttpCode HTTP_CODE = HttpCode.FEIGN_CLIENT_FALLBACK;

    public FeignClientException() {
        super(HTTP_CODE.getCode(), HTTP_CODE.getMessage());
    }

    public FeignClientException(String message) {
        super(HTTP_CODE.getCode(), message);
    }

    public FeignClientException(String message, Throwable throwable) {
        super(HTTP_CODE.getCode(), message, throwable);
    }

    public FeignClientException(Throwable throwable) {
        super(HTTP_CODE.getCode(), HTTP_CODE.getMessage(), throwable);
    }

    public FeignClientException(Integer code) {
        super(code, HTTP_CODE.getMessage());
    }

    public FeignClientException(Integer code, String message) {
        super(code, message);
    }

    public FeignClientException(Integer code, Throwable throwable) {
        super(code, HTTP_CODE.getMessage(), throwable);
    }

    public FeignClientException(Integer code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
