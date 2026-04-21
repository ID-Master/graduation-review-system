package cn.edu.cuhk.mkt.entity.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 业务模块-学生课程数据列表 vo对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@ApiModel(value = "StudentCourseVO", description = "业务模块-学生课程数据列表 VO对象")
public class StudentCourseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @ApiModelProperty(value = "id主键", name = "id")
    private String id;

    /**
     * Graduation Audit Self-check Form
     */
    @ApiModelProperty(value = "Graduation Audit Self-check Form", name = "graduationForm")
    private String graduationForm;

    /**
     * Student I.D
     */
    @ApiModelProperty(value = "Student I.D", name = "studentId")
    private String studentId;

    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称", name = "nameEn")
    private String nameEn;

    /**
     * 中文名称
     */
    @ApiModelProperty(value = "中文名称", name = "nameCh")
    private String nameCh;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", name = "contactTel")
    private String contactTel;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", name = "status")
    private Integer status;

    /**
     * 状态名称
     */
    @ApiModelProperty(value = "状态名称", name = "statusText")
    private String statusText;

    /**
     * 提交时间（Submit Time）
     */
    @ApiModelProperty(value = "提交时间（Submit Time）", name = "submitTime")
    private String submitTime;

    /**
     * 学生复选框，本人本学期申报毕业（0：否，1：是）
     */
    @ApiModelProperty(value = "学生复选框，本人本学期申报毕业（0：否，1：是）", name = "studentCheckBox")
    private Integer studentCheckBox;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    @ApiModelProperty(value = "专业", name = "major")
    private String major;

    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;

}