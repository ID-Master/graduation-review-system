package com.uneed.common.support.api;

import com.uneed.common.core.http.HttpCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.uneed.common.core.lang.ObjectUtil.equal;

/**
 * 统一响应结果
 *
 * @author diablo
 * @date 2020/4/25
 * @since 1.1.0
 */
@Data
@ApiModel(value = "Result", description = "响应结果")
public class Result<T> {

    /**
     * 是否成功
     */
    @ApiModelProperty(value = "是否成功", name = "success", position = 1)
    private boolean success;

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码", name = "code", position = 2)
    private int code;

    /**
     * 返回消息
     */
    @ApiModelProperty(value = "返回消息", name = "message", position = 3)
    private String message;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据", name = "data", position = 4)
    private T data;

    ///////////////////////////// 构造函数 ///////////////////////////////////////////////////////////////////

    /**
     * 含有http状态码{@link HttpCode}的构造函数
     *
     * @param httpCode http状态码
     */
    public Result(HttpCode httpCode) {
        this(httpCode, httpCode.getMessage());
    }

    /**
     * 含有http状态码{@link HttpCode}、自定义返回消息的构造函数
     *
     * @param httpCode http状态码
     * @param message  自定义返回消息
     */
    public Result(HttpCode httpCode, String message) {
        this(httpCode, message, null);
    }

    /**
     * 含有http状态码{@link HttpCode}、返回数据的构造函数
     *
     * @param httpCode http状态码
     * @param data     返回数据
     */
    public Result(HttpCode httpCode, T data) {
        this(httpCode, httpCode.getMessage(), data);
    }

    /**
     * 含有http状态码{@link HttpCode}、自定义返回消息、返回数据的构造函数
     *
     * @param httpCode http状态码
     * @param message  自定义返回消息
     * @param data     返回数据
     */
    public Result(HttpCode httpCode, String message, T data) {
        this(httpCode.getCode(), message, data);
    }

    /**
     * 含有自定义状态码、自定义返回消息、返回数据的构造函数
     *
     * @param code    自定义状态码
     * @param message 自定义返回消息
     * @param data    返回数据
     */
    public Result(int code, String message, T data) {
        this(equal(code, HttpCode.OK.getCode()), code, message, data);
    }

    /**
     * 含有自定义状态码、自定义返回消息、返回数据的构造函数
     *
     * @param success 状态
     * @param code    自定义状态码
     * @param message 自定义返回消息
     * @param data    返回数据
     */
    public Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    ///////////////////////////// 操作成功 ///////////////////////////////////////////////////////////////////

    /**
     * 操作成功
     * <p>
     * {"status": true, "code": 200, "message": "操作成功"}
     *
     * @param <T> 泛型参数
     * @return Result
     */
    public static <T> Result<T> ok() {
        return new Result<>(HttpCode.OK);
    }

    /**
     * 操作成功
     * <p>
     * {"status": true, "code": 200, "message": "操作成功", "data": {返回数据}}
     *
     * @param data 返回数据
     * @param <T>  泛型参数
     * @return Result<T>
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(HttpCode.OK, data);
    }

    /**
     * 操作成功
     * <p>
     * {"status": true, "code": 200, "message": "操作成功", "data": {返回数据}}
     *
     * @param data 返回数据
     * @param <T>  泛型参数
     * @return Result<T>
     */
    public static <T> Result<T> ok(T data, int code) {
        return new Result<>(true, code, HttpCode.OK.getMessage(), data);
    }

    /**
     * 操作成功
     * <p>
     * {"status": true, "code": 200, "message": "返回消息", "data": {返回数据}}
     *
     * @param data    返回数据
     * @param message 返回消息
     * @param <T>     泛型参数
     * @return Result<T>
     */
    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(HttpCode.OK, message, data);
    }

    /**
     * 操作成功
     * <p>
     * {"status": true, "code": 200, "message": "返回消息", "data": {返回数据}}
     *
     * @param data    返回数据
     * @param code    状态码
     * @param message 返回消息
     * @param <T>     泛型参数
     * @return Result<T>
     */
    public static <T> Result<T> ok(T data, int code, String message) {
        return new Result<>(true, code, message, data);
    }

    ///////////////////////////// 请求失败 ///////////////////////////////////////////////////////////////////

    /**
     * 请求失败
     * <p>
     * {"status": false, "code": 400, "message": "请求失败"}
     *
     * @param <T> 泛型参数
     * @return Result
     */
    public static <T> Result<T> fail() {
        return new Result<>(HttpCode.FAIL);
    }

    /**
     * 请求失败
     * <p>
     * {"status": false, "code": 400, "message": "返回消息"}
     *
     * @param message 返回消息
     * @param <T>     泛型参数
     * @return Result
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(HttpCode.FAIL, message);
    }

    /**
     * 请求失败
     * <p>
     * {"status": false, "code": 状态码, "message": "返回消息"}
     *
     * @param code    状态码
     * @param message 返回消息
     * @param <T>     泛型参数
     * @return Result
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(false, code, message, null);
    }

    /**
     * 请求失败
     * <p>
     * {"status": false, "code": httpCode编号, "message": "httpCode消息"}
     *
     * @param httpCode httpCode状态码
     * @param <T>      泛型参数
     * @return Result
     */
    public static <T> Result<T> fail(HttpCode httpCode) {
        return new Result<>(false, httpCode.getCode(), httpCode.getMessage(), null);
    }

    /**
     * 请求失败
     * <p>
     * {"status": false, "code": httpCode编号, "message": "返回消息"}
     *
     * @param httpCode httpCode状态码
     * @param message  返回消息
     * @param <T>      泛型参数
     * @return Result
     */
    public static <T> Result<T> fail(HttpCode httpCode, String message) {
        return new Result<>(false, httpCode.getCode(), message, null);
    }

}
