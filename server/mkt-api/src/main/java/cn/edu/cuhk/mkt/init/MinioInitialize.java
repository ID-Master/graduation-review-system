package cn.edu.cuhk.mkt.init;

import cn.edu.cuhk.mkt.common.consts.SystemConst;
import cn.edu.cuhk.mkt.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * minio初始化
 *
 * @author taokai
 */
@Slf4j
@DependsOn(value = {"commonConfig", "minioConfig", "minioClient"})
@Component
public class MinioInitialize implements InitializingBean {
    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    @Override
    public void afterPropertiesSet() {
        // 初始化minio bucket
        initBucket();
    }

    private void initBucket(){
        log.info("-------------------- Minio初始化 start --------------------");
        try {
            String bucketName = StringUtils.defaultIfBlank(minioConfig.getBucketName(), SystemConst.MINIO_BUCKET_NAME);
            Boolean bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if(!bucketExist){
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Minio bucketName init success: {}", bucketName);
            }
        }catch (Exception e){
            log.error("初始化minio异常: {}", e.getMessage(), e);
        }
        log.info("-------------------- Minio初始化 end --------------------");
    }
}
