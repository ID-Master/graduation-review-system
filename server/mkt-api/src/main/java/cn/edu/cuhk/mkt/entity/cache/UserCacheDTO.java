package cn.edu.cuhk.mkt.entity.cache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 缓存用户信息
 *
 * @author taokai
 */
@Data
@ApiModel(value = "UserCacheDTO", description = "缓存用户信息")
public class UserCacheDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", name = "id")
    private String id;

    /**
     * 中文名称
     */
    @ApiModelProperty(value = "中文名称", name = "nameCh")
    private String nameCh;

    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称", name = "nameEn")
    private String nameEn;

}
