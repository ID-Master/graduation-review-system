package cn.edu.cuhk.mkt.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统模块-ad组织临时表 vo对象
 *
 * @author taok
 * @date 2021-08-23
 */
@Data
@ApiModel(value = "AdOrgTempVO", description = "系统模块-ad组织临时表 VO对象")
public class AdOrgTempVO implements Serializable {

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
     * 层级
     */
    @ApiModelProperty(value = "层级", name = "distinguishedName")
    private String distinguishedName;

    /**
     * 职称
     */
    @ApiModelProperty(value = "职称", name = "title")
    private String title;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id", name = "parentId")
    private String parentId;

    /**
     * 是否有子集
     */
    @ApiModelProperty(value = "是否有子集", name = "hasChildren")
    private Integer hasChildren;

    /**
     * 权重
     */
    @ApiModelProperty(value = "权重", name = "weight")
    private String weight;

}