package cn.edu.cuhk.mkt.controller.biz;

import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.converter.biz.CourseCategoryConverter;
import cn.edu.cuhk.mkt.entity.biz.CourseCategory;
import cn.edu.cuhk.mkt.entity.biz.CourseCategoryVO;
import cn.edu.cuhk.mkt.param.CommonParam;
import cn.edu.cuhk.mkt.param.biz.CourseCategoryParam;
import cn.edu.cuhk.mkt.param.biz.CourseTemplateParam;
import cn.edu.cuhk.mkt.service.biz.CourseCategoryService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.mybatis.utils.Conditions;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 业务模块-课程分类表 前端控制器
 *
 * @author taok
 * @date 2021-08-13
 */
@Slf4j
@Api(tags = {"业务模块-课程分类表"})
@RestController
@RequestMapping("${adminPath}/biz/course-category")
public class CourseCategoryController extends AbstractRestController<CourseCategoryService, CourseCategory, CourseCategoryConverter, CourseCategoryVO> {
    @Autowired
    private UserService userService;

    /**
     * 新增数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "新增数据接口", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "insert")
    public Result<String> insert(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseCategoryVO vo) {
        log.info("====> /biz/course-category/insert, vo=" + JsonUtil.toJson(vo));
        int count = service.lambdaQuery()
                .eq(CourseCategory::getMajor, vo.getMajor())
                .eq(CourseCategory::getCategoryCode, vo.getCategoryCode())
                .eq(CourseCategory::getPart, vo.getPart())
                .eq(CourseCategory::getGrade, vo.getGrade())
                .count();
        log.info("----------------->"+ count);
        AssertUtil.isGtZero(count, "专业和模板编号已存在");
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
    public Result<String> update(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseCategoryVO vo) {
        log.info("====> /biz/course-category/update, vo=" + JsonUtil.toJson(vo));
        int count = service.lambdaQuery()
                .eq(CourseCategory::getMajor, vo.getMajor())
                .eq(CourseCategory::getCategoryCode, vo.getCategoryCode())
                .eq(CourseCategory::getPart, vo.getPart())
                .eq(CourseCategory::getGrade, vo.getGrade())
                .notIn(CourseCategory::getId, vo.getId())
                .count();
        log.info("----------------->"+ count);
        AssertUtil.isGtZero(count, "专业和模板编号已存在");
        return super.update(vo, null);
    }

    /**
     * 根据id获取数据详情
     *
     * @param id 数据主键
     * @return Result<CourseCategoryVO> 详情结果
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "detail/{id}")
    public Result<CourseCategoryVO> detail(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /biz/course-category/detail/{id}, id=" + id);
        return super.detail(id, null);
    }

    /**
     * 根据条件参数获取分页后的列表数据
     *
     * @param search 条件参数
     * @return Result<PageData<CourseCategoryVO>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "list")
    public Result<PageData<CourseCategoryVO>> list(@RequestBody @ApiParam(value = "条件参数", required = true) PageSearch<CourseCategoryParam> search) {
        log.info("====> /biz/course-category/list, search=" + JsonUtil.toJson(search));
        Page<CourseCategory> page = Conditions.page(search, CourseCategory.class);
        if(search.getCondition() == null){
            search.setCondition(new CourseCategoryParam());
        }
        // 构造分页查询对象
        LambdaQueryWrapper<CourseCategory> queryWrapper = Wrappers.lambdaQuery();
        // 主修课程
        if(StringUtils.isNotBlank(search.getCondition().getMajor())){
            queryWrapper.eq(CourseCategory::getMajor, search.getCondition().getMajor());
        }
        // 模板编码/名称
        if(StringUtils.isNotBlank(search.getCondition().getTemplateKeyword())){
            queryWrapper.and(wrapper ->
                wrapper.like(CourseCategory::getCategoryCode, search.getCondition().getTemplateKeyword())
                    .or()
                    .like(CourseCategory::getCategoryName, search.getCondition().getTemplateKeyword())
            );
        }
        //年级
        if(StringUtils.isNotBlank(search.getCondition().getGrade())){
            queryWrapper.eq(CourseCategory::getGrade, search.getCondition().getGrade());
        }
        queryWrapper.orderByDesc(CourseCategory::getGrade)
                .orderByDesc(CourseCategory::getMajor)
                .orderByAsc(CourseCategory::getSortIndex);
        service.page(page, queryWrapper);
        PageData<CourseCategoryVO> pageData = toVOPage(page);
        userService.fillUserData(pageData.getRecords());
        return super.success(pageData);
    }

    /**
     * 根据id集合批量删除
     *
     * @param param 参数对象
     * @return Result<String> 批量删除结果
     */
    @ApiOperation(value = "根据id集合批量删除", notes = "根据id集合批量删除")
    @PostMapping(value = "remove-by-ids")
    public Result<String> removeByIds(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CommonParam param) {
        log.info("====> /biz/course-category/remove-by-ids, param=" + JsonUtil.toJson(param));
        AssertUtil.isEmpty(param.getIds(), "id集合不能为空");
        service.removeByIds(param.getIds());
        return super.success("删除成功");
    }

    /**
     * 根据条件获取课程分类数据
     *
     * @param search 条件参数
     * @return Result<CourseCategory> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "根据条件获取课程分类数据", notes = "根据传入参数条件，从数据库中获取数据列表")
    @PostMapping(value = "get-by-condition")
    public Result<List<CourseCategory>> getByCondition(@RequestBody @ApiParam(value = "条件参数", required = true) CourseTemplateParam search) {
        log.info("====> /biz/course-category/get-by-condition, search=" + JsonUtil.toJson(search));
        if(StringUtils.isBlank(search.getMajor())){
            search.setMajor(UserSession.getUser().getMajor());
        }
        if(StringUtils.isBlank(search.getGrade())){
            search.setGrade(UserSession.getUser().getGrade());
        }
        List<CourseCategory> list = service.lambdaQuery()
                .eq(CourseCategory::getMajor, search.getMajor())
                .eq(CourseCategory::getGrade, search.getGrade())
                .eq(CourseCategory::getCategoryCode, search.getCategoryCode()).list();
        return success(list);
    }

    /**
     * 根据id复制数据
     *
     * @param id 数据主键
     * @return Result<CourseCategoryVO> 详情结果
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @PostMapping(value = "copy")
    public Result<Integer> copy(@RequestBody @ApiParam(value = "数据id", required = true) CourseCategoryParam param) {
        log.info("====> /biz/course-category/copy, param=" + JsonUtil.toJson(param));
        return success(service.copy(param));
    }

    /*
     * 查询动态课程分类列表
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "get-current-list")
    public Result<List<CourseCategoryVO>> getCurrentList(@RequestBody @ApiParam(value = "条件参数", required = true) CourseCategoryParam param) {
        log.info("====> /biz/course-category/get-current-list, search=" + JsonUtil.toJson(param));
        return success(service.getStudentCategoryList(param));
    }


}
