package cn.edu.cuhk.mkt.controller.sys;

import cn.edu.cuhk.mkt.common.annotation.IgnoreUserToken;
import cn.edu.cuhk.mkt.common.consts.SystemConst;
import cn.edu.cuhk.mkt.common.util.SpringContextUtil;
import cn.edu.cuhk.mkt.param.auth.LoginParam;
import cn.edu.cuhk.mkt.param.auth.SmsParam;
import cn.edu.cuhk.mkt.service.auth.AuthService;
import com.alibaba.fastjson.JSON;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.core.util.SessionUtil;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录认证接口
 * @author taokai
 */
@Slf4j
@Api(tags = {"身份认证中心"})
@RestController
@RequestMapping("${adminPath}/auth")
public class AuthController extends AbstractController {
    @Autowired
    private AuthService authService;

    /**
     * 账号登录（PC端）
     *
     * @param param 用户名、密码
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "账号登录（PC端）", notes = "登录接口（PC端）")
    @IgnoreUserToken
    @PostMapping(value = "/login/pc")
    public Result<Object> loginPc(@Valid @RequestBody @ApiParam(value = "登录参数（PC端）", required = true) LoginParam param) {
        log.info("---------------->pc login param: {}", JSON.toJSONString(param));
        UserInfo userInfo = authService.loginPC(param);
        return super.success(userInfo);
    }

    /**
     * 学生提交个人信息
     *
     * @return
     */
    @ApiOperation(value = "学生提交个人信息", notes = "学生提交个人信息")
    @PostMapping(value = "/student/submit")
    public Result<UserInfo> studentSubmit(@Valid @RequestBody @ApiParam(value = "学生提交个人信息", required = true) LoginParam param) {
        log.info("---------------->param: {}", JSON.toJSONString(param));
        UserInfo userInfo = authService.studentSubmit(param);
        return super.success(userInfo);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @ApiOperation(value = "获取账号信息（PC端）", notes = "获取用户信息（PC端）")
    @GetMapping(value = "/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        UserInfo userInfo = UserSession.getUser();
        return super.success(userInfo);
    }

    @ApiOperation(value = "根据手机号获取验证码", notes = "根据手机号获取验证码")
    @IgnoreUserToken
    @PostMapping(value = "/getSmsCode")
    public Result<Object> getSmsCode(@RequestBody @ApiParam(value = "短信验证码参数", required = true) SmsParam smsParam) {
        String randomSmsCode = authService.sendSms(smsParam);
        String activeProfile = StringUtils.defaultString(SpringContextUtil.getActiveProfile());
        List<String> activeProfileList = new ArrayList();
        activeProfileList.add(SystemConst.ENV_LOCAL);
        activeProfileList.add(SystemConst.ENV_DEV);
        // 非本地环境，测试环境，不返回验证信息
        if(!activeProfileList.contains(activeProfile)){
            randomSmsCode = "";
        }
        return super.success("短信验证码已发送到您的手机，请注意查收 "+randomSmsCode);
    }

    @ApiOperation(value = "二次验证-短信（PC端）", notes = "二次验证-短信（PC端）")
    @IgnoreUserToken
    @GetMapping(value = "/verifyCaptcha/{captcha}")
    public Result<Object> verifyCaptcha(@Valid @PathVariable @ApiParam(value = "验证码", required = true) String captcha) {
        log.info("---------------->pc sms captcha: {}", captcha);
        authService.verifyCaptcha(captcha);
        return super.success("短信验证码通过");
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @GetMapping(value = "/logout")
    public Result<Object> logout() {
        SessionUtil.getSession().invalidate();
        return super.success();
    }

}
