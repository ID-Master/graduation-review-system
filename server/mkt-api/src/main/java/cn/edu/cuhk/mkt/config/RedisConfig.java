package cn.edu.cuhk.mkt.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.TimeZone;

/**
 * redis配置
 *
 * @Dependson注解是在另外一个实例创建之后才创建当前实例，也就是，最终两个实例都会创建，只是顺序不一样
 *
 * @ConditionalOnBean注解是只有当另外一个实例存在时，才创建，否则不创建，也就是，最终有可能两个实例都创建了，有可能只创建了一个实例，也有可能一个实例都没创建
 *
 * @author taokai
 */
@Configuration
public class RedisConfig {

    /**
     * redisTemplate 序列化使用自定义序列化类
     *
     * @param factory redis连接工厂类
     * @return RedisTemplate
     */
    @Bean
    @SuppressWarnings("ALL")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 设置key的序列化规则
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        // 设置value的序列化规则，使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        valueSerializer.setObjectMapper(mapper);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
