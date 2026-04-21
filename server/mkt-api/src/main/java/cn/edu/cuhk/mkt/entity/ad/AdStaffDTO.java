package cn.edu.cuhk.mkt.entity.ad;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 员工DTO
 *
 */
@Data
public class AdStaffDTO {

    /**
     * 组织编码 department
     */
    @JSONField(name = "TEA_COLLEGE_CODE")
    private String  department;

    /**
     * 名称
     */
    @JSONField(name = "TEA_NAME")
    private String name;

    /**
     * 英文名称  displayName
     */
    @JSONField(name = "TEA_USERNAME")
    private String displayName;

    /**
     * 员工号 employeeID
     */
    @JSONField(name = "TEA_EMPLID")
    private String employeeId;


    /**
     * 工作电话 ipPhone
     * 73507->84273507
     */
    private String ipPhone;

    /**
     * 邮箱 mail
     * xxxx@cuhk.edu.cn
     */
    private String mail;

    /**
     * 邮箱前缀:mailNickname
     */
    private String mailNickName;

    /**
     * 职称  title
     * eg:Engineer
     */
    private String title;

    /**
     * 手机号 mobile
     * eg:
     */
    private String mobile;

    /**
     * 工作地址 physicalDeliveryOfficeName
     * Room 203B, Zhi Xin Building
     */
    private String  physicalDeliveryOfficeName;


    /**
     * 层级
     * eg OU=staff,OU=cuhksz,DC=CUHK,DC=EDU,DC=CN
     */
    private String distinguishedName;


    /**
     * memberof
     * CN=G-AllStaff02,OU=Common,OU=CUHKSZ Group,DC=CUHK,DC=EDU,DC=CN, CN=VPN_Cisco,OU=Common,OU=CUHKSZ Group,DC=CUHK,DC=EDU,DC=CN, CN=HAB-AVP-ID,OU=HAB Group,OU=CUHKSZ Group,DC=CUHK,DC=EDU,DC=CN, CN=G-AVP-ID,OU=Staff OU,OU=CUHKSZ Group,DC=CUHK,DC=EDU,DC=CN, CN=VDIUsers,OU=Common,OU=CUHKSZ Group,DC=CUHK,DC=EDU,DC=CN
     */
    private List<String> memberof;


}
