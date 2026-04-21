package cn.edu.cuhk.mkt.mapper.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseCategory;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务模块-课程分类表 Mapper接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseCategoryMapper extends SuperMapper<CourseCategory> {

    /**
     * 通过课程主表id获取课程分类列表
     * @return
     */
    List<CourseCategory> listByCourseMasterId(@Param(value = "courseMasterId") String courseMasterId);

    List<CourseCategory> listByMajor(@Param(value = "major") String major);

}