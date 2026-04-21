package cn.edu.cuhk.mkt.entity.minio;

import lombok.Data;

import java.io.Serializable;

/**
 * Minio文件信息
 * @author taokai
 */
@Data
public class MinioObjectDTO implements Serializable {
    /****************************** minio文件对象信息 ******************************/
    private String bucketName;
    private String objectId;
    private String etag;
    private String objectName;
    private String versionId;

    /****************************** 文件对象信息 ******************************/
    private String originalName;
    private String fileName;
    private String extension;
    private Long size;
    private String contentType;
    private String url;
    private String md5;

}
