package cn.edu.cuhk.mkt.service.impl.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseTemplate;
import cn.edu.cuhk.mkt.mapper.biz.CourseTemplateMapper;
import cn.edu.cuhk.mkt.param.biz.CourseTemplateParam;
import cn.edu.cuhk.mkt.service.biz.CourseTemplateService;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务模块-课程模板表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class CourseTemplateServiceImpl extends SuperServiceImpl<CourseTemplateMapper, CourseTemplate> implements CourseTemplateService {

    @Override
    public List<CourseTemplate> listCourseTemplateByCondition(CourseTemplateParam condition) {
        return mapper.listCourseTemplateByCondition(condition);
    }

}
