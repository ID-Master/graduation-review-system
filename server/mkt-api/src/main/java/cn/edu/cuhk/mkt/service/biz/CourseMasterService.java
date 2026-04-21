package cn.edu.cuhk.mkt.service.biz;

import cn.edu.cuhk.mkt.entity.biz.*;
import cn.edu.cuhk.mkt.entity.report.MktCourseReport;
import cn.edu.cuhk.mkt.param.CommonParam;
import cn.edu.cuhk.mkt.param.biz.CourseMasterParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.mybatis.base.SuperService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 业务模块-学生课程数据主表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseMasterService extends SuperService<CourseMaster> {

    /**
     * 新增课程数据
     * @param courseMasterVO
     * @return
     */
    int next(CourseMasterVO courseMasterVO);

    /**
     * 提交课程数据
     * @param courseMasterVO
     * @return
     */
    int submit(CourseMasterVO courseMasterVO);
    int checkSubmit(CourseMasterVO courseMasterVO);

    /**
     * 老师查询学生课程分页数据
     * @param page
     * @param condition
     * @return
     */
    Page<StudentCourseVO> listCourse(Page<StudentCourseVO> page, CourseMasterParam condition);

    /**
     * 老师审批课程数据
     * @param courseMasterVO
     * @return
     */
    int approve(CourseMasterVO courseMasterVO);

    int update(CourseMasterVO courseMasterVO);

    int acknowledged(CourseMasterVO courseMasterVO);

    /**
     * 导出MKT报表
     * @param param 导出参数
     * @return
     */
    List<MktCourseReport> listCourseMktReport(CommonParam param);

    /**
     * 课程是否可编辑（0：不可编辑，1：可编辑）
     *
     * @param courseMaster 课程主数据
     * @param studentId 学生id
     * @param courseMasterId 主课程id
     * @return
     */
    int getStudentCourseEditable(CourseMaster courseMaster, String studentId, String courseMasterId);

    /**
     * 获取预计毕业学期
     * @return
     */
    Map<String,String> getExpectedList();

    void email(EmailRecordVO emailRecordVO);

    List<String> studentImport(MultipartFile file);

    Page<CourseExportVO> reportPage(Page<CourseExportVO> page, CourseExportVO condition);
    List<CourseExportVO> reportList(CourseExportVO vo);

    List<CourseMaster> getByIds(List<String> ids);

}