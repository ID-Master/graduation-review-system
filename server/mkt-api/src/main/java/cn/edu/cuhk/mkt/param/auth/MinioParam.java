package cn.edu.cuhk.mkt.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * minio参数对象
 *
 * @author taokai
 */
@ApiModel(value = "MinioParam", description = "minio参数对象")
@Data
public class MinioParam {
    /**
     * 桶
     */
    @ApiModelProperty(value = "桶", name = "bucket")
    private String bucket;

    /**
     * 通过前缀关键字, 模糊查找或精确匹配删除（示例：/mkt，/mkt/ewt34file，/mkt/12345.jpg）
     * 文件夹目录：/mkt 或 /mkt/20210815
     * 文件名称: /mkt/123 或 /mkt/12345.jpg
     */
    @ApiModelProperty(value = "通过前缀关键字, 模糊查找或精确匹配删除（示例：/mkt，/mkt/ewt34file，/mkt/12345.jpg）", name = "prefix")
    private String prefix;

}
