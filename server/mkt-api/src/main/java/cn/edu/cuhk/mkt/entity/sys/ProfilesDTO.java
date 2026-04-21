package cn.edu.cuhk.mkt.entity.sys;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统模块-全局配置表 dto对象
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProfilesDTO", description = "系统模块-全局配置表 DTO对象")
public class ProfilesDTO extends SuperModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "description")
    private String description;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", name = "code")
    private String code;

    /**
     * 变量值
     */
    @ApiModelProperty(value = "变量值", name = "value")
    private String value;

    /**
     * 启用状态（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "启用状态（0：禁用，1：启用）", name = "status")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sortIndex")
    private Integer sortIndex;
}