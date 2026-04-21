package cn.edu.cuhk.mkt.entity.sys;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统模块-用户表 dto对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserDTO", description = "系统模块-用户表 DTO对象")
public class UserDTO extends SuperModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 数据版本
     */
    @ApiModelProperty(value = "数据版本", name = "version")
    private Long version;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", name = "description")
    private String description;

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
     * 用户类型（STUDENT：学生，TEACHER：老师，SYSTEM：系统账号）
     */
    @ApiModelProperty(value = "用户类型（STUDENT：学生，TEACHER：老师，SYSTEM：系统账号）", name = "userType")
    private String userType;

    /**
     * 状态（0：无效，1：有效）
     */
    @ApiModelProperty(value = "状态（0：无效，1：有效）", name = "status")
    private Integer status;

    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id", name = "studentId")
    private String studentId;

    /**
     * 中文名称
     */
    @ApiModelProperty(value = "中文名称", name = "nameCh")
    private String nameCh;

    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称", name = "nameEn")
    private String nameEn;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    /**
     * 主修专业
     */
    @ApiModelProperty(value = "主修专业", name = "major")
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
     * 是否是国际学生（0：否，1：是）
     */
    @ApiModelProperty(value = "是否是国际学生", name = "internationalStudent")
    private Integer internationalStudent;

}