package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.entity.minio.MinioObjectDTO;
import cn.edu.cuhk.mkt.entity.sys.FileStore;
import cn.edu.cuhk.mkt.mapper.sys.FileStoreMapper;
import cn.edu.cuhk.mkt.service.oss.MinioService;
import cn.edu.cuhk.mkt.service.sys.FileStoreService;
import com.uneed.common.core.date.DateUtil;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 系统模块-文件表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class FileStoreServiceImpl extends SuperServiceImpl<FileStoreMapper, FileStore> implements FileStoreService {
    @Autowired
    private MinioService minioService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileStore uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty() || multipartFile.getSize() == 0) {
            AssertUtil.isNull(null, "文件不能为空");
        }
        FileStore fileStore = new FileStore();
        // minio文件信息
        log.info("====> minio服务上传接口, 开始时间: {}", DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        MinioObjectDTO minioObjectDTO = minioService.putObject(multipartFile);
        log.info("====> minio服务上传接口, 结束时间: {}", DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // 新增文件
        fileStore.setFileName(minioObjectDTO.getFileName());
        fileStore.setOriginName(minioObjectDTO.getOriginalName());
        fileStore.setFileSize(minioObjectDTO.getSize());
        fileStore.setExtension(minioObjectDTO.getExtension());
        fileStore.setContentType(minioObjectDTO.getContentType());
        fileStore.setFileUrl(minioObjectDTO.getUrl());
        fileStore.setMd5(minioObjectDTO.getMd5());
        super.insert(fileStore);
        return fileStore;
    }

}
