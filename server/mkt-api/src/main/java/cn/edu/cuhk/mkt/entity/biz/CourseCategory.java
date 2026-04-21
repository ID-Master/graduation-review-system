package cn.edu.cuhk.mkt.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务模块-课程分类表
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_course_category")
public class CourseCategory extends SuperModel {

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
     * 专业
     */
    @TableField("major")
    private String major;

    /**
     * 课程分类编号
     */
    @TableField("category_code")
    private String categoryCode;

    /**
     * 课程分类名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 得分
     */
    @TableField("units")
    private Integer units;

    /**
     * 课程子部分
     */
    @TableField("part")
    private String part;

    /**
     * 年级
     */
    @TableField("grade")
    private String grade;

    /**
     * 排序
     */
    @TableField("sort_index")
    private Integer sortIndex;

    /**
     * 类型（1：学分，2：修读进度）
     */
    @TableField("type")
    private Integer type;

    /**
     * 标准（0：否，1：是）
     */
    @TableField("standard")
    private Integer standard;

    /**
     * 是否是国际学生（0：否，1：是）
     */
    @TableField("international_student")
    private Integer internationalStudent;

}
