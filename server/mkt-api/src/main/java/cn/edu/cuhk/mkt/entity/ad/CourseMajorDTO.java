package cn.edu.cuhk.mkt.entity.ad;

import lombok.Data;

import java.io.Serializable;

/**
 * 课程专业数据
 * @author taokai
 */
@Data
public class CourseMajorDTO implements Serializable {
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 排序
     */
    private String sortIndex;
}
