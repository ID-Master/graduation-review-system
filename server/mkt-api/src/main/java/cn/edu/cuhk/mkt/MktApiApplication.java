package cn.edu.cuhk.mkt;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author uneed
 */
@Slf4j
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@MapperScan("cn.edu.cuhk.mkt.mapper.**")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
// @SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class, scanBasePackages = {"cn.edu.cuhk.mkt.**","com.uneed.**"})
public class MktApiApplication {

	public static void main(String[] args) throws Exception {
		// SpringApplication.run(MktApiApplication.class, args);
		// log.info("********************** 服务启动成功 service startup completed **********************");
		ConfigurableApplicationContext application = SpringApplication.run(MktApiApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("adminPath");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application Spring-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
	}

}
