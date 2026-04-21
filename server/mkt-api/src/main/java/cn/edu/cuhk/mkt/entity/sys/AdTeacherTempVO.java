package cn.edu.cuhk.mkt.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统模块-ad老师临时表 vo对象
 *
 * @author taok
 * @date 2021-08-23
 */
@Data
@ApiModel(value = "AdTeacherTempVO", description = "系统模块-ad老师临时表 VO对象")
public class AdTeacherTempVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @ApiModelProperty(value = "id主键", name = "id")
    private String id;

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
     * 批次号
     */
    @ApiModelProperty(value = "批次号", name = "batchNumber")
    private String batchNumber;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码", name = "department")
    private String department;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称", name = "displayName")
    private String displayName;

    /**
     * 员工号
     */
    @ApiModelProperty(value = "员工号", name = "employeeId")
    private String employeeId;

    /**
     * 工作电话
     */
    @ApiModelProperty(value = "工作电话", name = "ipPhone")
    private String ipPhone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "mail")
    private String mail;

    /**
     * 邮箱前缀
     */
    @ApiModelProperty(value = "邮箱前缀", name = "mailNickName")
    private String mailNickName;

    /**
     * 职称
     */
    @ApiModelProperty(value = "职称", name = "title")
    private String title;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile")
    private String mobile;

    /**
     * 工作地址
     */
    @ApiModelProperty(value = "工作地址", name = "physicalDeliveryOfficeName")
    private String physicalDeliveryOfficeName;

    /**
     * 层级
     */
    @ApiModelProperty(value = "层级", name = "distinguishedName")
    private String distinguishedName;

}