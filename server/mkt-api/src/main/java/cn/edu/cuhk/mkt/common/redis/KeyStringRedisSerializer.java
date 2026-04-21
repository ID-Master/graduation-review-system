package cn.edu.cuhk.mkt.common.redis;

import cn.edu.cuhk.mkt.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author taokai
 */
@Slf4j
@DependsOn(value = {"commonConfig"})
public class KeyStringRedisSerializer implements RedisSerializer<String> {
    private final Charset charset;
    public static final StringRedisSerializer US_ASCII;
    public static final StringRedisSerializer ISO_8859_1;
    public static final StringRedisSerializer UTF_8;
    private final String REDIS_PREFIX = CommonConfig.getRedisPrefix()+":";

    public KeyStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public KeyStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset).replaceFirst(REDIS_PREFIX, ""));
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return (string == null ? null : (REDIS_PREFIX + string).getBytes(charset));
    }

    static {
        US_ASCII = new StringRedisSerializer(StandardCharsets.US_ASCII);
        ISO_8859_1 = new StringRedisSerializer(StandardCharsets.ISO_8859_1);
        UTF_8 = new StringRedisSerializer(StandardCharsets.UTF_8);
    }

}