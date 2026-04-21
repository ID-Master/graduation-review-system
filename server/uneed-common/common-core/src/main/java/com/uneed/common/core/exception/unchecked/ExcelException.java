package com.uneed.common.core.exception.unchecked;

import com.uneed.common.core.http.HttpCode;

public class ExcelException extends UncheckedException {

    private static final HttpCode HTTP_CODE = HttpCode.BUSINESS_HANDLER_FAILED;

    public ExcelException() {
        super(HTTP_CODE.getCode(), HTTP_CODE.getMessage());
    }

    public ExcelException(String message) {
        super(HTTP_CODE.getCode(), message);
    }

    public ExcelException(String message, Throwable throwable) {
        super(HTTP_CODE.getCode(), message, throwable);
    }

    public ExcelException(Throwable throwable) {
        super(HTTP_CODE.getCode(), HTTP_CODE.getMessage(), throwable);
    }

    public ExcelException(Integer code) {
        super(code, HTTP_CODE.getMessage());
    }

    public ExcelException(Integer code, String message) {
        super(code, message);
    }

    public ExcelException(Integer code, Throwable throwable) {
        super(code, HTTP_CODE.getMessage(), throwable);
    }

    public ExcelException(Integer code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

}
