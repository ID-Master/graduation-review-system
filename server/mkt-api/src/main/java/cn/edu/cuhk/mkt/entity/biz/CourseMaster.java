package cn.edu.cuhk.mkt.entity.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 业务模块-学生课程数据主表
 *
 * @author taok
 * @date 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_course_master")
public class CourseMaster extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 数据版本号
     */
    @TableField("version")
    private Long version;

    /**
     * 备注信息
     */
    @TableField("description")
    private String description;

    /**
     * 状态（0：草稿，1：待审核，2、已驳回，3、已完成,4、废弃的）
     */
    @TableField("status")
    private Integer status;

    /**
     * 申请人id（学生）
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 提交时间（学生）
     */
    @TableField("student_submit_time")
    private Date studentSubmitTime;

    /**
     * 学生复选框（0：不选中，1：选中）
     */
    @TableField("student_check_box")
    private Integer studentCheckBox;

    /**
     * 学生反馈
     */
    @TableField("student_check_feedback")
    private String studentCheckFeedback;

    /**
     * 文件id
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 学生签名图片url
     */
    @TableField("signature_url")
    private String signatureUrl;

    /**
     * 学生签字日期
     */
    @TableField("signature_date")
    private Date signatureDate;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private Date submitTime;

    /**
     * 审批人id（老师）
     */
    @TableField("teacher_id")
    private String teacherId;

    /**
     * 审批日期（老师）
     */
    @TableField("teacher_approve_time")
    private Date teacherApproveTime;

    /**
     * 老师复选框（0：不选中，1：选中）
     */
    @TableField("teacher_check_box")
    private Integer teacherCheckBox;

    /**
     * 打回原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 检查日期（老师）
     */
    @TableField("officer_checked_date")
    private Date officerCheckedDate;

    /**
     * 英文成绩单
     */
    @TableField("english_school_report")
    private String englishSchoolReport;

    /**
     * 课程子部分
     */
    @TableField("part")
    private String part;

    /**
     * 专业
     */
    @TableField("major")
    private String major;

    /**
     * 年级
     */
    @TableField("grade")
    private String grade;

    /**
     * 自我声明
     */
    @TableField("self_declaration")
    private String selfDeclaration;

}
