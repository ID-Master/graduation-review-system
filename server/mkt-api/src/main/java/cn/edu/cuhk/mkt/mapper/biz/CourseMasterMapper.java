package cn.edu.cuhk.mkt.mapper.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseExportVO;
import cn.edu.cuhk.mkt.entity.biz.CourseMaster;
import cn.edu.cuhk.mkt.entity.biz.StudentCourseVO;
import cn.edu.cuhk.mkt.entity.report.MktCourseReport;
import cn.edu.cuhk.mkt.param.CommonParam;
import cn.edu.cuhk.mkt.param.biz.CourseMasterParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务模块-学生课程数据主表 Mapper接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseMasterMapper extends SuperMapper<CourseMaster> {

    /**
     * 分页获取学生课程数据
     *
     * @param condition 查询条件
     * @return 返回课程分页数据
     */
    Page<StudentCourseVO> listCourseByCondition(Page<StudentCourseVO> page, @Param("condition") CourseMasterParam condition);

    /**
     * 导出MKT报表
     * @return
     */
    List<MktCourseReport> listCourseMktReport(@Param("param") CommonParam param);

    Page<CourseExportVO> reportPage(Page<CourseExportVO> page, @Param("condition") CourseExportVO condition);
    List<CourseExportVO> reportList(@Param("condition") CourseExportVO condition);

}