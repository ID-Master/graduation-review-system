package com.uneed.common.mybatis.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务实体的超类，该类用来定义实体的创建、最后修改信息，及逻辑删除标记。
 *
 * @author diablo
 * @date 2020/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantModel extends SuperModel {

    private static final long serialVersionUID = 1702454943786970579L;

    /**
     * 创建人
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;
}
