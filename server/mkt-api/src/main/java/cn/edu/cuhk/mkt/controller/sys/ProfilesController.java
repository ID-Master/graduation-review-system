package cn.edu.cuhk.mkt.controller.sys;

import cn.edu.cuhk.mkt.common.enums.StatusEnum;
import cn.edu.cuhk.mkt.common.util.UserUtil;
import cn.edu.cuhk.mkt.converter.sys.ProfilesConverter;
import cn.edu.cuhk.mkt.entity.biz.CourseDetail;
import cn.edu.cuhk.mkt.entity.biz.StudentCourseVO;
import cn.edu.cuhk.mkt.entity.sys.Profiles;
import cn.edu.cuhk.mkt.entity.sys.ProfilesVO;
import cn.edu.cuhk.mkt.service.sys.ProfilesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.mybatis.page.Sort;
import com.uneed.common.mybatis.utils.Conditions;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;

/**
 * 系统模块-全局配置表 前端控制器
 *
 * @author taok
 * @date 2021-08-17
 */
@Slf4j
@Api(tags = {"系统模块-全局配置表"})
@RestController
@RequestMapping("${adminPath}/sys/profiles")
public class ProfilesController extends AbstractRestController<ProfilesService, Profiles, ProfilesConverter, ProfilesVO> {

    /**
     * 新增数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "新增数据接口", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "insert")
    public Result<String> insert(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) ProfilesVO vo) {
        log.info("====> /sys/profiles/insert, vo=" + JsonUtil.toJson(vo));
        vo.setStatus(StatusEnum.EFFECTIVE.getCode());
        return super.insert(vo, null);
    }

    /**
     * 修改数据
     *
     * @param vo VO对象
     * @return Result<String> 修改结果，并填充受影响行数
     */
    @ApiOperation(value = "修改数据接口，返回受影响行数", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "update")
    public Result<String> update(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) ProfilesVO vo) {
        log.info("====> /sys/profiles/update, vo=" + JsonUtil.toJson(vo));
        return super.update(vo, null);
    }

    /**
     * 根据id删除数据
     *
     * @param id 数据主键
     * @return Result<String> 删除结果，并填充受影响行数
     */
    @ApiOperation(value = "删除数据接口，返回受影响行数", notes = "根据数据id，从数据库中删除其对应的数据")
    @GetMapping(value = "remove/{id}")
    public Result<String> remove(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/profiles/remove/{id}, id=" + id);
        return super.remove(id, null);
    }

    /**
     * 根据id设置数据有效性
     *
     * @param id 数据主键
     * @return Result<String> 设置有效性结果结果，并填充受影响行数
     */
    @ApiOperation(value = "设置数据有效性接口，返回受影响行数", notes = "根据数据id，设置对应数据的有效性")
    @GetMapping(value = "active/{id}")
    public Result<String> active(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/profiles/active/{id}, id=" + id);
        return super.active(id, null);
    }

    /**
     * 根据id获取数据详情
     *
     * @param id 数据主键
     * @return Result<ProfilesVO> 详情结果
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "detail/{id}")
    public Result<ProfilesVO> detail(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /sys/profiles/detail/{id}, id=" + id);
        return super.detail(id, null);
    }

    /**
     * 根据条件参数获取分页后的列表数据
     *
     * @param search 条件参数
     * @return Result<PageData<ProfilesVO>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "list")
    public Result<PageData<ProfilesVO>> list(@RequestBody @ApiParam(value = "条件参数", required = true) PageSearch<ProfilesVO> search) {
        log.info("====> /sys/profiles/list, search=" + JsonUtil.toJson(search));
        UserUtil.mustTeacher();
        Page<Profiles> page = Conditions.page(search, entityClass);
        QueryWrapper<Profiles> wrapper = Conditions.queryWrapper(search, entityClass);
        wrapper.orderByAsc("sort_index");
        service.page(page, wrapper);
        return super.success(toVOPage(page));
    }

    /**
     * 根据编号获取数据列表
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @GetMapping(value = "/list-by-code/{code}")
    public Result<List<ProfilesVO>> listByCode(@PathVariable("code") @ApiParam(value = "编号", required = true) String code) {
        log.info("====> /sys/profiles/list-by-code, code=" + code);
        return super.success(service.getList(code));
//        List<Profiles> list = service.lambdaQuery()
//                .eq(Profiles::getCode, code)
//                .eq(Profiles::getStatus, StatusEnum.EFFECTIVE.getCode())
//                .orderByAsc(Profiles::getSortIndex)
//                .list();
//        return super.success(super.toVO(list));
    }

}
