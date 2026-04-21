package cn.edu.cuhk.mkt.init;

import cn.edu.cuhk.mkt.config.MajorConfig;
import cn.edu.cuhk.mkt.entity.ad.CourseMajorDTO;
import cn.edu.cuhk.mkt.entity.sys.Profiles;
import cn.edu.cuhk.mkt.service.sys.ProfilesService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化课程专业数据
 *
 * @author taokai
 */
@Slf4j
@DependsOn(value = {"commonConfig", "majorConfig"})
@Component
public class MajorInitialize implements InitializingBean {

    @Autowired
    private ProfilesService profilesService;

    @Autowired
    private MajorConfig majorConfig;

    @Override
    public void afterPropertiesSet() {
        log.info("-------------------- start 课程专业数据初始化 --------------------");
        try {
            int count = profilesService.lambdaQuery().eq(Profiles::getCode, "Major").count();
            if(count > 0){
                log.info("==========>课程专业数据已存在，无需初始化");
                return;
            }
            List<CourseMajorDTO> majors = majorConfig.getMajors();
            log.info("==========>初始化课程专业数据: {}", JSON.toJSONString(majors));
            profilesService.initMajors(majors);
        }catch (Exception e){
            log.error("!!!!!!!!!!!!!!! 课程专业数据初始化异常: {} !!!!!!!!!!!!!!!", e.getMessage(), e);
        }
        log.info("-------------------- end 课程专业数据初始化 --------------------");
    }

}
