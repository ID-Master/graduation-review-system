package cn.edu.cuhk.mkt.entity.biz;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 业务模块-课程分类表 dto对象
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseCategoryDTO", description = "业务模块-课程分类表 DTO对象")
public class CourseCategoryDTO extends SuperModel implements Serializable {

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
     * 专业
     */
    @ApiModelProperty(value = "专业", name = "major")
    private String major;

    /**
     * 课程分类编号
     */
    @ApiModelProperty(value = "课程分类编号", name = "categoryCode")
    private String categoryCode;

    /**
     * 课程分类名称
     */
    @ApiModelProperty(value = "课程分类名称", name = "categoryName")
    private String categoryName;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "title")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", name = "content")
    private String content;

    /**
     * 得分
     */
    @ApiModelProperty(value = "得分", name = "units")
    private Integer units;

    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sortIndex")
    private Integer sortIndex;

    /**
     * 类型（1：学分，2：修读进度）
     */
    @ApiModelProperty(value = "类型", name = "type")
    private Integer type;

    /**
     * 标准（0：否，1：是）
     */
    @ApiModelProperty(value = "标准", name = "standard")
    private Integer standard;

    /**
     * 是否是国际学生（0：否，1：是）
     */
    @ApiModelProperty(value = "是否是国际学生", name = "internationalStudent")
    private Integer internationalStudent;

}