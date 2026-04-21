package com.uneed.common.support.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 获取spring bean的工具类.
 *
 * @author diablo
 * @date 2019/12/17
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {

    /**
     * spring 上下文对象
     */
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (isNull(SpringUtil.context)) {
            SpringUtil.context = context;
        }
        log.info("========ApplicationContext配置成功,在普通类可以通过调用SpringUtil.getAppContext()获取applicationContext对象");
    }

    /**
     * 获取applicationContext
     *
     * @return ApplicationContext
     */
    @SuppressWarnings("unused")
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 根据名字获取spring容器注入bean对象
     *
     * @param name 注入容器的bean名称
     * @return Object spring容器的bean对象
     */
    public static Object getBean(String name) {
        try {
            return isNotNull(context) ? context.getBean(name) : null;
        } catch (BeansException e) {
            log.warn("spring 获取bean异常！参数：name=" + name + ", 错误信息：" + e.getMessage());
        }
        return null;
    }

    /**
     * 根据名字获取spring容器注入bean对象，返回bean的类型由传染的clazz决定
     *
     * @param name  注入容器的bean名称
     * @param clazz 返回bean的类型
     * @param <T>   泛型参数
     * @return T spring容器的bean对象
     */
    @SuppressWarnings("unused")
    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return isNotNull(context) ? context.getBean(name, clazz) : null;
        } catch (BeansException e) {
            log.warn("spring 获取bean异常！参数：name=" + name + ", clazz=" + clazz + ", 错误信息：" + e.getMessage());
        }
        return null;
    }

    /**
     * 根据class类型获取spring容器注入bean对象
     *
     * @param clazz java对象class类型
     * @param <T>   泛型参数
     * @return T spring容器的bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return isNotNull(context) ? context.getBean(clazz) : null;
        } catch (BeansException e) {
            log.warn("spring 获取bean异常！参数：clazz=" + clazz + ", 错误信息：" + e.getMessage());
        }
        return null;
    }
}
