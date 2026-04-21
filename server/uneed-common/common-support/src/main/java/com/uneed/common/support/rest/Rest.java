package com.uneed.common.support.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.support.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * do something in here.
 *
 * @author huanganding
 * @date 2020/11/28
 */
@Slf4j
public class Rest<T> {

    /**
     * url地址上的常量
     */
    public static final String QUESTION = "?";

    /**
     * url地址上的常量
     */
    public static final String AND = "&";

    //////////////////////////////// 构造器 ///////////////////////////////////////////

    /**
     * 私有化构造函数
     */
    private Rest() {

    }

    /**
     * 私有化构造函数
     */
    private Rest(Class<T> responseType) {
        this.responseType = responseType;
    }

    /**
     * Rest请求构造器
     *
     * @param <T> 返回值泛型
     * @return Rest<T> rest请求对象
     */

    public static <T> Rest<T> build() {
        return new Rest<>();
    }

    /**
     * Rest请求构造器
     *
     * @param responseType 返回值类型
     * @param <T>          返回值泛型
     * @return Rest<T> rest请求对象
     */

    public static <T> Rest<T> build(Class<T> responseType) {
        return new Rest<>(responseType);
    }

    //////////////////////////////// 对外执行方法，返回请求结果 ///////////////////////////////////////////

    /**
     * get请求
     *
     * @param url 请求路径
     * @return 响应信息
     */
    public T get(String url) {
        return this.request(url, HttpMethod.GET);
    }

    /**
     * get请求
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return 响应信息
     */
    public T get(String url, Map<String, Object> params) {
        return this.request(url, HttpMethod.GET, params);
    }

    /**
     * get请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应信息
     */
    public T get(String url, Map<String, Object> params, HttpHeaders headers) {
        return this.request(url, HttpMethod.GET, params, headers);
    }

    /**
     * post请求
     *
     * @param url 请求路径
     * @return 响应信息
     */
    public T post(String url) {
        return this.request(url, HttpMethod.POST);
    }

    /**
     * post请求
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return 响应信息
     */
    public T post(String url, Map<String, Object> params) {
        return this.request(url, HttpMethod.POST, params);
    }

    /**
     * post请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应信息
     */
    public T post(String url, Map<String, Object> params, HttpHeaders headers) {
        return this.request(url, HttpMethod.POST, params, headers);
    }

    /**
     * 扩展请求
     *
     * @param url 请求路径
     * @return 响应信息
     */
    public T request(String url) {
        return this.url(url).request();
    }

    /**
     * 扩展请求
     *
     * @param url        请求路径
     * @param httpMethod 请求方法
     * @return 响应信息
     */
    public T request(String url, HttpMethod httpMethod) {
        return this.url(url).method(httpMethod).request();
    }

    /**
     * 扩展请求
     *
     * @param url        请求路径
     * @param httpMethod 请求方法
     * @param params     请求参数
     * @return 响应信息
     */
    public T request(String url, HttpMethod httpMethod, Map<String, Object> params) {
        return this.url(url).method(httpMethod).params(params).request();
    }

    /**
     * 扩展请求
     *
     * @param url        请求路径
     * @param httpMethod 请求方法
     * @param params     请求参数
     * @param headers    请求头
     * @return 响应信息
     */
    public T request(String url, HttpMethod httpMethod, Map<String, Object> params, HttpHeaders headers) {
        return this.url(url).method(httpMethod).params(params).headers(headers).request();
    }

    /**
     * rest请求执行器，接收一个消费者函数
     *
     * @param consumer 消费者函数，会返回请求后的Json数据
     */
    public void execute(Consumer<T> consumer) {
        try {
            if (isNull(consumer)) {
                throw new BusinessException(400, "消费者函数[consumer]不能为空！");
            }
            if (isEmpty(this.url)) {
                throw new BusinessException(400, "请求的url不能为空！");
            }
            //初始化方法
            initialization();
            //执行restTemplate请求，并解析结果
            T result = analysis(exchange());
            //执行消费函数
            consumer.accept(result);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            //如果异常消费函数为空，执行异常
            if (isNull(exception)) {
                throw e;
            }
            //执行异常消费函数
            exception.accept(e);
        }
    }

    //////////////////////////////// 对外设置方法，返回Rest本身 ///////////////////////////////////////////

    /**
     * 设置请求url
     */
    public Rest<T> url(String url) {
        if (isNotEmpty(url)) {
            this.url = url;
        }
        return this;
    }

    /**
     * 设置请求方法
     */
    public Rest<T> method(HttpMethod method) {
        if (isNotNull(method)) {
            this.method = method;
        }
        return this;
    }

    /**
     * 设置请求参数
     */
    public Rest<T> paramType(ParamType paramType) {
        if (isNotNull(paramType)) {
            this.paramType = paramType;
        }
        return this;
    }

    /**
     * 设置请求参数
     */
    public Rest<T> params(Map<String, Object> params) {
        if (isNotNull(params)) {
            this.params = params;
        }
        return this;
    }

    /**
     * 设置请求参数，接收一个消费者函数
     */
    public Rest<T> params(Consumer<Map<String, Object>> consumer) {
        if (isNotNull(consumer)) {
            if (isNull(this.params)) {
                this.params = Maps.newHashMap();
            }
            consumer.accept(this.params);
        }
        return this;
    }

    /**
     * 设置请求头
     */
    public Rest<T> headers(HttpHeaders headers) {
        if (isNotNull(headers)) {
            this.headers = headers;
        }
        return this;
    }

    /**
     * 设置请求头，接收一个消费者函数
     */
    public Rest<T> headers(Consumer<HttpHeaders> consumer) {
        if (isNotNull(consumer)) {
            initHttpHeaders();
            consumer.accept(this.headers);
        }
        return this;
    }

    /**
     * 设置contentType
     */
    public Rest<T> contentType(MediaType type) {
        return headers(headers -> headers.setContentType(type));
    }

    /**
     * 设置bearerAuth
     */
    public Rest<T> bearerAuth(String token) {
        return headers(headers -> headers.setBearerAuth(token));
    }

    /**
     * 设置转换器
     */
    public Rest<T> converter(Function<JSONObject, T> function) {
        if (isNotNull(function)) {
            this.converter = function;
        }
        return this;
    }

    /**
     * 设置异常消费者
     */
    public Rest<T> exception(Consumer<BusinessException> consumer) {
        if (isNotNull(consumer)) {
            this.exception = consumer;
        }
        return this;
    }

    /**
     * 设置响应类型
     */
    public Rest<T> responseType(Class<T> responseType) {
        if (isNotNull(responseType)) {
            this.responseType = responseType;
        }
        return this;
    }

    /**
     * 设置RestTemplate对象
     */
    public Rest<T> restTemplate(RestTemplate restTemplate) {
        if (isNotNull(restTemplate)) {
            this.restTemplate = restTemplate;
        }
        return this;
    }

    //////////////////////////////// 私有方法，初始化请求信息 ///////////////////////////////////////////

    /**
     * 初始化请求信息
     */
    private void initialization() {
        //默认请求方法为GET
        initHttpMethod();
        //初始化restTemplate请求对象
        initRestTemplate();
        //初始化参数类型
        initParamType();
        //初始化参数
        initParams();
    }

    /**
     * 默认请求方法为GET
     */
    private void initHttpMethod() {
        if (isNotNull(this.method)) {
            return;
        }
        this.method = HttpMethod.GET;
    }

    /**
     * 初始化请求头
     */
    private void initHttpHeaders() {
        if (isNotNull(this.headers)) {
            return;
        }
        this.headers = new HttpHeaders();
    }

    /**
     * 初始化RestTemplate对象
     */
    private void initRestTemplate() {
        //1. 优先取设置的restTemplate
        if (isNotNull(this.restTemplate)) {
            return;
        }
        //2. 其次从Spring容器中获取restTemplate
        this.restTemplate = SpringUtil.getBean(RestTemplate.class);
        if (log.isDebugEnabled()) {
            log.debug("---->> spring容器中获取restTemplate:{}", this.restTemplate);
        }
        if (isNotNull(this.restTemplate)) {
            return;
        }
        //3. 构造默认的restTemplate
        this.restTemplate = new RestTemplate();
        if (log.isDebugEnabled()) {
            log.debug("---->> 构造默认的restTemplate:{}", this.restTemplate);
        }
    }

    /**
     * 初始化参数类型，只处理参数类型为空的情况
     */
    private void initParamType() {
        if (isNotNull(this.paramType)) {
            return;
        }
        //如果请求类型是GET，参数类型为url参数
        if (equal(this.method, HttpMethod.GET)) {
            this.paramType = ParamType.URL;
            return;
        }
        //如果请求头不为空
        if (isNotNull(this.headers)) {
            //判断是否JSON
            if (equal(this.headers.getContentType(), MediaType.APPLICATION_JSON)) {
                this.paramType = ParamType.JSON;
                return;
            }
        }
        //其他情况，默认为body参数
        this.paramType = ParamType.BODY;
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        //如果参数为空，不处理
        if (isEmpty(this.params)) {
            return;
        }
        //如果参数类型是URL
        if (equal(this.paramType, ParamType.URL)) {
            initUrlParams();
            return;
        }
        //如果参数类型是JSON
        if (equal(this.paramType, ParamType.JSON)) {
            this.params.forEach(jsonParams::put);
            return;
        }
        //其他情况
        this.params.forEach(bodyParams::add);
    }

    /**
     * 初始化url参数
     */
    private void initUrlParams() {
        //添加 ?
        if (!this.url.contains(QUESTION)) {
            this.url = this.url.concat(QUESTION);
        }
        //添加 &
        if (!this.url.endsWith(QUESTION) && !this.url.endsWith(AND)) {
            this.url = this.url.concat(AND);
        }
        final StringBuffer buffer = new StringBuffer();
        //循环添加参数
        this.params.forEach((key, value) -> buffer.append(key).append("=").append(value).append(AND));
        //删除末
        this.url = this.url.concat(buffer.toString().substring(0, buffer.length() - 1));
    }

    //////////////////////////////// 私有方法，restTemplate请求处理 ///////////////////////////////////////////

    /**
     * 执行rest请求
     */
    private T request() {
        final List<T> list = Lists.newArrayList();
        execute(list::add);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 解析结果
     *
     * @param json 响应的json
     * @return T
     */
    private T analysis(JSONObject json) {
        //1. 优先执行转换函数的处理
        if (isNotNull(this.converter)) {
            return this.converter.apply(json);
        }
        //2. 未指定转换函数的情况下，需要校验是否设置了响应类型
        if (isNull(this.responseType)) {
            throw new BusinessException(400, "未指定转换函数的情况下，必须设置响应的数据类型[responseType]");
        }
        return json.toJavaObject(this.responseType);
    }

    /**
     * 执行restTemplate请求
     */
    private JSONObject exchange() throws BusinessException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("----- >> restTemplate request, url = {}, method = {}, params = {}", this.url, this.method.name(), JSON.toJSONString(this.params));
                log.debug("----- >> restTemplate request, headers = {}", JSON.toJSONString(this.headers));
            }
            ResponseEntity<JSONObject> exchange = restTemplate.exchange(this.url, this.method, buildEntity(), JSONObject.class);
            if (log.isDebugEnabled()) {
                log.debug("===== >> restTemplate response, exchange = {}", JSON.toJSONString(exchange));
            }
            if (equal(exchange.getStatusCode(), HttpStatus.OK)) {
                return exchange.getBody();
            }
            throw new BusinessException(exchange.getStatusCodeValue(), exchange.getStatusCode().getReasonPhrase());
        } catch (Exception e) {
            int code = (e instanceof BusinessException) ? ((BusinessException) e).getCode() : 400;
            log.error("===== >> restTemplate请求异常:{}", e.getMessage());
            log.error("===== >> restTemplate request, url = {}, method = {}, params = {}", this.url, this.method.name(), JSON.toJSONString(this.params));
            log.error("===== >> restTemplate request, headers = {}", JSON.toJSONString(this.headers));
            throw new BusinessException(code, "restTemplate请求异常:" + e.getMessage(), e);
        }
    }

    /**
     * 构建HttpEntity对象
     */
    private HttpEntity<Object> buildEntity() {
        //优先body参数
        if (!this.bodyParams.isEmpty()) {
            return new HttpEntity<>(this.bodyParams, this.headers);
        }
        if (!this.jsonParams.isEmpty()) {
            return new HttpEntity<>(this.jsonParams, this.headers);
        }
        return new HttpEntity<>(this.headers);
    }

    //////////////////////////////// 私有参数 ///////////////////////////////////////////

    /**
     * 返回值类型
     */
    private Class<T> responseType;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求头
     */
    private HttpHeaders headers;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * 参数类型
     */
    private ParamType paramType;

    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * Spring RestTemplate 对象
     */
    private RestTemplate restTemplate;

    /**
     * bodyParams
     */
    private final MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();

    /**
     * jsonParams
     */
    private final JSONObject jsonParams = new JSONObject();

    /**
     * 转换消费函数
     */
    private Function<JSONObject, T> converter;

    /**
     * 异常消费函数
     */
    private Consumer<BusinessException> exception;
}
