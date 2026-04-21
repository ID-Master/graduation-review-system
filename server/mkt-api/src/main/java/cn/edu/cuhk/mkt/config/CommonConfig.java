package cn.edu.cuhk.mkt.config;

import com.uneed.common.dict.api.AreaRepertory;
import com.uneed.common.dict.api.DictRepertory;
import com.uneed.common.dict.impl.RedisAreaRepertory;
import com.uneed.common.dict.impl.RedisDictRepertory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wutao
 */
@Configuration
@Component
public class CommonConfig {
    /**
     * 项目请求路径前缀
     */
    private static String adminPath;

    /**
     * redis key prefix
     */
    private static String redisPrefix;

    public static String getRedisPrefix() {
        return redisPrefix;
    }

    @Value("${common.redisPrefix}")
    public void setRedisPrefix(String redisPrefix) {
        CommonConfig.redisPrefix = redisPrefix;
    }

    public static String getAdminPath() {
        return StringUtils.defaultString(adminPath, "");
    }

    @Value("${adminPath}")
    public void setAdminPath(String adminPath) {
        CommonConfig.adminPath = adminPath;
    }

    @Bean
    public DictRepertory dictRepertory(RedisTemplate redisTemplate) {
        return new RedisDictRepertory(redisTemplate);
    }

    @Bean
    public AreaRepertory areaRepertory(RedisTemplate redisTemplate) {
        return new RedisAreaRepertory(redisTemplate);
    }

}
