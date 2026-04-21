package com.uneed.common.support.base;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.exception.ExceptionUtil;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.exception.unchecked.FeignClientException;
import com.uneed.common.core.exception.unchecked.TokenException;
import com.uneed.common.core.exception.unchecked.UncheckedException;
import com.uneed.common.core.http.HttpCode;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.support.api.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器的超类
 * <p>
 * 1. 定义了统一的返回信息
 * <p>
 * 2. 定义了控制器的全局异常处理
 *
 * @author diablo
 * @date 2020/4/27
 * @since 1.1.0
 */
public abstract class AbstractController {

    /**
     * 日志记录器
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取 HttpServletRequest
     * @return {HttpServletRequest}
     */
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取 HttpServletResponse
     * @return {HttpServletResponse}
     */
    protected HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 响应成功
     *
     * @param <T> 泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> success() {
        return Result.ok();
    }

    /**
     * 响应成功，并设置响应数据
     *
     * @param data 响应数据
     * @param <T>  泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> success(T data) {
        return Result.ok(data);
    }

    /**
     * 响应成功，并设置响应数据、响应消息
     *
     * @param data    响应数据
     * @param message 响应消息
     * @param <T>     泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> success(T data, String message) {
        return Result.ok(data, message);
    }

    /**
     * 响应失败
     *
     * @param <T> 泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> failed() {
        return Result.fail();
    }

    /**
     * 响应失败，并设置响应消息
     *
     * @param message 响应消息
     * @param <T>     泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> failed(String message) {
        return Result.fail(message);
    }

    /**
     * 响应失败，并设置状态码、响应消息
     *
     * @param code    状态码
     * @param message 响应消息
     * @param <T>     泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> failed(int code, String message) {
        return Result.fail(code, message);
    }

    /**
     * 响应失败，并设置通用的HttpCode状态码常量
     *
     * @param httpCode 通用的HttpCode状态码常量
     * @param <T>      泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> failed(HttpCode httpCode) {
        return Result.fail(httpCode);
    }

    /**
     * 响应失败，并设置通用的HttpCode状态码常量、响应消息
     *
     * @param httpCode 通用的HttpCode状态码常量
     * @param message  响应消息
     * @param <T>      泛型参数
     * @return Result<T> 统一响应结果
     */
    protected <T> Result<T> failed(HttpCode httpCode, String message) {
        return Result.fail(httpCode, message);
    }

    /**
     * 统一异常处理
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param e        异常信息
     * @return Object 响应信息
     */
    @ExceptionHandler({Exception.class})
    protected Object exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        //控制台打印异常
        log.error("================================ <<< 统一异常处理 >>> ================================");
        log.error("request uri: " + request.getRequestURI());
        log.error("request params: " + JsonUtil.toJson(request.getParameterMap()));
        log.error("exception message: " + e.getMessage(), e);
        log.error("====================================================================================");
        //token异常
        if (e instanceof TokenException) {
            return failed(((TokenException) e).getCode(), e.getMessage());
        } else if (e instanceof FeignClientException) {
            return failed(((FeignClientException) e).getCode(), e.getMessage());
        } else if (e instanceof BusinessException) {
            return failed(((BusinessException) e).getCode(), e.getMessage());
        }
        //其他异常，直接获取异常信息返回
        return failed(getErrorMessage(e));
    }

    /**
     * 获取异常消息
     *
     * @param e 异常
     * @return String 异常消息
     */
    private String getErrorMessage(Exception e) {
        //参数校验异常
        if (e instanceof MethodArgumentNotValidException) {
            String[] errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            return ArrayUtil.join(errors, " | ");
        }
        //excel异常
        if (e instanceof ExcelAnalysisException) {
            return ExceptionUtil.getSimpleMessage(e);
        }
        //Unchecked异常
        else if (e instanceof UncheckedException) {
            return ExceptionUtil.getSimpleMessage(e);
        }
        else {
            log.error("=================>接口抛出异常: {}", e.getMessage(), e);
        }
        //其他情况
        return "服务器内部异常";
    }
}
