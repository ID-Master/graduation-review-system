package com.uneed.common.support.json;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 扩展fastjson的值过滤器
 *
 * @author diablo
 * @date 2018/5/12
 */
public class DefaultJsonValueFilter implements ValueFilter {

    /**
     * 处理json值过滤
     *
     * @param obj   json的值类型
     * @param key   json的key值
     * @param value json的value值
     * @return 返回修改后的值
     */
    @Override
    public Object process(Object obj, String key, Object value) {
        return value == null ? "" : value;
    }
}
