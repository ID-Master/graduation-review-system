package com.uneed.common.support.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import java.math.BigInteger;

/**
 * fastJson序列化配置，继承FastJsonConfig
 *
 * @author diablo
 * @date 2018/5/12
 */
public class DefaultFastJsonConfig extends FastJsonConfig {

    public DefaultFastJsonConfig() {
        super();
        //处理长整形型丢失精度的问题.
        super.getSerializeConfig().put(Long.class, ToStringSerializer.instance);
        super.getSerializeConfig().put(Long.TYPE, ToStringSerializer.instance);
        super.getSerializeConfig().put(BigInteger.class, ToStringSerializer.instance);

        //设置特性
        super.setSerializerFeatures(
                //输出结果格式化
                //SerializerFeature.PrettyFormat,
                //输出空置字段
                SerializerFeature.WriteMapNullValue,
                //list字段如果为null，输出为[]，而不是null
                SerializerFeature.WriteNullListAsEmpty,
                //字符类型字段如果为null，输出为""，而不是null
                SerializerFeature.WriteNullStringAsEmpty,
                //日期格式化
                SerializerFeature.WriteDateUseDateFormat,
                //Enum输出name()或者original
                SerializerFeature.WriteEnumUsingToString,
                //禁用循环引用检测
                SerializerFeature.DisableCircularReferenceDetect
        );
    }
}
