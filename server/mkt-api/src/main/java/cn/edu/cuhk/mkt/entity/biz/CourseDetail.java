package cn.edu.cuhk.mkt.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 业务模块-学生课程数据明细表
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_course_detail")
public class CourseDetail extends SuperModel {

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
     * 课程主id
     */
    @TableField("course_master_id")
    private String courseMasterId;

    /**
     * 课程分类编码
     */
    @TableField("course_category_code")
    private String courseCategoryCode;

    /**
     * 课程子部分
     */
    @TableField("part")
    private String part;

    /**
     * 课程模板id
     */
    @TableField("course_template_id")
    private String courseTemplateId;

    /**
     * 课程编码
     */
    @TableField("course_code")
    private String courseCode;

    /**
     * 课程标题
     */
    @TableField("course_title")
    private String courseTitle;

    /**
     * 得分
     */
    @TableField("units")
    private BigDecimal units;

    /**
     * 自检
     */
    @TableField("self_check")
    private String selfCheck;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 选修课
     */
    @TableField("minor")
    private String minor;

    /**
     * 排序
     */
    @TableField("sort_index")
    private Integer sortIndex;

    /**
     * 类型（1：学分，2：修读进度）
     */
    @TableField("type")
    private String type;

    /**
     * 修读进度（1：已完成修读，2：正在修读，3：未修读）
     */
    @TableField("progress")
    private Integer progress;

    /**
     * 扩展属性1
     */
    @TableField("attr1")
    private String attr1;

    /**
     * 扩展属性2
     */
    @TableField("attr2")
    private String attr2;

}
