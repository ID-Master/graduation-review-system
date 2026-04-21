package cn.edu.cuhk.mkt.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger api 文档配置
 * http://localhost:10050/doc.html
 *
 * @author taokai
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * 定义分隔符,配置Swagger多包
     */
    private static final String SPLITOR = ";";

    @Bean
    public Docket createRestApi() {
        List<String> basePackageList = new ArrayList<>();
        basePackageList.add("cn.edu.cuhk.mkt");
        String basePackage = StringUtils.join(basePackageList, SPLITOR);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("swagger-cuhk-mkt-doc")
                //描述
                .description("港中大（MKT）学生自查系统")
                //这里配置的是服务网站
                .termsOfServiceUrl("http://www.u-need.cn")
                // 三个参数依次是姓名，个人网站，邮箱
                .contact(new Contact("diaoblo", "http://www.u-need.cn", "anding.huang@u-need.cn"))
                //版本
                .version("1.0").build();
    }

}
