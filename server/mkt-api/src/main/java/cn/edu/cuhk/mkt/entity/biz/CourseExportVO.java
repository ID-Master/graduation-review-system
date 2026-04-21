package cn.edu.cuhk.mkt.entity.biz;

import cn.edu.cuhk.mkt.entity.BaseVO;
import cn.edu.cuhk.mkt.entity.sys.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 业务模块-学生课程数据主表 vo对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@ApiModel(value = "CourseExportVO", description = "VO对象")
public class CourseExportVO extends BaseVO {
    @ApiModelProperty(value = "年级", name = "年级")
    private String grade;
    @ApiModelProperty(value = "专业", name = "专业")
    private String major;
    @ApiModelProperty(value = "总（人数）", name = "总（人数）")
    private Integer total;
    @ApiModelProperty(value = "已填（人数）", name = "已填（人数）")
    private Integer submitTotal;
    @ApiModelProperty(value = "未提交（人数）", name = "未提交（人数）")
    private Integer unfilledTotal;
    @ApiModelProperty(value = "申报本学期毕业（人数）", name = "申报本学期毕业（人数）")
    private Integer selfDeclarationTotal;
    @ApiModelProperty(value = "满足毕业（人数）", name = "满足毕业（人数）")
    private Integer officerCheckedTotal;
}