package cn.edu.cuhk.mkt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AD域认证配置
 * @author taokai
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "ad")
public class AdConfig {
    /**
     * 开启AD域认证：true
     * 关闭AD域认证：false
     */
    private static Boolean enabled;
    /**
     * host或ip
     */
    private static String host;
    /**
     * 端口
     */
    private static String port;
    /**
     * 域
     */
    private static String domain;

    public void setEnabled(Boolean enabled) {
        AdConfig.enabled = enabled;
    }

    public void setHost(String host) {
        AdConfig.host = host;
    }

    public void setPort(String port) {
        AdConfig.port = port;
    }

    public void setDomain(String domain) {
        AdConfig.domain = domain;
    }

    public static Boolean getEnabled() {
        return enabled;
    }

    public static String getHost() {
        return host;
    }

    public static String getPort() {
        return port;
    }

    public static String getDomain() {
        return domain;
    }

}
