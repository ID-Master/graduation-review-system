package cn.edu.cuhk.mkt.param.biz;

import cn.edu.cuhk.mkt.param.CommonParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务模块-课程模板表 查询对象
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@ApiModel(value = "CourseTemplateParam", description = "业务模块-课程模板表 查询对象")
public class CourseTemplateParam extends CommonParam {

    private static final long serialVersionUID = 1L;
    /**
     * 用户（学生）id
     */
    private String userId;
    /**
     * 专业
     */
    @ApiModelProperty(value = "专业", name = "major")
    private String major;

    /**
     * 模板分类编号
     */
    @ApiModelProperty(value = "模板分类编号/Template code", name = "categoryCode")
    private String categoryCode;

    /**
     * 课程模板part
     */
    @ApiModelProperty(value = "课程模板part", name = "part")
    private String part;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;

}