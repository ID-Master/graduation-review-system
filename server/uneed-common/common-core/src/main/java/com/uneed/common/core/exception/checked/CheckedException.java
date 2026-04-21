package com.uneed.common.core.exception.checked;

/**
 * 包装了checked类型的异常，为其增加一个code的错误编码，方便后续统一异常处理
 *
 * @author diablo
 * @date 2017/12/28
 */
public class CheckedException extends Exception {

    private static final long serialVersionUID = 4122022678523476141L;

    private Integer code;

    public CheckedException() {
        super();
    }

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    public CheckedException(Integer code) {
        super();
        this.code = code;
    }

    public CheckedException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CheckedException(Integer code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public CheckedException(Integer code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
