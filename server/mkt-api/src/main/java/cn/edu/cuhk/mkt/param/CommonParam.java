package cn.edu.cuhk.mkt.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 公共参数
 *
 * @author taokai
 */
@Data
@ApiModel(value = "CommonParam", description = "公共参数对象")
public class CommonParam implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id", name = "id")
    private String id;

    /**
     * id集合
     */
    @ApiModelProperty(value = "id集合", name = "ids")
    private List<String> ids;

}
