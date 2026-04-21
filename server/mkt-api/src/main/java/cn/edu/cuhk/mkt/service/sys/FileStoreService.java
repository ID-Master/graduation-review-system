package cn.edu.cuhk.mkt.service.sys;

import cn.edu.cuhk.mkt.entity.sys.FileStore;
import com.uneed.common.mybatis.base.SuperService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统模块-文件表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface FileStoreService extends SuperService<FileStore> {

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    FileStore uploadFile(MultipartFile multipartFile);

}