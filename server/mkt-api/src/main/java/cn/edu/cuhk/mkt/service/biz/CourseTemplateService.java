package cn.edu.cuhk.mkt.service.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseTemplate;
import cn.edu.cuhk.mkt.param.biz.CourseTemplateParam;
import com.uneed.common.mybatis.base.SuperService;

import java.util.List;

/**
 * 业务模块-课程模板表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseTemplateService extends SuperService<CourseTemplate> {

    /**
     * 通过条件获取课程模板数据
     * @param condition
     * @return
     */
    List<CourseTemplate> listCourseTemplateByCondition(CourseTemplateParam condition);

}