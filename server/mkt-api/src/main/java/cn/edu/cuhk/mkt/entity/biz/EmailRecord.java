package cn.edu.cuhk.mkt.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务模块-邮件记录表
 *
 * @author taok
 * @date 2021-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_email_record")
public class EmailRecord extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 数据版本号
     */
    @TableField("version")
    private Long version;

    /**
     * 备注信息
     */
    @TableField("description")
    private String description;

    /**
     * 业务数据id
     */
    @TableField("biz_id")
    private String bizId;

    /**
     * 业务类型
     */
    @TableField("biz_type")
    private String bizType;

    /**
     * 发送邮箱
     */
    @TableField("mail_from")
    private String mailFrom;

    /**
     * 接收邮箱，多个用逗号分隔
     */
    @TableField("mail_to")
    private String mailTo;

    /**
     * 邮件状态（0：待发送，1：成功，2：失败）
     */
    @TableField("status")
    private Integer status;

    /**
     * 主题
     */
    @TableField("subject")
    private String subject;

    /**
     * 邮件内容
     */
    @TableField("content")
    private String content;

    /**
     * 重试次数
     */
    @TableField("retrys")
    private Integer retrys;

    /**
     * 错误信息
     */
    @TableField("error_info")
    private String errorInfo;
}
