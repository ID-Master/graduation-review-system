package cn.edu.cuhk.mkt.entity.biz;

import cn.edu.cuhk.mkt.entity.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 业务模块-课程模板表 vo对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@ApiModel(value = "CourseTemplateVO", description = "业务模块-课程模板表 VO对象")
public class CourseTemplateVO extends BaseVO {

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
     * 课程分类id
     */
    @ApiModelProperty(value = "课程分类id", name = "courseCategoryId")
    @NotBlank(message = "课程分类id不能为空")
    private String courseCategoryId;

    /**
     * 课程编号
     */
    @ApiModelProperty(value = "课程编号", name = "courseCode")
    @NotBlank(message = "课程编号不能为空")
    private String courseCode;

    /**
     * 课程标题
     */
    @ApiModelProperty(value = "课程标题", name = "courseTitle")
    @NotBlank(message = "课程标题不能为空")
    private String courseTitle;

    /**
     * 课程子部分
     */
    @ApiModelProperty(value = "课程子部分", name = "part")
    private String part;

    /**
     * 评分
     */
    @ApiModelProperty(value = "评分", name = "units")
    private BigDecimal units;

    /**
     * 类型（1：学分，2：修读进度）
     */
    @ApiModelProperty(value = "类型", name = "type")
    private Integer type;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sortIndex")
    private Integer sortIndex;

    /**
     * 修读进度（1：已完成修读，2：正在修读，3：未修读）
     */
    @ApiModelProperty(value = "修读进度", name = "progress")
    private Integer progress;

    //////////////////////////////// 自定义字段 ////////////////////////////////
    /**
     * 自检
     */
    @ApiModelProperty(value = "自检", name = "selfCheck")
    private String selfCheck;

}