package cn.edu.cuhk.mkt.entity.biz;

import cn.edu.cuhk.mkt.entity.BaseVO;
import cn.edu.cuhk.mkt.entity.sys.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 业务模块-学生课程数据主表 vo对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@ApiModel(value = "CourseMasterVO", description = "业务模块-学生课程数据主表 VO对象")
public class CourseMasterVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @ApiModelProperty(value = "id主键", name = "id")
    private String id;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "GMT+8")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date teacherApproveTime;

    /**
     * 老师复选框（0：不选中，1：选中）
     */
    @ApiModelProperty(value = "学生复选框（0：不选中，1：选中）", name = "teacherCheckBox")
    private Integer teacherCheckBox;

    /**
     * 学生反馈
     */
    @ApiModelProperty(value = "学生反馈", name = "studentCheckFeedback")
    private  String studentCheckFeedback;

    /**
     * 打回原因
     */
    @ApiModelProperty(value = "打回原因", name = "reason")
    private String reason;

    /**
     * 检查日期（老师）
     */
    @ApiModelProperty(value = "检查日期（老师）", name = "officerCheckedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date officerCheckedDate;

    /**
     * 英文成绩单
     */
    @ApiModelProperty(value = "英文成绩单", name = "englishSchoolReport")
    private String englishSchoolReport;

    /**************************** 自定义字段 ****************************/
    /**
     * 课程明细数据
     */
    @ApiModelProperty(value = "课程明细数据", name = "courseDetailVOList")
    private List<CourseDetailVO> courseDetailVOList;

    /**
     * 学生数据
     */
    @ApiModelProperty(value = "学生数据", name = "studentVO")
    private UserVO studentVO;

    /**
     * 审核参数（同意：agree，驳回：reject）
     */
    @ApiModelProperty(value = "审核参数（同意：agree，驳回：reject）", name = "action")
    private String action;

    /**
     * 邮件对象
     */
    @ApiModelProperty(value = "邮件对象", name = "emailVO")
    private EmailRecordVO emailVO;

    /**
     * 课程是否可编辑（0：不可编辑，1：可编辑）
     */
    @ApiModelProperty(value = "课程是否可编辑（0：不可编辑，1：可编辑）", name = "editable")
    private Integer editable;

    /**
     * 课程分类集合
     */
    @ApiModelProperty(value = "课程分类集合", name = "courseCategoryVOList")
    private List<CourseCategoryVO> courseCategoryVOList;

    /**
     * ids主键
     */
    @ApiModelProperty(value = "ids主键", name = "ids")
    private List<String> ids;

    @ApiModelProperty(value = "课程子部分", name = "课程子部分")
    private String part;

    @ApiModelProperty(value = "专业", name = "专业")
    private String major;

    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;

    @ApiModelProperty(value = "自我声明", name = "selfDeclaration")
    private String selfDeclaration;

}