package cn.edu.cuhk.mkt.init;

import cn.edu.cuhk.mkt.service.auth.AdService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 系统角色初始化
 *
 * @author taokai
 */
@Slf4j
@DependsOn(value = {"commonConfig"})
@Component
public class UserInitialize implements InitializingBean {

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @Override
    public void afterPropertiesSet() {
        // 初始化系统管理员账号
        initAdminUser();
        // 初始化AD学生账号
        //initAdStudentUser();
        // 初始化AD老师账号
        //initAdTeacherUser();
    }

    /**
     * 初始化系统管理员账号
     */
    private void initAdminUser(){
        log.info("-------------------- 系统用户初始化 start --------------------");
        try {
            userService.initAdminUser();
        }catch (Exception e){
            log.error("!!!!!!!!!!!!!!! 系统用户初始化异常: {} !!!!!!!!!!!!!!!", e.getMessage(), e);
        }
        log.info("-------------------- 系统用户初始化 end ----------------------");
    }

    /**
     * 初始化AD学生账号
     */
    private void initAdStudentUser(){
        try {
            adService.initAdStudent();
        }catch (Exception e){
            log.error("!!!!!!!!!!!!!!! 初始化AD学生账号异常: {} !!!!!!!!!!!!!!!", e.getMessage(), e);
        }
    }

    /**
     * 初始化AD老师账号
     */
    private void initAdTeacherUser(){
        try {
            adService.initAdTeacher();
        }catch (Exception e){
            log.error("!!!!!!!!!!!!!!! 初始化AD老师账号异常: {} !!!!!!!!!!!!!!!", e.getMessage(), e);
        }
    }

}
