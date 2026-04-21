package cn.edu.cuhk.mkt.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * oauth2 配置
 *
 **/
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private String logoutUrl = "/logout";
    private String logoutSuccessUrl = "https://sts.cuhk.edu.cn/adfs/ls/?wa=wsignout1.0";
    private String jSessionId = "JSESSIONID";

    private String[] antMatchers = {
            "/csrf",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/doc.html",
            "/v2/api-docs/**",
            "/login.html",
            "/favicon.ico",
            "/error",
            "/webjars/**",
            "/auth/login/**",
            "/common/health/check",
            "/common/oss/download/**",
            "/common/refresh-user-cache",
            "/user",
            "/mkt/auth/login/pc",
            "/"
    };

    private String[] ignoringAntMatchers = {
            "/client/**",
            "/service/**",
            "/jwt/**",
            "/logout",
            "/mkt/auth/login/pc",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(antMatchers).permitAll().anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and().csrf().ignoringAntMatchers(ignoringAntMatchers)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().logout()
                .logoutUrl(logoutUrl)
                .invalidateHttpSession(true).clearAuthentication(true).deleteCookies(jSessionId)
                .logoutSuccessUrl(logoutSuccessUrl).permitAll()
                .and().oauth2Login();
        http.addFilterBefore(new ForwardedHeaderFilter(), OAuth2AuthorizationRequestRedirectFilter.class);
    }


}
