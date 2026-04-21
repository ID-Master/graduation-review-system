package cn.edu.cuhk.mkt.entity.report;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;


@Data
@ColumnWidth(value = 30)
public class MktUserDto implements Serializable {

    @ExcelProperty(index = 0, value = "学号")
    private String loginName;

    @ExcelProperty(index = 1, value = "专业")
    private String major;

    @ExcelProperty(index = 2, value = "姓名")
    private String name;

    @ExcelProperty(index = 3, value = "预期毕业学期")
    private String expectedGraduationTerm;

    @ExcelProperty(index = 4, value = "邮箱")
    private String email;

}
