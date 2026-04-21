package cn.edu.cuhk.mkt.entity.sys;

import com.uneed.common.mybatis.model.SuperModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统模块-文件表 dto对象
 *
 * @author taok
 * @date 2021-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FileStoreDTO", description = "系统模块-文件表 DTO对象")
public class FileStoreDTO extends SuperModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 数据版本
     */
    @ApiModelProperty(value = "数据版本", name = "version")
    private Long version;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", name = "description")
    private String description;

    /**
     * 文件名称（不带后缀）
     */
    @ApiModelProperty(value = "文件名称（不带后缀）", name = "fileName")
    private String fileName;

    /**
     * 原文件名称（带后缀）
     */
    @ApiModelProperty(value = "原文件名称（带后缀）", name = "originName")
    private String originName;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小", name = "fileSize")
    private Long fileSize;

    /**
     * 文件后缀
     */
    @ApiModelProperty(value = "文件后缀", name = "extension")
    private String extension;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型", name = "contentType")
    private String contentType;

    /**
     * 原文件url
     */
    @ApiModelProperty(value = "原文件url", name = "fileUrl")
    private String fileUrl;

    /**
     * 压缩图url
     */
    @ApiModelProperty(value = "压缩图url", name = "compressUrl")
    private String compressUrl;

    /**
     * 缩略图url
     */
    @ApiModelProperty(value = "缩略图url", name = "thumbnailUrl")
    private String thumbnailUrl;

    /**
     * 预览图url
     */
    @ApiModelProperty(value = "预览图url", name = "previewUrl")
    private String previewUrl;

    /**
     * 图片base64字符
     */
    @ApiModelProperty(value = "图片base64字符", name = "imageBase64")
    private String imageBase64;

    /**
     * 文件md5
     */
    @ApiModelProperty(value = "文件md5", name = "md5")
    private String md5;

}