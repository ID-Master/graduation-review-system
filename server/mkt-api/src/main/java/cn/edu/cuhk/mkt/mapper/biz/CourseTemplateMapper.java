package cn.edu.cuhk.mkt.mapper.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseTemplate;
import cn.edu.cuhk.mkt.param.biz.CourseTemplateParam;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务模块-课程模板表 Mapper接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseTemplateMapper extends SuperMapper<CourseTemplate> {

    /**
     * 通过条件获取课程模板数据
     * @param condition
     * @return
     */
    List<CourseTemplate> listCourseTemplateByCondition(@Param(value = "condition") CourseTemplateParam condition);

}