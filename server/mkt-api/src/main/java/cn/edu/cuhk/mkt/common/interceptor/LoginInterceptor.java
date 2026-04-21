package cn.edu.cuhk.mkt.common.interceptor;

import cn.edu.cuhk.mkt.common.annotation.IgnoreUserToken;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.http.HttpCode;
import com.uneed.common.core.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 登录拦截器
 *
 * @author taokai
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 用户身份鉴权
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("----------------->>> request url: {}", request.getRequestURI());
        IgnoreUserToken annotation = null;
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //AnnotationUtils.findAnnotation()
            //AnnotatedElementUtils
            // 从请求方法上拦截忽略用户身份注解，首先从方法上获取
            annotation = handlerMethod.getMethodAnnotation(IgnoreUserToken.class);
            // 再从类上获取
            if (isNull(annotation)) {
                annotation = handlerMethod.getBeanType().getAnnotation(IgnoreUserToken.class);
            }
        }

        // 获取session
        HttpSession session = SessionUtil.getSession();
        // 放通接口
        if(isNotNull(annotation)){
            return true;
        }
        else if(isNull(session)){
            throw new BusinessException(HttpCode.TOKEN_EXPIRED.getCode(), HttpCode.TOKEN_EXPIRED.getMessage());
        }
        /*UserInfo userInfo = UserSession.getUser();
        String userType = userInfo.getUserType();
        Boolean smsAuth = userInfo.getSmsAuth();
        // 如果是学生登录，判断是否已通过短信验证码
        if(userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
            if(smsAuth.equals(Boolean.FALSE)){
                AssertUtil.isNull(null, "短信验证码未通过");
            }
        }*/

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}
