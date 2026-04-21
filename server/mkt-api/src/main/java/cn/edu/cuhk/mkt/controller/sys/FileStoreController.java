package cn.edu.cuhk.mkt.controller.sys;

import cn.edu.cuhk.mkt.converter.sys.FileStoreConverter;
import cn.edu.cuhk.mkt.entity.sys.FileStore;
import cn.edu.cuhk.mkt.entity.sys.FileStoreVO;
import cn.edu.cuhk.mkt.service.sys.FileStoreService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统模块-文件表 前端控制器
 *
 * @author taok
 * @date 2021-08-14
 */
@Slf4j
@Api(tags = {"系统模块-文件表"})
@RestController
@RequestMapping("${adminPath}/sys/file-store")
public class FileStoreController extends AbstractRestController<FileStoreService, FileStore, FileStoreConverter, FileStoreVO> {
    @Autowired
    private UserService userService;

    /**
     * 根据id获取数据详情
     *
     * @param id 数据主键
     * @return Result<FileStoreVO> 详情结果
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "detail/{id}")
    public Result<FileStoreVO> detail(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/file-store/detail/{id}, id=" + id);
        return super.detail(id, null);
    }

    /**
     * 根据条件参数获取分页后的列表数据
     *
     * @param search 条件参数
     * @return Result<PageData<FileStoreVO>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "list")
    public Result<PageData<FileStoreVO>> list(@RequestBody @ApiParam(value = "条件参数", required = true) PageSearch<FileStoreVO> search) {
        log.info("====> /sys/file-store/list, search=" + JsonUtil.toJson(search));
        Result<PageData<FileStoreVO>> pageDataResult = super.list(search, null);
        if(null != pageDataResult && null != pageDataResult.getData()){
            userService.fillUserData(pageDataResult.getData().getRecords());
        }
        return pageDataResult;
    }

}
