package cn.edu.cuhk.mkt.service.oss;

import cn.edu.cuhk.mkt.entity.minio.MinioObjectDTO;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * Minio文件存储服务
 * @author taokai
 */
public interface MinioService {

    /**
     * 判断bucket是否存在
     *
     * @param bucket 桶名
     * @return
     */
    boolean bucketExists(String bucket);

    /**
     * 判断bucket是否存在，不存在抛异常
     * @param bucket 桶名
     */
    void bucketExistsThowException(String bucket);

    /**
     * 根据bucket获取信息
     *
     * @param bucket 桶名
     * @return
     */
    Optional<Bucket> getBucket(String bucket);

    /**
     * 创建 bucket
     *
     * @param bucket 桶名
     */
    void createBucket(String bucket);

    /**
     * 根据bucket删除信息
     *
     * @param bucket 桶名
     */
    void removeBucket(String bucket);

    /**
     * 获取全部bucket
     *
     * @return 全部桶
     */
    List<Bucket> listBuckets();

    /**
     * 获取文件外链
     *
     * @param bucket 桶名
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    String getObjectUrl(String bucket, String objectName, Integer expires);

    /**
     * 根据文件前置查询文件
     *
     * @param bucket 桶名
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    List<String> listObjects(String bucket, String prefix, boolean recursive);

    /**
     * 删除文件
     *
     * @param bucket 桶名，如果没值则取默认值
     * @param object 文件id
     */
    void removeObject(String bucket, String object);

    /**
     * 批量删除文件对象
     * 参考文档：https://blog.csdn.net/puzzledboylhc/article/details/109726267
     * @param bucket 存储桶名
     * @param objects 对象名称集合
     * @return 批量删除文件，返回结果
     */
    Iterable<Result<DeleteError>> removeObjects(String bucket, List<String> objects);

    /**
     * 上传文件
     *
     * @param multipartFile 文件流
     * @return 文件对象
     */
    MinioObjectDTO putObject(MultipartFile multipartFile);

    /**
     * 上传文件
     *
     * @param multipartFiles 文件流
     * @return 文件对象集合
     */
    List<MinioObjectDTO> putObject(MultipartFile ...multipartFiles);

    /**
     * 下载文件
     *
     * @param objectId 文件id
     * @param response 响应输出对象
     */
    void download(String objectId, HttpServletResponse response);

}
