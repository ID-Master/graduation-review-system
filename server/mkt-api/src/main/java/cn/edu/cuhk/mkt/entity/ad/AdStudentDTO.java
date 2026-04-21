package cn.edu.cuhk.mkt.entity.ad;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生DTO
 *
 */
@Data
public class AdStudentDTO implements Serializable {

    /**
     * 组织编码 department
     * AO
     */
    @JSONField(name = "STU_COLLEGE_CODE")
    private String department;

    /**
     * 说明 description
     * eg 2016 Undergraduate
     */
    private String description;

    /**
     * 英文名称  displayName
     * Ziyuan Liu (116030044)
     */
    private String displayName;

    /**
     * 名称
     */
    @JSONField(name = "STU_NAME")
    private String name;

    /**
     * 性别
     * F M
     */
    private String employeeType;

    /**
     * 员工号 employeeID
     * 116030044
     */
    @JSONField(name = "STU_EMPLID")
    private String employeeId;


    /**
     * 邮箱 mail
     * xxxx@link.cuhk.edu.cn
     */
    private String mail;

    /**
     * 邮箱前缀:mailNickname
     */
    private String mailNickName;

    /**
     * 职称  title
     * eg:Student
     */
    private String title;

    /**
     * 层级
     * eg OU=staff,OU=cuhksz,DC=CUHK,DC=EDU,DC=CN
     */
    private String distinguishedName;


}
