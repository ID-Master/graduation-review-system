package cn.edu.cuhk.mkt.service.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseCategory;
import cn.edu.cuhk.mkt.entity.biz.CourseCategoryVO;
import cn.edu.cuhk.mkt.param.biz.CourseCategoryParam;

import com.uneed.common.mybatis.base.SuperService;

import java.util.List;

/**
 * 业务模块-课程分类表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseCategoryService extends SuperService<CourseCategory> {

    /**
     * 通过课程主表id获取课程分类列表
     * @return
     */
    List<CourseCategory> listByCourseMasterId( String courseMasterId);


    List<CourseCategory> listByMajor( String major);

    /**
     * 复制课程分类
     * @return Long
     * @return Integer
     */
    Integer copy(String id);

    /**
     * 复制课程分类
     * @param param
     * @return Integer
     */
    Integer copy(CourseCategoryParam param);

    /**
     * 查询当前学生课程分类
     * @param param
     * @return List<CourseCategoryVO>
     */
    List<CourseCategoryVO> getStudentCategoryList(CourseCategoryParam param);

}