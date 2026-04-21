package cn.edu.cuhk.mkt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 展示层VO对象基类
 *
 * @author taokai
 */
@Data
@ApiModel(value = "BaseVO", description = "展示层VO对象基类")
public class BaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id", name = "createdBy")
    private String createdBy;

    /**
     * 创建人中文名称
     */
    @ApiModelProperty(value = "创建人中文名称", name = "createNameCh")
    private String createNameCh;

    /**
     * 创建人英文名称
     */
    @ApiModelProperty(value = "创建人英文名称", name = "createNameEn")
    private String createNameEn;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createdDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 更新人id
     */
    @ApiModelProperty(value = "更新人id", name = "updatedBy")
    private String updatedBy;

    /**
     * 更新人中文名称
     */
    @ApiModelProperty(value = "更新人中文名称", name = "updateNameCh")
    private String updateNameCh;

    /**
     * 更新人英文名称
     */
    @ApiModelProperty(value = "更新人英文名称", name = "updateNameEn")
    private String updateNameEn;

    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间", name = "updatedDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

}
