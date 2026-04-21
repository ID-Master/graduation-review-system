package cn.edu.cuhk.mkt.param.biz;

import cn.edu.cuhk.mkt.param.CommonParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务模块-老师查询自查表对象
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@ApiModel(value = "CourseMasterParam", description = "业务模块-老师查询自查表对象")
public class CourseMasterParam extends CommonParam {

    private static final long serialVersionUID = 1L;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业", name = "major")
    private String major;

    /**
     * 搜索字段(学生id: studentId，名字: name)
     */
    @ApiModelProperty(value = "搜索字段(学生id: studentId，名字: name)", name = "fieldName")
    private String fieldName;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字", name = "keyword")
    private String keyword;

    /**
     * 学生复选框，本人本学期申报毕业（0：否，1：是）
     */
    @ApiModelProperty(value = "学生复选框，本人本学期申报毕业（0：否，1：是）", name = "studentCheckBox")
    private Integer studentCheckBox;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;

    /**
     * 预计毕业学期
     */
    @ApiModelProperty(value = "预计毕业学期", name = "expectedGraduationTerm")
    private String expectedGraduationTerm;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", name = "status")
    private String status;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "时间类型", name = "时间类型（0：提交时间，1：检查时间）")
    private String timeType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;








}