package cn.edu.cuhk.mkt.entity.report;

import lombok.Data;

import java.io.Serializable;

/**
 * MKT课程报表数据
 *
 * @author taokai
 */
@Data
public class MktCourseReport implements Serializable {
    private String sid;
    private String name;
    private String major;
    private String grade;
    private String internationalStudent;
    private String progress1;
    private String progress2;
    private String progress3;
    private String totalUnitsPassed;
    /**
     * UC （36）
     */
    private Integer ucPass;
    private Integer ucNr;
    private Integer ucIp;
    private Integer ucLeft;
    private String ucUnits;
    private String ucRemark;
    /**
     * SP (30)
     */
    private Integer spPass;
    private Integer spNr;
    private Integer spIp;
    private Integer spLeft;
    private String spUnits;
    private String spRemark;
    /**
     * MR (18)
     */
    private Integer mrPass;
    private Integer mrNr;
    private Integer mrIp;
    private Integer mrLeft;
    private String mrUnits;
    private String mrRemark;
    /**
     * Me(a) (9-15)
     */
    private Integer meaPass;
    private Integer meaNr;
    private Integer meaIp;
    private Integer meaLeft;
    private String meaUnits;
    private String meaRemark;
    /**
     * Me(b) (3-9)
     */
    private Integer mebPass;
    private Integer mebNr;
    private Integer mebIp;
    private String mebLeft;
    private String mebUnits;
    private String mebRemark;
    /**
     * FE (18)
     */
    private Integer fePass;
    private Integer feNr;
    private Integer feIp;
    private Integer feLeft;
    private String feUnits;
    private String feRemark;

    private String remakrs;
}
