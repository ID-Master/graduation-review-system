package cn.edu.cuhk.mkt.controller.sys;

import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.InfoVO;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.param.auth.LoginParam;
import cn.edu.cuhk.mkt.service.auth.AuthService;
import cn.edu.cuhk.mkt.service.sys.InfoService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.alibaba.fastjson.JSON;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class AdfsController extends AbstractController {
    @Autowired
    private AuthService authService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private UserService userService;

    @RequestMapping("/mkt/info/get")
    public Result<Object> getUserInfo() {
        User user = userService.findByLoginName("studenttest01");
        UserInfo userInfo = new UserInfo();
        BeanUtil.copy(user, userInfo);
        UserSession.setUser(userInfo);
        return super.success(user);
    }

    @RequestMapping("/adfs/getInfo")
    public Result<Object> user(@AuthenticationPrincipal OAuth2User principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("AdfsController-获取user信息 authentication: {}", JSON.toJSON(authentication));
        log.info("AdfsController-获取user信息 principal :{}", JSON.toJSON(principal));

        LoginParam loginParam = new LoginParam();
        Map<String, Object> attributes = principal.getAttributes();
        String keyTitle = "title";
        String keyUniqueName = "unique_name";
        String keyUpn = "upn";
        String keyEmail = "email";

        if(attributes.get(keyTitle) != null){
            loginParam.setTitle(attributes.get(keyTitle).toString());
        }
        if(attributes.get(keyUniqueName) != null){
            loginParam.setUniqueName(attributes.get(keyUniqueName).toString());
        }
        if(attributes.get(keyUpn) != null){
            String upn = attributes.get(keyUpn).toString().toLowerCase();
            String loginName = upn.split("@")[0];
            loginParam.setStudentId(loginName);
            loginParam.setLoginName(loginName);
            loginParam.setUpn(upn);
        }
        if(attributes.get(keyEmail) != null){
            loginParam.setEmail(attributes.get(keyEmail).toString());
        }
        log.info("=========>loginParam: {}", JSON.toJSONString(loginParam));
        UserInfo userInfo = authService.loginAdfs(loginParam);
        String studentId = loginParam.getStudentId();
        String loginName = loginParam.getLoginName();
        String email = loginParam.getEmail();
        if(email == null){
            User user = userService.findByLoginName(loginName);
            if(user != null){ email = user.getEmail(); }
        }

        Map<String, Object> maps = Maps.newHashMap();
        maps.put("studentId", studentId);
        maps.put("name", studentId);
        maps.put("userType", userInfo.getUserType());
        maps.put("nameCh", userInfo.getNameCh());
        maps.put("nameEn", userInfo.getNameEn());
        maps.put("major", userInfo.getMajor());
        maps.put("minor", userInfo.getMinor());
        maps.put("contactTel", userInfo.getContactTel());
        maps.put("expectedYear", userInfo.getExpectedYear());
        maps.put("editable", userInfo.getEditable());
        maps.put("email", email);
        maps.put("grade", userInfo.getGrade());
        maps.put("internationalStudent", userInfo.getInternationalStudent());

        //是否管理员
        List<String> adminList = Lists.newArrayList("smeadtest@cuhk.edu.cn");
        maps.put("isAdmin", adminList.contains(email));
        //是否信任教师
        Boolean isTrustForTeacher = Boolean.FALSE;
        InfoVO vo = new InfoVO();
        Optional<Info> optionalInfo = infoService.list().stream().findFirst();
        if(optionalInfo.isPresent()){
            String teacherList = optionalInfo.get().getTeacherList();
            log.info("------------->" + teacherList);
            isTrustForTeacher = teacherList.contains(email);
        }
        maps.put("isTrustForTeacher", isTrustForTeacher);

        return super.success(maps);

    }

}
