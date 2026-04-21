package cn.edu.cuhk.mkt.entity.biz;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务模块-学生课程数据主表 dto对象
 *
 * @author taok
 * @date 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseMasterDTO", description = "业务模块-学生课程数据主表 DTO对象")
public class CourseMasterDTO extends SuperModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 数据版本号
     */
    @ApiModelProperty(value = "数据版本号", name = "version")
    private Long version;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", name = "description")
    private String description;

    /**
     * 状态（0：草稿，1：待审核，2、已驳回，3、已完成）
     */
    @ApiModelProperty(value = "状态（0：草稿，1：待审核，2、已驳回，3、已完成,4、废弃的）", name = "status")
    private Integer status;

    /**
     * 申请人id（学生）
     */
    @ApiModelProperty(value = "申请人id（学生）", name = "studentId")
    private String studentId;

    /**
     * 提交时间（学生）
     */
    @ApiModelProperty(value = "提交时间（学生）", name = "studentSubmitTime")
    private Date studentSubmitTime;

    /**
     * 学生复选框（0：不选中，1：选中）
     */
    @ApiModelProperty(value = "学生复选框（0：不选中，1：选中）", name = "studentCheckBox")
    private Integer studentCheckBox;

    /**
     * 学生反馈
     */
    @ApiModelProperty(value = "学生反馈", name = "studentCheckFeedback")
    private  String studentCheckFeedback;

    /**
     * 文件id
     */
    @ApiModelProperty(value = "文件id", name = "fileId")
    private String fileId;

    /**
     * 学生签名图片url
     */
    @ApiModelProperty(value = "学生签名图片url", name = "signatureUrl")
    private String signatureUrl;

    /**
     * 学生签字日期
     */
    @ApiModelProperty(value = "学生签字日期", name = "signatureDate")
    private Date signatureDate;

    /**
     * 提交时间
     */
    @ApiModelProperty(value = "提交时间", name = "submitTime")
    private Date submitTime;

    /**
     * 审批人id（老师）
     */
    @ApiModelProperty(value = "审批人id（老师）", name = "teacherId")
    private String teacherId;

    /**
     * 审批日期（老师）
     */
    @ApiModelProperty(value = "审批日期（老师）", name = "teacherApproveTime")
    private Date teacherApproveTime;

    /**
     * 老师复选框（0：不选中，1：选中）
     */
    @ApiModelProperty(value = "学生复选框（0：不选中，1：选中）", name = "teacherCheckBox")
    private Integer teacherCheckBox;

    /**
     * 打回原因
     */
    @ApiModelProperty(value = "打回原因", name = "reason")
    private String reason;

    /**
     * 检查日期（老师）
     */
    @ApiModelProperty(value = "检查日期（老师）", name = "officerCheckedDate")
    private Date officerCheckedDate;

    /**
     * 英文成绩单
     */
    @ApiModelProperty(value = "英文成绩单", name = "englishSchoolReport")
    private String englishSchoolReport;

    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;
}