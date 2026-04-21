package cn.edu.cuhk.mkt.entity.report;

import lombok.Data;

import java.io.Serializable;

@Data
public class MktCourseReportHead implements Serializable {
    private String ucUnits;
    private String spUnits;
    private String mrUnits;
    private String meaUnits;
    private String mebUnits;
    private String feUnits;
}
