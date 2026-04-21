package cn.edu.cuhk.mkt.controller.biz;

import cn.edu.cuhk.mkt.common.consts.BizConst;
import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.common.util.UserUtil;
import cn.edu.cuhk.mkt.converter.biz.CourseTemplateConverter;
import cn.edu.cuhk.mkt.entity.biz.*;
import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.ProfilesVO;
import cn.edu.cuhk.mkt.param.CommonParam;
import cn.edu.cuhk.mkt.param.biz.CourseTemplateParam;
import cn.edu.cuhk.mkt.service.biz.CourseCategoryService;
import cn.edu.cuhk.mkt.service.biz.CourseDetailService;
import cn.edu.cuhk.mkt.service.biz.CourseMasterService;
import cn.edu.cuhk.mkt.service.biz.CourseTemplateService;
import cn.edu.cuhk.mkt.service.sys.InfoService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.session.UserInfo;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 业务模块-课程模板表 前端控制器
 *
 * @author taok
 * @date 2021-08-13
 */
@Slf4j
@Api(tags = {"业务模块-课程模板表"})
@RestController
@RequestMapping("${adminPath}/biz/course-template")
public class CourseTemplateController extends AbstractRestController<CourseTemplateService, CourseTemplate, CourseTemplateConverter, CourseTemplateVO> {
    @Autowired
    private CourseCategoryService courseCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseMasterService courseMasterService;

    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private InfoService infoService;

    /**
     * 新增数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "新增数据接口", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "insert")
    public Result<String> insert(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseTemplateVO vo) {
        log.info("====> /biz/course-template/insert, vo=" + JsonUtil.toJson(vo));
        // 判断课程分类是否存在
        CourseCategory courseCategory = courseCategoryService.getById(vo.getCourseCategoryId());
        AssertUtil.isNull(courseCategory, "课程分类id不存在");
        // 判断课程编号是否已存在
        int count = service.lambdaQuery()
                .eq(CourseTemplate::getCourseCategoryId, courseCategory.getId())
                .eq(CourseTemplate::getCourseCode, vo.getCourseCode())
                .count();
        if(count > 0){
            AssertUtil.isNull(null, "课程编号已存在");
        }
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
    public Result<String> update(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseTemplateVO vo) {
        log.info("====> /biz/course-template/update, vo=" + JsonUtil.toJson(vo));
        CourseTemplate courseTemplate = service.getById(vo.getId());
        AssertUtil.isNull(courseTemplate, "课程模板id不存在");
        // 不允许修改字段
        vo.setCourseCategoryId(null);
        courseTemplate = new CourseTemplateConverter().toEntity(vo);
        service.update(courseTemplate);
        return super.success();
    }

    /**
     * 根据id获取数据详情
     *
     * @param id 数据主键
     * @return Result<CourseTemplateVO> 详情结果
     */
    @ApiOperation(value = "获取数据详情接口", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "detail/{id}")
    public Result<CourseTemplateVO> detail(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /biz/course-template/detail/{id}, id=" + id);
        return super.detail(id, null);
    }

    /**
     * 根据条件参数获取分页后的列表数据
     *
     * @param search 条件参数
     * @return Result<PageData<CourseTemplateVO>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "获取数据列表接口", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "list")
    public Result<PageData<CourseTemplateVO>> list(@RequestBody @ApiParam(value = "条件参数", required = true) PageSearch<CourseTemplateVO> search) {
        log.info("====> /biz/course-template/list, search=" + JsonUtil.toJson(search));
        Page<CourseTemplate> page = Conditions.page(search, CourseTemplate.class);
        LambdaQueryWrapper<CourseTemplate> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(search.getCondition().getCourseCategoryId())){
            queryWrapper.eq(CourseTemplate::getCourseCategoryId, search.getCondition().getCourseCategoryId());
        }
//        queryWrapper.orderByDesc(CourseTemplate::getSortIndex);
        queryWrapper.orderByAsc(CourseTemplate::getCourseCode);
        service.page(page, queryWrapper);
        PageData<CourseTemplateVO> pageData = toVOPage(page);
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
        log.info("====> /biz/course-template/remove-by-ids, param=" + JsonUtil.toJson(param));
        AssertUtil.isEmpty(param.getIds(), "id集合不能为空");
        service.removeByIds(param.getIds());
        return super.success("删除成功");
    }

    /**
     * 根据条件获取课程模板数据列表
     *
     * @param search 条件参数
     * @return Result<List<CourseTemplate>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "根据条件获取课程模板数据列表", notes = "根据传入参数条件，从数据库中获取数据列表")
    @PostMapping(value = "list-by-condition")
    public Result<Object> listByCondition(@RequestBody @ApiParam(value = "条件参数", required = true) CourseTemplateParam search) {
        UserUtil.mustStudent();
        String categoryCode = StringUtils.defaultString(search.getCategoryCode());
        UserInfo userInfo = UserSession.getUser();
        search.setMajor(userInfo.getMajor());
        search.setGrade(userInfo.getGrade());
        log.info("====> /biz/course-template/list-by-condition, search=" + JsonUtil.toJson(search));
        List<CourseTemplateVO> courseTemplateVOList = null;
        // 课程模板数据
        List<CourseTemplate> courseTemplateList = service.listCourseTemplateByCondition(search);
        // 特殊课程
        List<String> excludeCodeList = getExcludeCode();
        if(CollectionUtils.isNotEmpty(courseTemplateList)){
            courseTemplateVOList = new CourseTemplateConverter().toVO(courseTemplateList);
            List<String> courseCodes = Lists.newArrayList();
            if(BizConst.MAJOR_REQUIRED_COURSES.equals(categoryCode) || BizConst.MAJOR_ELECTIVE_COURSES.equals(categoryCode)){
                CourseMaster courseMaster = courseMasterService.lambdaQuery().eq(CourseMaster::getStudentId, userInfo.getId())
                        .eq(CourseMaster::getMajor, userInfo.getMajor())
                        .eq(CourseMaster::getGrade, userInfo.getGrade())
                        .one();
                if(courseMaster != null){
                    List<CourseDetail> courseDetailList = courseDetailService.lambdaQuery()
                        .eq(CourseDetail::getCourseMasterId, courseMaster.getId())
                        .notIn(CourseDetail::getSelfCheck, "0","")
                        .notIn(CourseDetail::getCourseCategoryCode, categoryCode)
                        .list();
                    Optional.ofNullable(courseDetailList).orElse(Lists.newArrayList()).forEach(courseDetail -> {
                        courseCodes.add(courseDetail.getCourseCode());
                    });
                }
            }
            List<String> excludeCode1 = Lists.newArrayList("ENG3001", "PED2002", "FRN1001", "MAT4001");
            Iterator<CourseTemplateVO> iterator = courseTemplateVOList.iterator();
            while (iterator.hasNext()) {
                CourseTemplateVO courseTemplateVO = iterator.next();
                courseTemplateVO.setSelfCheck("0");
                String courseCode = StringUtils.defaultString(courseTemplateVO.getCourseCode());
                // 自有选修需排除的课程编码
                if(BizConst.FREE_ELECTIVE.equals(categoryCode)){
                    if(excludeCode1.contains(courseCode)){
                        log.info("categoryCode: {}, 被排除课程编号: {}]", categoryCode, courseCode);
                        iterator.remove();
                    }
                }
                if(BizConst.MAJOR_REQUIRED_COURSES.equals(categoryCode) || BizConst.MAJOR_ELECTIVE_COURSES.equals(categoryCode)){
                    // if(courseCodes.contains(courseCode) && excludeCodeList.contains(courseCode)){
                    if(courseCodes.contains(courseCode) && courseCode.endsWith("*")){
                        log.info("categoryCode: {}, 被排除课程编号: {}]", categoryCode, courseCode);
                        iterator.remove();
                    }
                }
            }
        }
        return success(courseTemplateVOList);
    }

    public List<String> getExcludeCode(){
        // 学生在学院课程部分已勾DMS2030*，MKT2010*，MGT2020*课程，需在专业选修课程部分排除掉
        List<String> excludeCodeList = Lists.newArrayList("DMS2030*", "MKT2010*", "MGT2020*","DMS2051*","MGT4188*","MGT4020*","MGT4189*");
        Info info = infoService.getOne();
        if(info != null){
            String codeList = info.getCodeList();
            if(codeList != null && !codeList.isEmpty()){
                excludeCodeList = Arrays.asList(codeList.split("\n"));
            }
        }
        log.info("excludeCodeList ------>" + String.join("," , excludeCodeList));
        return excludeCodeList;
    }

}
