package cn.edu.cuhk.mkt.param.biz;

import cn.edu.cuhk.mkt.param.CommonParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务模块-课程分类表 查询对象
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@ApiModel(value = "CourseCategoryParam", description = "业务模块-课程分类表 查询对象")
public class CourseCategoryParam extends CommonParam {

    private static final long serialVersionUID = 1L;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业", name = "major")
    private String major;

    /**
     * 课程分类编号/名称
     */
    @ApiModelProperty(value = "Template code / Template name", name = "templateKeyword")
    private String templateKeyword;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;


}