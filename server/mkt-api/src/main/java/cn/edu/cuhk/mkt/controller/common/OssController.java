package cn.edu.cuhk.mkt.controller.common;

import cn.edu.cuhk.mkt.common.annotation.IgnoreUserToken;
import cn.edu.cuhk.mkt.common.consts.SystemConst;
import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.entity.sys.FileStore;
import cn.edu.cuhk.mkt.param.auth.MinioParam;
import cn.edu.cuhk.mkt.service.oss.MinioService;
import cn.edu.cuhk.mkt.service.sys.FileStoreService;
import com.uneed.common.core.date.DateUtil;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Date;
import java.util.List;

/**
 * @author taokai
 */
@Slf4j
@Api(tags = {"系统模块-oss文件存储服务"})
@RestController
@RequestMapping("${adminPath}/common/oss")
public class OssController extends AbstractController {
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private MinioService minioService;

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    @ApiOperation(value = "文件上传")
    @IgnoreUserToken
    @PostMapping("/upload")
    public Result<FileStore> upload(@RequestParam("file") @ApiParam(value = "文件参数：file", required = true) MultipartFile multipartFile) {
        log.info("====> /common/oss/upload, 开始时间: {}", DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        FileStore fileStore = fileStoreService.uploadFile(multipartFile);
        log.info("====>end /common/oss/upload, 结束时间: {}", DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return Result.ok(fileStore);
    }

    /**
     * 文件下载
     * @param fileUrl
     * @return
     */
    @ApiOperation(value = "文件下载")
    @IgnoreUserToken
    @GetMapping("/download/{fileUrl}/**")
    public void download(@PathVariable(value = "fileUrl") @ApiParam(value = "文件url地址", required = true) String fileUrl) {
        final String path = getRequest().getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern = getRequest().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
        String objectId = fileUrl;
        if (null != arguments && !arguments.isEmpty()) {
            objectId = fileUrl + SystemConst.URI_DELIMITER + arguments;
        }
        log.info("====> /common/oss/download/{}", objectId);
        minioService.download(objectId, getResponse());
    }

    /**
     * 批量删除文件对象
     * @param minioParam
     * @return
     */
    @ApiOperation(value = "批量删除文件对象")
    @PostMapping("/remove-files")
    public Result<Object> removeFiles(@RequestBody MinioParam minioParam) {
        log.info("====> /common/oss/remove-files/{}", minioParam.toString());
        UserInfo userInfo = UserSession.getUser();
        if(userInfo.getSuperAdmin().equals(Boolean.FALSE)){
            AssertUtil.isNull(null, "非管理员，禁止批量删除操作");
        }
        String bucket = minioParam.getBucket();
        String prefix = StringUtils.defaultString(minioParam.getPrefix());
        List<String> objectList = minioService.listObjects(bucket, prefix, true);
        minioService.removeObjects(bucket, objectList);
        return super.success("删除成功");
    }
}