package cn.edu.cuhk.mkt.controller.common;

import cn.edu.cuhk.mkt.common.annotation.IgnoreUserToken;
import cn.edu.cuhk.mkt.common.consts.BizConst;
import cn.edu.cuhk.mkt.common.enums.BizCodeEnum;
import cn.edu.cuhk.mkt.entity.mail.MailDTO;
import cn.edu.cuhk.mkt.service.common.EmailService;
import cn.edu.cuhk.mkt.service.sys.BizCodeService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.uneed.common.support.api.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author taokai
 */
@Slf4j
@Api(tags = {"公共服务模块"})
@RestController
@RequestMapping("${adminPath}/common")
public class CommonController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BizCodeService bizCodeService;

    @ApiOperation(value = "服务健康检测")
    @IgnoreUserToken
    @GetMapping("/health/check")
    public Object check() {
        return Result.ok("I am OK!");
    }

    @ApiOperation(value = "强制刷新用户缓存")
    @IgnoreUserToken
    @GetMapping("/refresh-user-cache")
    public Object refreshUserCache() {
        userService.refreshUserCache(true);
        return Result.ok("用户缓存刷新完成");
    }

    @ApiOperation(value = "同步AD学生数据")
    @IgnoreUserToken
    @GetMapping("/sync-ad-student")
    public Object syncAdStudent() {
        /// adService.syncAdStudent();
        return Result.ok("同步AD学生数据完成");
    }

    @ApiOperation(value = "同步AD老师数据")
    @IgnoreUserToken
    @GetMapping("/sync-ad-teacher")
    public Object syncAdTeacher() {
        /// adService.syncAdTeacher();
        return Result.ok("同步AD老师数据完成");
    }

    @ApiOperation(value = "获取流水号", notes = "获取流水号")
    @GetMapping(value = {"/getSerialNumber", "/getSerialNumber/{code}"})
    public Result<Object> getSerialNumber(@PathVariable(value = "code") String code) {
        code = StringUtils.defaultString(code, BizCodeEnum.TEST.getCode());
        String prefixKey = BizConst.CACHE_BIZ_PREFIX_KEY + code;
        String serialNumber = bizCodeService.getSerialNumber(prefixKey);
        return Result.ok(serialNumber);
    }

    @ApiOperation(value = "测试发邮件", notes = "测试发邮件")
    @GetMapping(value = {"/send-email"})
    public Result<Object> sendEmail() {
        MailDTO mailDTO = new MailDTO();
        String[] tos = new String[]{"smeit04@cuhk.edu.cn"};
        mailDTO.setTos(tos);
        mailDTO.setSubject("测试邮件");
        String content = StringUtils.defaultString("您好，This is the pricelist, but it serves as a guide line only. Is there anything you are particularly interested in?");
        mailDTO.setContent(content);
        emailService.htmlEmail(mailDTO);
        return Result.ok("");
    }

}
