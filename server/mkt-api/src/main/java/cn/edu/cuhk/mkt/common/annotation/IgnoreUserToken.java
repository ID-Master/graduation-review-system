package cn.edu.cuhk.mkt.common.annotation;

import java.lang.annotation.*;

/**
 * 忽略用户鉴权，可配置在方法或是类上
 * @author taokai
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface IgnoreUserToken {

}
