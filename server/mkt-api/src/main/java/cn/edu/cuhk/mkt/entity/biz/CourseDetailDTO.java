package cn.edu.cuhk.mkt.entity.biz;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 业务模块-学生课程数据明细表 dto对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseDetailDTO", description = "业务模块-学生课程数据明细表 DTO对象")
public class CourseDetailDTO extends SuperModel implements Serializable {

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
     * 课程主id
     */
    @ApiModelProperty(value = "课程主id", name = "courseMasterId")
    private String courseMasterId;

    /**
     * 课程分类编码
     */
    @ApiModelProperty(value = "课程分类编码", name = "courseCategoryCode")
    private String courseCategoryCode;

    /**
     * 课程子部分
     */
    @ApiModelProperty(value = "课程子部分", name = "part")
    private String part;

    /**
     * 课程模板id
     */
    @ApiModelProperty(value = "课程模板id", name = "courseTemplateId")
    private String courseTemplateId;

    /**
     * 课程编码
     */
    @ApiModelProperty(value = "课程编码", name = "courseCode")
    private String courseCode;

    /**
     * 课程标题
     */
    @ApiModelProperty(value = "课程标题", name = "courseTitle")
    private String courseTitle;

    /**
     * 得分
     */
    @ApiModelProperty(value = "得分", name = "units")
    private BigDecimal units;

    /**
     * 自检
     */
    @ApiModelProperty(value = "自检", name = "selfCheck")
    private String selfCheck;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    /**
     * 选修课
     */
    @ApiModelProperty(value = "选修课", name = "minor")
    private String minor;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sortIndex")
    private Integer sortIndex;

    /**
     * 类型（1：学分，2：修读进度）
     */
    @ApiModelProperty(value = "类型", name = "type")
    private String type;

    /**
     * 修读进度（1：已完成修读，2：正在修读，3：未修读）
     */
    @ApiModelProperty(value = "修读进度", name = "progress")
    private Integer progress;

    /**
     * 扩展属性1
     */
    @ApiModelProperty(value = "扩展属性1", name = "attr1")
    private String attr1;

    /**
     * 扩展属性2
     */
    @ApiModelProperty(value = "扩展属性2", name = "attr2")
    private String attr2;

}