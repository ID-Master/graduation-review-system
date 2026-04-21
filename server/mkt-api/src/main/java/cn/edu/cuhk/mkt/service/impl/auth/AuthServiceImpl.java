package cn.edu.cuhk.mkt.service.impl.auth;

import cn.edu.cuhk.mkt.common.consts.SystemConst;
import cn.edu.cuhk.mkt.common.enums.SmsEnum;
import cn.edu.cuhk.mkt.common.enums.StatusEnum;
import cn.edu.cuhk.mkt.common.enums.UserEnum;
import cn.edu.cuhk.mkt.common.util.*;
import cn.edu.cuhk.mkt.config.AdConfig;
import cn.edu.cuhk.mkt.entity.auth.DisguiseAccountDTO;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.param.auth.LoginParam;
import cn.edu.cuhk.mkt.param.auth.SmsParam;
import cn.edu.cuhk.mkt.service.auth.AuthService;
import cn.edu.cuhk.mkt.service.biz.CourseMasterService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.core.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 登录认证服务
 * @author taokai
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseMasterService courseMasterService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录认证（PC端）
     *
     * @param loginParam 账号、密码
     * @return 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo loginPC(LoginParam loginParam) {
        // 数据校验
        AssertUtil.isBlank(loginParam.getLoginName(), "账号不能为空");
        // 是否管理员
        boolean isAdmin = false;
        // 开启AD域认证：true，关闭AD域认证：false
        Boolean enabled = AdConfig.getEnabled();
        // 用户类型
        String userType = UserEnum.USER_TYPE.SYSTEM.getCode();
        // 判断是否伪装登录
        DisguiseAccountDTO disguiseAccount = disguiseLogin(loginParam);
        // 用户信息
        User user = null;
        // 用户登录信息
        UserInfo userInfo = new UserInfo();
        // 伪装登录
        if(disguiseAccount.getIsDisguise()){
            user = disguiseAccount.getAgentUser();
            // 真实账号（被代理AD域账号）用户类型
            userType = disguiseAccount.getAgentUser().getUserType();
        }
        // 系统账号认证
        if(user == null){
            user = sysLogin(loginParam);
        }
        // AD域登录
        if (user == null && enabled.equals(Boolean.TRUE)){
            user = adLogin(loginParam);
            userType = user.getUserType();
        }

        AssertUtil.isNull(user, "系统繁忙，请稍后再试");

        // 账号状态
        Integer status = user.getStatus();
        if(status != null && user.getStatus().equals(StatusEnum.INVALID.getCode())){
            AssertUtil.isNull(null, "账号已被禁用，请联系管理员");
        }
        // 是否超级管理员
        if(user.getLoginName().equals(SystemConst.SYS_ADMIN_NAME)){
            isAdmin = true;
        }
        BeanUtil.copy(user, userInfo);
        // 将用户数据转成json格式存储到session
        String userInfoJson = JSON.toJSONString(userInfo);
        SessionUtil.setSessionAttribute(UserSession.SESSION_KEY_USER_INFO, userInfoJson);
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo loginAdfs(LoginParam loginParam){
        // 设置邮箱
        String studentId = StringUtils.defaultString(loginParam.getStudentId());
        String title = StringUtils.defaultString(loginParam.getTitle());
        String loginName = loginParam.getLoginName();
        String upn = loginParam.getUpn();
        String email = loginParam.getEmail();
        String uniqueName = loginParam.getUniqueName();
        AssertUtil.isBlank(upn, "账号不能为空");
        String userType = UserEnum.USER_TYPE.TEACHER.getCode();
        if(title.equalsIgnoreCase(UserEnum.USER_TYPE.STUDENT.getCode())){
            userType = UserEnum.USER_TYPE.STUDENT.getCode();
        }
        if((email == null || email == "") && userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
            email = loginName + "@link.cuhk.edu.cn";
        }else if((email == null || email == "") && userType.equals(UserEnum.USER_TYPE.TEACHER.getCode())){
            email = loginName + "@cuhk.edu.cn";
        }
        User user = userService.lambdaQuery()
                .eq(User::getLoginName, loginParam.getLoginName())
                .eq(User::getUserType, userType)
                .one();
        if(user == null){
            user = new User();
            user.setStudentId(loginName);
            user.setLoginName(loginName);
            user.setNameEn(uniqueName);
            user.setNameCh(uniqueName);
            user.setUserType(userType);
            //学生记录年级
            if(userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
                user.setGrade(UserUtil.getGrade(loginName));
            }
            user.setStatus(StatusEnum.EFFECTIVE.getCode());
            user.setEmail(email);
            userService.insert(user);
        }else {
            boolean updateFlag = false;
            if(StringUtils.isNotBlank(email)){
                user.setEmail(email);
                updateFlag = true;
            }
            if(StringUtils.isNotBlank(studentId)){
                user.setStudentId(studentId);
            }
            user.setNameEn(uniqueName);
            if(user.getNameCh().isEmpty()){
                user.setNameCh(uniqueName);
            }
            user.setStudentId(loginName);
            //学生记录年级
            if(userType.equals(UserEnum.USER_TYPE.STUDENT.getCode()) && user.getGrade().isEmpty()){
                user.setGrade(UserUtil.getGrade(loginName));
            }
            if(updateFlag){
                userService.update(user);
            }
        }
        // 学生课程是否可编辑
        Integer editable = courseMasterService.getStudentCourseEditable(null, user.getId(), null);
        // 用户登录信息
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setStudentId(user.getStudentId());
        userInfo.setLoginName(user.getLoginName());
        userInfo.setNameCh(user.getNameCh());
        userInfo.setNameEn(user.getNameEn());
        if(userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
            userInfo.setMajor(user.getMajor());
            userInfo.setMinor(user.getMinor());
            userInfo.setContactTel(user.getContactTel());
            userInfo.setExpectedYear(user.getExpectedYear());
            userInfo.setGrade(user.getGrade());
            userInfo.setInternationalStudent(user.getInternationalStudent());
        }
        userInfo.setUserType(userType);
        userInfo.setSuperAdmin(false);
        userInfo.setEditable(editable);
//        userInfo.setEmail(email);
        // 更新缓存
        String userInfoJson = JSON.toJSONString(userInfo);
        SessionUtil.setSessionAttribute(UserSession.SESSION_KEY_USER_INFO, userInfoJson);
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo studentSubmit(LoginParam loginParam) {
        // 身份校验
        UserUtil.mustStudent();
        // 参数校验
        AssertUtil.isBlank(loginParam.getMajor(), "必修课程不能为空");
        AssertUtil.isBlank(loginParam.getContactTel(), "手机号不能为空");
        AssertUtil.isBlank(loginParam.getExpectedYear(), "预计毕业年份不能为空");

        User user = userService.findByLoginName(loginParam.getLoginName());
        // 更新session
        UserInfo userInfo = UserSession.getUser();
        userInfo.setMajor(loginParam.getMajor());
        userInfo.setMinor(loginParam.getMinor());
        userInfo.setContactTel(loginParam.getContactTel());
        userInfo.setExpectedYear(loginParam.getExpectedYear());
        userInfo.setGrade(loginParam.getGrade());
        userInfo.setInternationalStudent(loginParam.getInternationalStudent());
        UserSession.setUser(userInfo);
        // 更新用户信息
//        User user = userService.getById(userInfo.getId());
        user.setMajor(userInfo.getMajor());
        user.setMinor(userInfo.getMinor());
        user.setContactTel(userInfo.getContactTel());
        user.setExpectedYear(userInfo.getExpectedYear());
        user.setGrade(userInfo.getGrade());
        user.setInternationalStudent(userInfo.getInternationalStudent());
        userService.update(user);
        return userInfo;
    }

    @Override
    public String sendSms(SmsParam smsParam) {
        // 获取当前登录用户信息
        UserInfo userInfo = UserSession.getUser();
        if(userInfo == null || StringUtils.isBlank(userInfo.getId())){
            AssertUtil.isNull(null, "登录验证未通过");
        }
        String userType = StringUtils.defaultString(userInfo.getUserType());
        if(!userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
            AssertUtil.isNull(null, "非学生身份，不允许获取短信");
        }

        String areaCode = StringUtils.defaultString(smsParam.getAreaCode());
        String phone = StringUtils.defaultString(smsParam.getPhone());
        String bizScope = StringUtils.defaultString(smsParam.getBizScope());
        Optional<SmsEnum.BIZ_SCOPE> bizScopeEnum = EnumSet.allOf(SmsEnum.BIZ_SCOPE.class)
                .stream()
                .filter(e -> e.getCode().equals(bizScope))
                .findFirst();
        if(!bizScopeEnum.isPresent()){
            AssertUtil.isNull(null, "业务域不存在");
        }

        // key = 业务域 + 区号 + 手机号
        StringBuilder redisKey = new StringBuilder();
        redisKey.append(bizScope)
                .append(areaCode)
                .append(phone);
        // 缓存短信验证码key
        String smsKey = SmsUtil.generateSmsKey(redisKey.toString());

        String cacheSmsCode = redisTemplate.opsForValue().get(smsKey);
        if(StringUtils.isNotBlank(cacheSmsCode)){
            AssertUtil.isNull(null, "请勿频繁获取短信验证码");
        }
        // 6位随机码（验证码）
        String randomSmsCode = Integer.toString(RandomUtil.randomInt(100000, 999999));
        redisTemplate.opsForValue().set(smsKey, randomSmsCode, 60, TimeUnit.SECONDS);
        // TODO: 调用短信发送接口
        log.info("短信验证码: {}, {}", smsKey, randomSmsCode);

        // 更新session用户信息
        //userInfo.setAreaCode(areaCode);
        //userInfo.setPhone(phone);
        // 将用户数据转成json格式存储到session
        String userInfoJson = JSON.toJSONString(userInfo);
        SessionUtil.setSessionAttribute(UserSession.SESSION_KEY_USER_INFO, userInfoJson);
        return randomSmsCode;
    }

    @Override
    public Boolean verifyCaptcha(String captcha) {
        // 获取当前登录用户信息
        UserInfo userInfo = UserSession.getUser();
        if(userInfo == null || StringUtils.isBlank(userInfo.getId())){
            AssertUtil.isNull(null, "登录验证未通过");
        }
        String userType = StringUtils.defaultString(userInfo.getUserType());
        if(!userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
            AssertUtil.isNull(null, "非学生身份，不允许获取短信");
        }

        // key = 业务域 + 区号 + 手机号
        StringBuilder redisKey = new StringBuilder();
        redisKey.append(SmsEnum.BIZ_SCOPE.AD_CAPTCHA.getCode())
                //.append(userInfo.getAreaCode())
                //.append(userInfo.getPhone())
        ;
        // 缓存短信验证码key
        String smsKey = SmsUtil.generateSmsKey(redisKey.toString());

        // 从redis缓存中获取验证码
        String redisCaptcha = redisTemplate.opsForValue().get(smsKey);
        if(StringUtils.isBlank(redisCaptcha)){
            AssertUtil.isNull(null, "短信验证码已失效，请重新获取");
        }
        else if(captcha.trim().equals(redisCaptcha)){
            // 更新session用户验证码状态
            userInfo.setSmsAuth(true);
            // 将用户数据转成json格式存储到session
            String userInfoJson = JSON.toJSONString(userInfo);
            SessionUtil.setSessionAttribute(UserSession.SESSION_KEY_USER_INFO, userInfoJson);
            // 删除redis用户验证码信息
            redisTemplate.delete(smsKey);
            return true;
        }
        else {
            AssertUtil.isNull(null, "短信验证码错误");
        }
        return false;
    }

    /**
     * 伪装登录
     *
     * @param loginParam 登录参数
     * @return 伪装登录账号信息
     */
    private DisguiseAccountDTO disguiseLogin(LoginParam loginParam){
        DisguiseAccountDTO disguiseAccount = new DisguiseAccountDTO();
        // 账号
        String loginName = loginParam.getLoginName();
        // 密码
        String password = loginParam.getPassword();
        if(loginName.indexOf(SystemConst.DISGUISE_LOGIN_SYMBOL) <= 0){
            disguiseAccount.setIsDisguise(false);
            return disguiseAccount;
        }
        String[] accounts = loginName.split(SystemConst.DISGUISE_LOGIN_SYMBOL);
        // 伪装账号
        String disguiseName = accounts[0];
        // 真实账号（被代理账号）
        String agentName = accounts[1];

        // 伪装账号
        User disguiseUser = userService.lambdaQuery()
                .eq(User::getLoginName, disguiseName)
                .eq(User::getUserType, UserEnum.USER_TYPE.SYSTEM.getCode())
                .one();
        AssertUtil.isNull(disguiseUser, "伪装账号不存在");
        // 解密
        String decryptPwd = JasyptUtil.decryptPbeWithMD5AndDES(JasyptUtil.SECRET_KEY, disguiseUser.getPassword());
        // 验证伪装登录密码
        AssertUtil.notEquals(decryptPwd, password, "伪装账号秘密错误");

        // 真实账号（被代理AD域账号）
        LambdaQueryChainWrapper<User> agentQueryWrapper = userService.lambdaQuery();
        agentQueryWrapper.eq(User::getLoginName, agentName);
        agentQueryWrapper.and(
                wrapper -> wrapper.eq(User::getUserType, UserEnum.USER_TYPE.STUDENT.getCode())
                        .or().eq(User::getUserType, UserEnum.USER_TYPE.TEACHER.getCode())
        );
        User agentUser = agentQueryWrapper.one();
        // 验证真实账号（被代理AD域账号）
        AssertUtil.isNull(agentUser, "真实账号不存在");

        disguiseAccount.setIsDisguise(true);
        disguiseAccount.setDisguiseUser(disguiseUser);
        disguiseAccount.setAgentUser(agentUser);
        return disguiseAccount;
    }

    /**
     * AD域登录认证
     * @param loginParam
     * @return
     */
    private User adLogin(LoginParam loginParam){
        // TODO: 获取账号身份信息（学生，老师）
        String userType = "";
        // 账号
        String loginName = loginParam.getLoginName();
        // 密码
        String password = loginParam.getPassword();
        // 当前环境
        String activeProfile = StringUtils.defaultString(SpringContextUtil.getActiveProfile());
        List<String> activeProfileList = new ArrayList();
        activeProfileList.add(SystemConst.ENV_LOCAL);
        activeProfileList.add(SystemConst.ENV_DEV);

        // 模拟AD账号身份
        String simulated = StringUtils.defaultString(loginParam.getSimulated());
        List<String> simulatedList = new ArrayList<>();
        simulatedList.add(UserEnum.USER_TYPE.STUDENT.getCode());
        simulatedList.add(UserEnum.USER_TYPE.TEACHER.getCode());

        // 只允许本地环境，测试环境模拟AD身份
        if(simulatedList.contains(simulated) && activeProfileList.contains(activeProfile)){
            userType = simulated;
        }
        // AD域认证
        else {
            Boolean adAuth = AdUtil.checkAdLogin(loginName, password);
            // AD登录认证失败
            if(adAuth.equals(Boolean.FALSE)){
                AssertUtil.isNull(null, "账号或密码错误");
            }
        }
        // 加密
        String encryptPwd = JasyptUtil.encryptPbeWithMD5AndDES(JasyptUtil.SECRET_KEY, password);
        // 验证真实账号（被代理AD域账号）
        LambdaQueryChainWrapper<User> userQueryWrapper = userService.lambdaQuery();
        userQueryWrapper.eq(User::getLoginName, loginName);
        userQueryWrapper.and(
                wrapper -> wrapper.eq(User::getUserType, UserEnum.USER_TYPE.STUDENT.getCode())
                        .or().eq(User::getUserType, UserEnum.USER_TYPE.TEACHER.getCode())
        );
        User user = userQueryWrapper.one();
        // 新增AD域用户数据
        if(user == null){
            user = new User();
            user.setLoginName(loginName);
            user.setPassword(encryptPwd);
            user.setStatus(StatusEnum.EFFECTIVE.getCode());
            // 学生
            if(userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
                user.setNameCh("");
                user.setNameEn("");
                user.setMajor("");
                user.setUserType(UserEnum.USER_TYPE.STUDENT.getCode());
                user.setMinor(loginParam.getMinor());
                user.setExpectedYear(loginParam.getExpectedYear());
                user.setAreaCode(loginParam.getAreaCode());
                user.setContactTel(loginParam.getContactTel());
            }
            // 老师
            else if(userType.equals(UserEnum.USER_TYPE.TEACHER.getCode())){
                user.setUserType(UserEnum.USER_TYPE.TEACHER.getCode());
            }else {
                AssertUtil.isNull(null, "身份识别失败");
            }
            userService.insert(user);
        }
        // 修改AD域用户数据
        else {
            user.setPassword(encryptPwd);
            // 学生
            if(userType.equals(UserEnum.USER_TYPE.STUDENT.getCode())){
                user.setMinor(loginParam.getMinor());
                user.setExpectedYear(loginParam.getExpectedYear());
                user.setAreaCode(loginParam.getAreaCode());
                user.setContactTel(loginParam.getContactTel());
            }
            // 老师
            else if(userType.equals(UserEnum.USER_TYPE.TEACHER.getCode())){

            }
            else {
                AssertUtil.isNull(null, "身份识别失败");
            }
            userService.update(user);
        }
        return user;
    }


    /**
     * 系统账号认证
     * @param loginParam 登录参数
     * @return 用户信息
     */
    private User sysLogin(LoginParam loginParam){
        User user = null;
        // 模拟AD账号身份
        String simulated = StringUtils.defaultString(loginParam.getSimulated());
        List<String> simulatedList = new ArrayList<>();
        simulatedList.add(UserEnum.USER_TYPE.STUDENT.getCode());
        simulatedList.add(UserEnum.USER_TYPE.TEACHER.getCode());
        if(!simulatedList.contains(simulated)){
            user = userService.lambdaQuery()
                    .eq(User::getLoginName, loginParam.getLoginName())
                    // .eq(User::getUserType, UserEnum.USER_TYPE.SYSTEM.getCode())
                    .one();
            AssertUtil.isNull(user, "账号不存在");
            // 解密
            String decryptPwd = JasyptUtil.decryptPbeWithMD5AndDES(JasyptUtil.SECRET_KEY, user.getPassword());
            AssertUtil.notEquals(decryptPwd, loginParam.getPassword(), "秘密错误");
        }
        return user;
    }

}