package cn.edu.cuhk.mkt.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统模块-全局配置表
 *
 * @author taok
 * @date 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_profiles")
public class Profiles extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 变量值
     */
    @TableField("value")
    private String value;

    /**
     * 启用状态（0：禁用，1：启用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 排序
     */
    @TableField("sort_index")
    private Integer sortIndex;
}
