package cn.edu.cuhk.mkt.entity.report;

import cn.edu.cuhk.mkt.entity.BaseVO;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ColumnWidth(value = 30)
public class CourseExportDto{
    @ExcelProperty(index = 0, value = "年级")
    private String grade;
    @ExcelProperty(index = 1, value = "专业")
    private String major;
    @ExcelProperty(index = 2, value = "总（人数）")
    private Integer total;
    @ExcelProperty(index = 3, value = "已填（人数）")
    private Integer submitTotal;
    @ExcelProperty(index = 4, value = "未提交（人数）")
    private Integer unfilledTotal;
    @ExcelProperty(index = 5, value = "申报本学期毕业（人数）")
    private Integer selfDeclarationTotal;
    @ExcelProperty(index = 6, value = "满足毕业（人数）")
    private Integer officerCheckedTotal;
}