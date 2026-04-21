package cn.edu.cuhk.mkt.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录参数对象
 *
 * @author taokai
 */
@ApiModel(value = "LoginParam", description = "登录参数对象")
@Data
public class LoginParam implements Serializable {
    /**
     * 模拟AD账号身份（学生：STUDENT，老师：TEACHER）
     */
    @ApiModelProperty(value = "模拟AD账号身份（学生：STUDENT，老师：TEACHER）", name = "simulated")
    private String simulated;

    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id", name = "studentId")
    private String studentId;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", name = "loginName")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "password")
    private String password;

    /**
     * 必修课程
     */
    @ApiModelProperty(value = "必修课程", name = "major")
    private String major;

    /**
     * 选修课程
     */
    @ApiModelProperty(value = "选修课程", name = "minor")
    private String minor;

    /**
     * 预计毕业年份
     */
    @ApiModelProperty(value = "预计毕业年份", name = "expectedYear")
    private String expectedYear;

    /**
     * 手机区号
     */
    @ApiModelProperty(value = "手机区号", name = "areaCode")
    private String areaCode;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", name = "contactTel")
    private String contactTel;

    /**
     * 账号类型（学生：Student，老师：除Student之外的都是）
     */
    @ApiModelProperty(value = "账号类型", name = "title")
    private String title;

    /**
     * 账号名称
     */
    @ApiModelProperty(value = "账号名称", name = "uniqueName")
    private String uniqueName;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", name = "upn")
    private String upn;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级", name = "grade")
    private String grade;

    /**
     * 是否是国际学生（0：否，1：是）
     */
    @ApiModelProperty(value = "是否是国际学生", name = "internationalStudent")
    private Integer internationalStudent;

}
