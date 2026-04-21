package cn.edu.cuhk.mkt.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.uneed.common.core.collection.map.Maps;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * json序列化配置，将Long类型转换为String类型.
 *
 * @author taokai
 */
@Configuration
public class JacksonSerializerConfig {
    /**
     * 序列化时，将long类型的数据转换为string类型
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Map<Class<?>, JsonSerializer<?>> map = Maps.newHashMap();
        map.put(Long.class, ToStringSerializer.instance);
        map.put(long.class, ToStringSerializer.instance);
        return builder -> builder.serializersByType(map);
    }
}
