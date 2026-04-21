package cn.edu.cuhk.mkt.entity.biz;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 业务模块-邮件记录表 dto对象
 *
 * @author taok
 * @date 2021-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EmailRecordDTO", description = "业务模块-邮件记录表 DTO对象")
public class EmailRecordDTO extends SuperModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 数据版本号
     */
    @ApiModelProperty(value = "数据版本号", name = "version")
    private Long version;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", name = "description")
    private String description;

    /**
     * 业务数据id
     */
    @ApiModelProperty(value = "业务数据id", name = "bizId")
    private String bizId;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型", name = "bizType")
    private String bizType;

    /**
     * 发送邮箱
     */
    @ApiModelProperty(value = "发送邮箱", name = "mailFrom")
    private String mailFrom;

    /**
     * 接收邮箱，多个用逗号分隔
     */
    @ApiModelProperty(value = "接收邮箱，多个用逗号分隔", name = "mailTo")
    private String mailTo;

    /**
     * 邮件状态（0：待发送，1：成功，2：失败）
     */
    @ApiModelProperty(value = "邮件状态（0：待发送，1：成功，2：失败）", name = "status")
    private Integer status;

    /**
     * 主题
     */
    @ApiModelProperty(value = "主题", name = "subject")
    private String subject;

    /**
     * 邮件内容
     */
    @ApiModelProperty(value = "邮件内容", name = "content")
    private String content;

    /**
     * 重试次数
     */
    @ApiModelProperty(value = "重试次数", name = "retrys")
    private Integer retrys;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息", name = "errorInfo")
    private String errorInfo;

}