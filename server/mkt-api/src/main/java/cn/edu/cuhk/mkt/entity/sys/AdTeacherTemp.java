package cn.edu.cuhk.mkt.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统模块-ad老师临时表
 *
 * @author taok
 * @date 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_ad_teacher_temp")
public class AdTeacherTemp extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 数据版本
     */
    @TableField("version")
    private Long version;

    /**
     * 备注信息
     */
    @TableField("description")
    private String description;

    /**
     * 批次号
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 组织编码
     */
    @TableField("department")
    private String department;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 英文名称
     */
    @TableField("display_name")
    private String displayName;

    /**
     * 员工号
     */
    @TableField("employee_id")
    private String employeeId;

    /**
     * 工作电话
     */
    @TableField("ip_phone")
    private String ipPhone;

    /**
     * 邮箱
     */
    @TableField("mail")
    private String mail;

    /**
     * 邮箱前缀
     */
    @TableField("mail_nick_name")
    private String mailNickName;

    /**
     * 职称
     */
    @TableField("title")
    private String title;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 工作地址
     */
    @TableField("physical_delivery_office_name")
    private String physicalDeliveryOfficeName;

    /**
     * 层级
     */
    @TableField("distinguished_name")
    private String distinguishedName;
}
