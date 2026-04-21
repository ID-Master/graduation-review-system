package cn.edu.cuhk.mkt.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统模块-ad学生临时表 vo对象
 *
 * @author taok
 * @date 2021-08-23
 */
@Data
@ApiModel(value = "AdStudentTempVO", description = "系统模块-ad学生临时表 VO对象")
public class AdStudentTempVO implements Serializable {

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
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称", name = "displayName")
    private String displayName;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    /**
     * 性别(F M)
     */
    @ApiModelProperty(value = "性别(F M)", name = "employeeType")
    private String employeeType;

    /**
     * 员工号
     */
    @ApiModelProperty(value = "员工号", name = "employeeId")
    private String employeeId;

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
     * 层级
     */
    @ApiModelProperty(value = "层级", name = "distinguishedName")
    private String distinguishedName;

}