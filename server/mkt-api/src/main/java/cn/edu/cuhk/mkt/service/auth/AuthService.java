package cn.edu.cuhk.mkt.service.auth;

import cn.edu.cuhk.mkt.param.auth.LoginParam;
import cn.edu.cuhk.mkt.param.auth.SmsParam;
import com.uneed.common.core.session.UserInfo;

/**
 * 登录认证服务
 *
 * @author taokai
 */
public interface AuthService {

    /**
     * 用户登录认证（PC端）
     *
     * @param loginParam 账号、密码
     * @return
     */
    UserInfo loginPC(LoginParam loginParam);

    /**
     * 登录 ADFS
     * @param loginParam
     * @return
     */
    UserInfo loginAdfs(LoginParam loginParam);

    /**
     * 学生提交个人信息
     * @param loginParam
     * @return
     */
    UserInfo studentSubmit(LoginParam loginParam);

    /**
     * 发送短信验证码
     *
     * @param smsParam 短信验证码对象
     * @return
     */
    String sendSms(SmsParam smsParam);

    /**
     * 短信码验证结果：成功：true，失败：false
     * @param captcha
     * @return
     */
    Boolean verifyCaptcha(String captcha);

}
