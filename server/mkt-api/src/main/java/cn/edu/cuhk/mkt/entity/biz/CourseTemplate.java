package cn.edu.cuhk.mkt.entity.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 业务模块-课程模板表
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_course_template")
public class CourseTemplate extends SuperModel {

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
     * 课程分类id
     */
    @TableField("course_category_id")
    private String courseCategoryId;

    /**
     * 课程编号
     */
    @TableField("course_code")
    private String courseCode;

    /**
     * 课程标题
     */
    @TableField("course_title")
    private String courseTitle;

    /**
     * 课程子部分
     */
    @TableField("part")
    private String part;

    /**
     * 评分
     */
    @TableField("units")
    private BigDecimal units;

    /**
     * 类型（1：学分，2：修读进度）
     */
    @TableField("type")
    private Integer type;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 排序
     */
    @TableField("sort_index")
    private Integer sortIndex;

    /**
     * 修读进度（1：已完成修读，2：正在修读，3：未修读）
     */
    @TableField("progress")
    private Integer progress;

}
