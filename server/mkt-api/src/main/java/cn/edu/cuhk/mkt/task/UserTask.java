package cn.edu.cuhk.mkt.task;

import cn.edu.cuhk.mkt.service.auth.AdService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用户定时任务
 *
 * @author taokai
 */
@Slf4j
@Component
public class UserTask {
    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    /**
     * 每6个小时刷新一次用户缓存
     */
    @Scheduled(cron = "0 0 */6 * * ?")
    public void refreshUserCache() {
        /// 每6个小时cron表达式: "0 0 */6 * * ?"
        log.info("-------------->定时任务：刷新用户缓存...");
        userService.refreshUserCache(false);
    }

    /**
     * 每周同步AD学生数据
     */
    /*@Scheduled(cron = "0 30 1 ? * SUN")
    public void syncAdStudent() {
        log.info("-------------->定时任务：每周同步AD学生数据...");
        adService.syncAdStudent();
    }*/

    /**
     * 每周同步AD老师数据
     */
    /*@Scheduled(cron = "0 30 2 ? * SUN")
    public void syncAdTeacher() {
        log.info("-------------->定时任务：每周同步AD老师数据...");
        adService.syncAdTeacher();
    }*/

}
