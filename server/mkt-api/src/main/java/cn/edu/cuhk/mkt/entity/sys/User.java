package cn.edu.cuhk.mkt.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统模块-用户表
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends SuperModel {

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
     * 账号
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户类型（STUDENT：学生，TEACHER：老师，SYSTEM：系统账号）
     */
    @TableField("user_type")
    private String userType;

    /**
     * 状态（0：无效，1：有效）
     */
    @TableField("status")
    private Integer status;

    /**
     * 学生id
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 中文名称
     */
    @TableField("name_ch")
    private String nameCh;

    /**
     * 英文名称
     */
    @TableField("name_en")
    private String nameEn;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 主修专业
     */
    @TableField("major")
    private String major;

    /**
     * 选修课程
     */
    @TableField("minor")
    private String minor;

    /**
     * 预计毕业年份
     */
    @TableField("expected_year")
    private String expectedYear;

    /**
     * 手机区号
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 联系电话
     */
    @TableField("contact_tel")
    private String contactTel;

    /**
     * 年级
     */
    @TableField("grade")
    private String grade;

    /**
     * 是否是国际学生（0：否，1：是）
     */
    @TableField("international_student")
    private Integer internationalStudent;

}
