package com.uneed.common.core.http;

import lombok.Getter;

/**
 * Http状态码.
 *
 * @author diablo
 * @date 2020/4/25
 * @since 1.1.0
 */
@Getter
public enum HttpCode {

    /**
     * 操作成功
     */
    OK(200, "操作成功", "ok"),

    /**
     * 请求失败
     */
    FAIL(400, "请求失败", "bad request"),

    /**
     * 请求未授权
     */
    UNAUTHORIZED(401, "请求未授权", "unauthorized"),

    /**
     * 请求被拒绝
     */
    FORBIDDEN(403, "请求被拒绝", "forbidden"),

    /**
     * 404 找到不请求
     */
    NOT_FOUND(404, "404 找不到请求", "not found"),

    /**
     * 请求方法被禁止
     */
    METHOD_NOT_ALLOWED(405, "请求方法被禁止", "method not allowed"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时", "request timeout"),

    /**
     * 请求发生冲突
     */
    CONFLICT(409, "请求发生冲突", "conflict"),

    /**
     * 请求资源已被删除
     */
    GONE(410, "请求资源已被删除", "gone"),

    /**
     * 不支持的媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型", "unsupported media type"),

    /**
     * 服务器异常
     */
    INTERNAL_SERVER_ERROR(500, "500 服务器异常", "internal server error"),

    /**
     * 默认token异常
     */
    TOKEN_FORBIDDEN(4201, "请求token被拒绝", "token forbidden"),

    /**
     * token失效
     */
    TOKEN_EXPIRED(4202, "会话失效，请重新登录", "token expired"),

    /**
     * token为空
     */
    TOKEN_EMPTY(4203, "Token为空", "token empty"),

    /**
     * token格式不正确
     */
    TOKEN_MALFORMED(4204, "Token格式不正确", "token malformed"),

    /**
     * token异常
     */
    TOKEN_ERROR(4205, "Token异常", "token error"),

    /**
     * 同终端（PC，APP，MP）自动挤账号
     */
    TOKEN_FORCED_OFFLINE(4206, "账号在另一台设备上登录，强制下线", "forced offline"),

    /**
     * 默认参数校验失败
     */
    PARAMETER_VERIFICATION_FAILED(4301, "参数校验失败", "parameter verification failed"),

    /**
     * 默认业务处理失败
     */
    BUSINESS_HANDLER_FAILED(4401, "业务处理失败", "business handler failed"),

    /**
     * 默认FeignClient异常
     */
    FEIGN_CLIENT_FALLBACK(4501, "FeignClient请求异常", "feign client fallback");

    /**
     * 响应编码
     */
    private final int code;

    /**
     * 中文消息
     */
    private final String message;

    /**
     * 英文描述信息
     */
    private final String description;

    /**
     * 构造函数
     */
    HttpCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
