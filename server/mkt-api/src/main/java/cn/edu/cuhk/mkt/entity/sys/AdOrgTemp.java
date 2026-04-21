package cn.edu.cuhk.mkt.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统模块-ad组织临时表
 *
 * @author taok
 * @date 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_ad_org_temp")
public class AdOrgTemp extends SuperModel {

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
     * 英文名称
     */
    @TableField("display_name")
    private String displayName;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 层级
     */
    @TableField("distinguished_name")
    private String distinguishedName;

    /**
     * 职称
     */
    @TableField("title")
    private String title;

    /**
     * 父id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 是否有子集
     */
    @TableField("has_children")
    private Integer hasChildren;

    /**
     * 权重
     */
    @TableField("weight")
    private String weight;
}
