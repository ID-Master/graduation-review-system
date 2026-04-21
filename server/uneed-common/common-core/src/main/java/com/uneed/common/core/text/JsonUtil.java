package com.uneed.common.core.text;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 17/12/31
 */
public final class JsonUtil {
    private JsonUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static String toJson(Object object) {
        //时间格式化配置
        SerializeConfig config = new SerializeConfig();
        config.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        //转换规则
        SerializerFeature[] features = {
                // 输出空置字段
                SerializerFeature.WriteMapNullValue,
                // list字段如果为null，输出为[]，而不是null
                SerializerFeature.WriteNullListAsEmpty,
                // 字符类型字段如果为null，输出为""，而不是null
                SerializerFeature.WriteNullStringAsEmpty
        };
        return JSON.toJSONString(object, config, features);
    }

    public static String toJsonSimple(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> toArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }
}
