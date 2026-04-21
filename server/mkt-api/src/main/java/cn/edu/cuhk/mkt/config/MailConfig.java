package cn.edu.cuhk.mkt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wutao
 */
@Configuration
@Component
public class MailConfig {
    private static String from;

    public static String getFrom() {
        return from;
    }

    @Value("${spring.mail.username}")
    public void setFrom(String from) {
        MailConfig.from = from;
    }
}
