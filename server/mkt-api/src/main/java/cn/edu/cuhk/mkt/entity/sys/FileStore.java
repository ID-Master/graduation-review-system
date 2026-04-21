package cn.edu.cuhk.mkt.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统模块-文件表
 *
 * @author taok
 * @date 2021-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_store")
public class FileStore extends SuperModel {

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
     * 文件名称（不带后缀）
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 原文件名称（带后缀）
     */
    @TableField("origin_name")
    private String originName;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件后缀
     */
    @TableField("extension")
    private String extension;

    /**
     * 文件类型
     */
    @TableField("content_type")
    private String contentType;

    /**
     * 原文件url
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 压缩图url
     */
    @TableField("compress_url")
    private String compressUrl;

    /**
     * 缩略图url
     */
    @TableField("thumbnail_url")
    private String thumbnailUrl;

    /**
     * 预览图url
     */
    @TableField("preview_url")
    private String previewUrl;

    /**
     * 图片base64字符
     */
    @TableField("image_base64")
    private String imageBase64;

    /**
     * 文件md5
     */
    @TableField("md5")
    private String md5;
}
