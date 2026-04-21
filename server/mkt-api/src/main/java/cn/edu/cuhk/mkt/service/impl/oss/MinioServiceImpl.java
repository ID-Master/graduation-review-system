package cn.edu.cuhk.mkt.service.impl.oss;

import cn.edu.cuhk.mkt.common.consts.SystemConst;
import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.config.MinioConfig;
import cn.edu.cuhk.mkt.entity.minio.MinioObjectDTO;
import cn.edu.cuhk.mkt.service.oss.MinioService;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.CharUtil;
import com.uneed.common.core.lang.Md5Util;
import com.uneed.common.core.lang.UUIDUtil;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author taokai
 */
@Slf4j
@Component
public class MinioServiceImpl implements MinioService {
    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    @SneakyThrows(Exception.class)
    @Override
    public boolean bucketExists(String bucket) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    @SneakyThrows(Exception.class)
    @Override
    public void bucketExistsThowException(String bucket) {
        boolean bucketExists = this.bucketExists(bucket);
        if(!bucketExists){
            AssertUtil.isNull(null, "bucket不存在");
        }
    }

    @SneakyThrows
    @Override
    public Optional<Bucket> getBucket(String bucket) {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucket)).findFirst();
    }

    @SneakyThrows(Exception.class)
    @Override
    public void createBucket(String bucket) {
        boolean bucketExists = bucketExists(bucket);
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    @SneakyThrows
    @Override
    public void removeBucket(String bucket) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }

    @SneakyThrows(Exception.class)
    @Override
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    @SneakyThrows
    @Override
    public String getObjectUrl(String bucket, String object, Integer expires) {
        bucketExistsThowException(bucket);
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucket).object(object).expiry(expires).build());
    }

    @SneakyThrows(Exception.class)
    @Override
    public List<String> listObjects(String bucket, String prefix, boolean recursive) {
        bucketExistsThowException(bucket);
        prefix = StringUtils.defaultString(prefix);
        List<String> objects = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).recursive(recursive).build());
        if (objectsIterator != null) {
            Iterator<Result<Item>> iterator = objectsIterator.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    Item item = iterator.next().get();
                    objects.add(item.objectName());
                }
            }
        }
        return objects;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void removeObject(String bucket, String object) {
        List<String> objects = new ArrayList<>();
        objects.add(object);
        this.removeObjects(bucket, objects);
    }

    @SneakyThrows(Exception.class)
    @Override
    public Iterable<Result<DeleteError>> removeObjects(String bucket, List<String> objects) {
        bucketExistsThowException(bucket);
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> deleteResult = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(dos).build());
        // 一定要对results进行迭代，不然不会触发删除操作
        if(deleteResult != null){
            Iterator<Result<DeleteError>> iterator = deleteResult.iterator();
            if(iterator != null){
                while (iterator.hasNext()){
                    DeleteError errorResult = iterator.next().get();
                }
            }
        }
        return deleteResult;
    }

    @SneakyThrows(Exception.class)
    @Override
    public MinioObjectDTO putObject(MultipartFile multipartFile) {
        return putObject(new MultipartFile[] {multipartFile}).get(0);
    }

    @SneakyThrows(Exception.class)
    @Override
    public List<MinioObjectDTO> putObject(MultipartFile ...multipartFiles) {
        String bucketName = StringUtils.defaultString(minioConfig.getBucketName());
        String gateway = StringUtils.defaultString(minioConfig.getGateway());
        String dir = StringUtils.defaultString(minioConfig.getDir());
        List<MinioObjectDTO> minioFileList = new LinkedList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            MinioObjectDTO minioObjectDTO = new MinioObjectDTO();
            // 原文件名称
            String originalName = multipartFile.getOriginalFilename();
            // 文件名
            String fileName = FileNameUtil.mainName(originalName);
            // 文件扩展名
            String extension = FileNameUtil.extName(originalName);
            // UUID重命名
            StringBuilder objectId = new StringBuilder();
            objectId.append(dir)
                    .append(SystemConst.URI_DELIMITER)
                    .append(UUIDUtil.gen32UUID())
                    .append(CharUtil.DOT)
                    .append(extension);
            // 文件名称objectId相同会覆盖
            minioClient.putObject(
                PutObjectArgs.builder()
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), PutObjectArgs.MIN_MULTIPART_SIZE)
                .bucket(bucketName)
                .object(objectId.toString())
                .contentType(multipartFile.getContentType())
                .build()
            );
            minioObjectDTO.setBucketName(bucketName);
            minioObjectDTO.setObjectId(objectId.toString());
            minioObjectDTO.setOriginalName(originalName);
            minioObjectDTO.setFileName(fileName);
            minioObjectDTO.setExtension(extension);
            minioObjectDTO.setSize(multipartFile.getSize());
            minioObjectDTO.setContentType(multipartFile.getContentType());
            minioObjectDTO.setUrl(gateway + objectId.toString());
            minioObjectDTO.setMd5(Md5Util.encrypt(objectId.toString()));
            minioFileList.add(minioObjectDTO);
        }
        return minioFileList;
    }

    @SneakyThrows(Exception.class)
    @Override
    public void download(String objectId, HttpServletResponse response) {
        InputStream is = null;
        try {
            String bucketName = StringUtils.defaultString(minioConfig.getBucketName());
            // 获取对象的元数据
            final StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectId).build());
            String contentType = StringUtils.defaultIfBlank(stat.contentType(), "application/octet-stream");
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(objectId, "UTF-8"));
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectId, "UTF-8"));
            is = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectId).build());
            IOUtils.copy(is, response.getOutputStream());
        }catch (ErrorResponseException e){
            AssertUtil.isNull(null, e.getMessage());
        }catch (Exception e) {
            AssertUtil.isNull(null, "文件下载失败");
        }finally {
            if(is != null){
                is.close();
            }
        }
    }

}
