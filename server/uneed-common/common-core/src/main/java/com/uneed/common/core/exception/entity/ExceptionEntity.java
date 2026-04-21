package com.uneed.common.core.exception.entity;

import com.uneed.common.core.exception.checked.CheckedException;
import com.uneed.common.core.exception.unchecked.UncheckedException;
import com.uneed.common.core.lang.ObjectUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 17/12/31
 */
public class ExceptionEntity implements Serializable {

    private static final long serialVersionUID = 1881343016888477702L;
    /**
     * 异常时间
     */
    private Long timestamp;

    /**
     * 请求的url
     */
    private String url;

    /**
     * 资源标识
     */
    private String uri;

    /**
     * 请求的ip
     */
    private String ip;

    /**
     * 请求方法
     */
    private String method;

    /**
     * sessionId
     */
    private String sessionId;

    /**
     * 请求参数
     */
    private Map<String, String[]> params;

    /**
     * 是否ajax请求
     */
    private Boolean ajaxRequest;

    /**
     * 异常编号
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String message;

    public ExceptionEntity() {

    }

    public ExceptionEntity(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        this.timestamp = System.currentTimeMillis();
        this.url = request.getRequestURL().toString();
        this.uri = request.getRequestURI();
        this.ip = request.getRemoteHost();
        this.method = request.getMethod();
        this.params = request.getParameterMap();
        this.sessionId = request.getSession().getId();
        this.ajaxRequest = request.getHeader("accept").contains("application/json") || (request
                .getHeader("X-Requested-With") != null && "XMLHttpRequest"
                .equalsIgnoreCase(request.getHeader("X-Requested-With")));
        if (exception instanceof UncheckedException) {
            this.code = ((UncheckedException) exception).getCode();
        } else if (exception instanceof CheckedException) {
            this.code = ((CheckedException) exception).getCode();
        } else {
            this.code = 0;
        }
        this.message = exception.getMessage();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }

    public Boolean isAjaxRequest() {
        return ajaxRequest;
    }

    public void setAjaxRequest(Boolean ajaxRequest) {
        this.ajaxRequest = ajaxRequest;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestInfo(){
        return "{timestamp=" + timestamp + ", url='" + url + '\'' + ", uri='" + uri + '\'' + ", ip='" + ip + '\'' + ", method='" + method + '\'' + ", sessionId='" + sessionId + '\'' + ", params=" + paramsToString() + ", ajaxRequest=" + ajaxRequest + "}";
    }

    @Override
    public String toString() {
        return "ExceptionEntity{" + "timestamp=" + timestamp + ", url='" + url + '\'' + ", uri='" + uri + '\'' + ", ip='" + ip + '\'' + ", method='" + method + '\'' + ", sessionId='" + sessionId + '\'' + ", params=" + paramsToString() + ", ajaxRequest=" + ajaxRequest + ", code=" + code + ", message='" + message + '\'' + '}';
    }

    private String paramsToString() {
        StringBuilder sb = new StringBuilder("[");
        if (ObjectUtil.isNotEmpty(params)) {
            int i = 0;
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                sb.append("{").append(entry.getKey()).append("=[");
                String[] values = ObjectUtil.nullToDefault(entry.getValue(), new String[0]);
                for (int k = 0; k < values.length; k++) {
                    sb.append("'").append(values[k]).append("'");
                    if (k < values.length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]}");
                if (i < params.size() - 1) {
                    sb.append(", ");
                }
                i++;
            }

        }
        return sb.append("]").toString();
    }
}
