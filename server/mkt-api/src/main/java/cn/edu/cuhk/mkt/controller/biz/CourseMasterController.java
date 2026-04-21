package cn.edu.cuhk.mkt.controller.biz;

import cn.edu.cuhk.mkt.common.consts.BizConst;
import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.common.util.ExcelUtil;
import cn.edu.cuhk.mkt.common.util.UserUtil;
import cn.edu.cuhk.mkt.converter.biz.CourseCategoryConverter;
import cn.edu.cuhk.mkt.converter.biz.CourseDetailConverter;
import cn.edu.cuhk.mkt.converter.biz.CourseMasterConverter;
import cn.edu.cuhk.mkt.converter.sys.UserConverter;
import cn.edu.cuhk.mkt.entity.biz.*;
import cn.edu.cuhk.mkt.entity.report.CourseExportDto;
import cn.edu.cuhk.mkt.entity.report.MktCourseReport;
import cn.edu.cuhk.mkt.entity.report.MktCourseReportHead;
import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.entity.sys.UserVO;
import cn.edu.cuhk.mkt.param.CommonParam;
import cn.edu.cuhk.mkt.param.biz.CourseMasterParam;
import cn.edu.cuhk.mkt.service.biz.CourseCategoryService;
import cn.edu.cuhk.mkt.service.biz.CourseDetailService;
import cn.edu.cuhk.mkt.service.biz.CourseMasterService;
import cn.edu.cuhk.mkt.service.biz.CourseTemplateService;
import cn.edu.cuhk.mkt.service.sys.InfoService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.support.api.Result;
import com.uneed.common.support.base.AbstractRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 业务模块-学生课程数据主表 前端控制器
 *
 * @author taok
 * @date 2021-08-13
 */
@Slf4j
@Api(tags = {"业务模块-学生课程数据主表"})
@RestController
@RequestMapping("${adminPath}/biz/course-master")
public class CourseMasterController extends AbstractRestController<CourseMasterService, CourseMaster, CourseMasterConverter, CourseMasterVO> {
    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseCategoryService courseCategoryService;

    @Autowired
    private CourseTemplateService courseTemplateService;

    @Autowired
    private InfoService infoService;

    /**
     * Next（下一步），学生保存课程数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "Next（下一步），学生保存课程数据", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "/next")
    public Result<CourseMasterVO> next(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/next, vo={}", JsonUtil.toJson(vo));
        service.next(vo);
        return super.success(vo);
    }

    /**
     * 根据专业编号获取学生课程明细数据
     *
     * @param code 数据主键
     * @return Result<CourseMasterVO> 详情结果
     */
    @ApiOperation(value = "根据专业编号获取学生课程明细数据", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = {"/detail/student", "/detail/student/{code}"})
    public Result<CourseMasterVO> detailStudent(@PathVariable(value = "code",required = false) @ApiParam(value = "课程分类编码") String code) {
        log.info("====> /biz/course-master/detail/student/{code}, code={}", code);
        UserUtil.mustStudent();
        UserInfo userInfo = UserSession.getUser();
        CourseMaster courseMaster = service.lambdaQuery().eq(CourseMaster::getStudentId, userInfo.getId())
                .eq(CourseMaster::getMajor, userInfo.getMajor())
                .eq(CourseMaster::getGrade, userInfo.getGrade())
                .one();
        if(courseMaster == null){
            log.warn("---------->课程主数据不存在");
            return super.success();
        }
        // 课程明细数据
        LambdaQueryChainWrapper<CourseDetail> queryWrapper = courseDetailService.lambdaQuery();
        queryWrapper.eq(CourseDetail::getCourseMasterId, courseMaster.getId());
        if(StringUtils.isNotBlank(code)){
            queryWrapper.eq(CourseDetail::getCourseCategoryCode, code);
        }
//        List<CourseDetail> courseDetailList = queryWrapper.orderByAsc(CourseDetail::getSortIndex).list();
        List<CourseDetail> courseDetailList = queryWrapper
//                .orderByAsc(CourseDetail::getCourseCode)
                .orderByAsc(CourseDetail::getSortIndex)
                .list();

        // 数据转换
        CourseMasterVO courseMasterVO = new CourseMasterConverter().toVO(courseMaster);
        courseMasterVO.setCourseDetailVOList(new CourseDetailConverter().toVO(courseDetailList));
        return super.success(courseMasterVO);
    }


    @ApiOperation(value = "学生submit课程数据", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "/student/check/submit")
    public Result<String> studentCheckSubmit(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/student/check/submit, vo={}", JsonUtil.toJson(vo));
        service.checkSubmit(vo);
        return super.success();
    }

    /**
     * 学生submit课程数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "学生submit课程数据", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "/student/submit")
    public Result<String> studentSubmit(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/student/submit, vo={}", JsonUtil.toJson(vo));
        service.submit(vo);
        return super.success();
    }

    /**
     * 老师获取学生课程详情数据
     *
     * @param id 数据主键
     * @return Result<CourseMasterVO> 详情结果
     */
    @ApiOperation(value = "老师获取学生课程详情数据", notes = "根据数据id，从数据库中获取其对应的数据详情")
    @GetMapping(value = "/detail/teacher/{id}")
    public Result<CourseMasterVO> detail(@PathVariable("id") @ApiParam(value = "数据id", required = true) String id) {
        log.info("====> /biz/course-master/detail/teacher/{id}, id={}" + id);
//        UserUtil.mustTeacher();

        CourseMaster courseMaster = service.getById(id);
        AssertUtil.isNull(courseMaster, "课程主数据不存在");
        // 课程明细数据
        List<CourseDetail> courseDetailList = courseDetailService.lambdaQuery()
                .eq(CourseDetail::getCourseMasterId, courseMaster.getId())
//                .orderByAsc(CourseDetail::getCourseCode)
                .orderByAsc(CourseDetail::getSortIndex)
                .list();
        //默认刚导入的学生根据专业查询课程
        if(courseDetailList.isEmpty()){
            CourseMasterVO vo = new CourseMasterVO();
            vo.setId(courseMaster.getId());
            vo.setMajor(courseMaster.getMajor());
            vo.setGrade(courseMaster.getGrade());
            return view(vo);
        }
        // 学生数据
        User student = userService.getById(courseMaster.getStudentId());
        AssertUtil.isNull(student, "学生数据不存在");
        UserVO studentVO = new UserConverter().toVO(student);
        //格式化审报毕业说明
        // formatNoteSeven(studentVO);

        // 课程分类列表
//        List<CourseCategory> categoryList = courseCategoryService.listByCourseMasterId(courseMaster.getId());
        String major = courseMaster.getMajor();
        String grade = courseMaster.getGrade();
        List<CourseCategory> categoryList = courseCategoryService.lambdaQuery()
                .eq(CourseCategory::getMajor, major)
                .eq(CourseCategory::getGrade, grade)
                .orderByAsc(CourseCategory::getSortIndex)
                .list();

        // 数据转换
        CourseMasterVO courseMasterVO = new CourseMasterConverter().toVO(courseMaster);
        courseMasterVO.setCourseDetailVOList(new CourseDetailConverter().toVO(courseDetailList));
        if(studentVO != null) {  //默认读取提交课程专业
            studentVO.setMajor(major);
        }
        courseMasterVO.setStudentVO(studentVO);
        courseMasterVO.setEditable(service.getStudentCourseEditable(courseMaster, null, null));
        List<CourseCategoryVO> courseCategoryVOList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(categoryList)){
            courseCategoryVOList.addAll(new CourseCategoryConverter().toVO(categoryList));
        }
        courseMasterVO.setCourseCategoryVOList(courseCategoryVOList);
        return super.success(courseMasterVO);
    }

    @ApiOperation(value = "老师获取学生课程详情数据", notes = "根据专业，从数据库中获取其对应的数据详情")
    @PostMapping(value = "/view/teacher")
    public Result<CourseMasterVO> view(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/view/, vo={}", JsonUtil.toJson(vo));
        //课程分类列表
        List<CourseCategory> categoryList = courseCategoryService.lambdaQuery()
                .eq(CourseCategory::getMajor, vo.getMajor())
                .eq(CourseCategory::getGrade, vo.getGrade())
                .orderByAsc(CourseCategory::getSortIndex)
                .list();
        //课程模板列表
        List<CourseDetailVO> sortList = Lists.newArrayList();
        List<String> courseCategoryIds = categoryList.stream().map(CourseCategory::getId).collect(Collectors.toList());
        if(courseCategoryIds.size() > 0) {
            List<CourseTemplate> courseTemplateList = courseTemplateService.lambdaQuery()
                    .in(CourseTemplate::getCourseCategoryId, courseCategoryIds)
                    .list();
            List<CourseDetailVO> courseDetailVOList = Lists.newArrayList();
            courseTemplateList.stream().forEach(item -> {
                CourseDetailVO courseDetailVO = new CourseDetailVO();
                courseDetailVO.setCourseCode(item.getCourseCode());
                courseDetailVO.setCourseTitle(item.getCourseTitle());
                courseDetailVO.setPart(item.getPart());
                courseDetailVO.setUnits(item.getUnits());
                courseDetailVO.setSelfCheck("");
                courseDetailVO.setRemark(item.getRemark());
                Optional<CourseCategory> category = categoryList.stream().filter(c -> c.getId().equals(item.getCourseCategoryId()) ).findFirst();
                if(category.isPresent()){
                    CourseCategory cc = category.get();
                    courseDetailVO.setCourseCategoryCode(cc.getCategoryCode());
                    courseDetailVO.setPart(cc.getPart());
                }
                courseDetailVOList.add(courseDetailVO);
            });
            sortList = courseDetailVOList.stream().sorted(Comparator.comparing(CourseDetailVO::getCourseCode)).collect(Collectors.toList());
        }

        // 数据转换
        String courseMasterId = vo.getId();
        CourseMasterVO courseMasterVO = new CourseMasterVO();
        if(courseMasterId != null && !courseMasterId.isEmpty()){
            CourseMaster courseMaster = service.getById(courseMasterId);
            courseMasterVO = new CourseMasterConverter().toVO(courseMaster);
            User student = userService.getById(courseMaster.getStudentId());
            courseMasterVO.setStudentVO(new UserConverter().toVO(student));
        }
        courseMasterVO.setCourseDetailVOList(sortList);
        courseMasterVO.setEditable(1);
        List<CourseCategoryVO> courseCategoryVOList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(categoryList)){
            courseCategoryVOList.addAll(new CourseCategoryConverter().toVO(categoryList));
        }
        courseMasterVO.setCourseCategoryVOList(courseCategoryVOList);
        return super.success(courseMasterVO);
    }

    /**
     * 老师审批课程数据
     *
     * @param vo VO对象
     * @return Result<String> 新增结果，并填充数据id
     */
    @ApiOperation(value = "老师审批课程数据", notes = "接收数据的VO对象，将该对象持久化到数据库中表")
    @PostMapping(value = "/teacher/approve")
    public Result<String> teacherApprove(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/teacher/approve, vo={}", JsonUtil.toJson(vo));
        service.approve(vo);
        return super.success();
    }

    /**
     * @param vo VO对象
     */
    @ApiOperation(value = "老师批量发送邮件", notes = "老师批量发送邮件")
    @PostMapping(value = "/teacher/email")
    public Result<String> teacherApprove(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) EmailRecordVO vo) {
        log.info("====> /biz/course-master/teacher/email, vo={}", JsonUtil.toJson(vo));
        service.email(vo);
        return super.success();
    }

    /**
     * 获取课程分页后数据列表
     *
     * @param param 条件参数
     * @return Result<PageData<StudentCourseVO>> 响应结果，并填充分页后的数据
     */
    @ApiOperation(value = "获取课程分页后数据列表", notes = "根据传入参数条件，从数据库中获取分页后的数据列表")
    @PostMapping(value = "list")
    public Result<PageData<StudentCourseVO>> list(@RequestBody @ApiParam(value = "条件参数", required = true) PageSearch<CourseMasterParam> param) {
        log.info("====> /biz/course-master/list, search={}", JsonUtil.toJson(param));
        UserUtil.mustTeacher();
        //构建分页对象
        Page<StudentCourseVO> page = new Page<>(param.getCurrent(), param.getSize());
        //执行查询操作
        page = service.listCourse(page, param.getCondition());
        return Result.ok(new PageData(page.getRecords(), page));
    }

    /**
     * 将数据导出为一个excel附件
     *
     */
    @ApiOperation(value = "数据导出接口", notes = "将数据导出成Excel文件")
    @SneakyThrows
    @PostMapping(value = "/export")
    public void export(@RequestBody CommonParam param, HttpServletResponse response) {
        log.info("====> /biz/course-master/export");
        String Separator = "-";
        try {
            UserUtil.mustTeacher();
            List<MktCourseReport> courseReportList = service.listCourseMktReport(param);
            //计算head
            MktCourseReportHead mktCourseReportHead = new MktCourseReportHead();
            mktCourseReportHead.setUcUnits("36");
            mktCourseReportHead.setSpUnits("30");
            mktCourseReportHead.setMrUnits("18");
            mktCourseReportHead.setMeaUnits("9-15");
            mktCourseReportHead.setMebUnits("");
            mktCourseReportHead.setFeUnits("18");
            if(courseReportList != null  &&  courseReportList.size() >0){
                String major = courseReportList.get(0).getMajor();
                String grade = courseReportList.get(0).getGrade();
                List<CourseCategory> courseCategoryList = courseCategoryService.lambdaQuery()
                        .eq(CourseCategory::getMajor, major)
                        .eq(CourseCategory::getGrade, grade)
                        .list();
                courseCategoryList.stream().forEach(item -> {
                    if(item.getCategoryCode().equals(BizConst.UNIVERSITY_CORE)){
                        mktCourseReportHead.setUcUnits(regexUnits(item.getTitle()));
                    }
                    if(item.getCategoryCode().equals(BizConst.SCHOOL_PACKAGE)){
                        mktCourseReportHead.setSpUnits(regexUnits(item.getTitle()));
                    }
                    if(item.getCategoryCode().equals(BizConst.MAJOR_REQUIRED_COURSES)){
                        mktCourseReportHead.setMrUnits(regexUnits(item.getTitle()));
                    }
                    if(item.getCategoryCode().equals(BizConst.MAJOR_ELECTIVE_COURSES) && item.getPart().equals(BizConst.PART_A)){
                        mktCourseReportHead.setMeaUnits(regexUnits(item.getTitle()));
                    }
                    if(item.getCategoryCode().equals(BizConst.MAJOR_ELECTIVE_COURSES) && item.getPart().equals(BizConst.PART_B)){
                        mktCourseReportHead.setMebUnits(regexUnits(item.getTitle()));
                    }
                    if(item.getCategoryCode().equals(BizConst.FREE_ELECTIVE)){
                        mktCourseReportHead.setFeUnits(regexUnits(item.getTitle()));
                    }
                });
            }
            //计算分数行
            courseReportList.stream().forEach(item -> {
                Integer mebLeft = 0;
                if(StringUtils.isNotBlank(item.getMebLeft())){
                    mebLeft = Integer.parseInt(item.getMebLeft());
                }
                item.setUcLeft(item.getUcLeft() - (item.getUcPass() + item.getUcNr() + item.getUcIp()));
                item.setSpLeft(item.getSpLeft() - (item.getSpPass() + item.getSpNr() + item.getSpIp()));
                item.setMrLeft(item.getMrLeft() - (item.getMrPass() + item.getMrNr() + item.getMrIp()));

                //Mea、Meb 特殊计算
                Integer meaTotal = item.getMeaPass() + item.getMeaNr() + item.getMeaIp();
                Integer mebTotal = item.getMebPass() + item.getMebNr() + item.getMebIp();
                String meaUnits = regexUnits(item.getMeaUnits());
                String mebUnits = regexUnits(item.getMebUnits());
                if(meaUnits.contains(Separator)){  //区间值计算
                    Integer meaMin = Integer.parseInt(meaUnits.split(Separator)[0]);
                    Integer meaMax = Integer.parseInt(meaUnits.split(Separator)[1]);
                    if(meaTotal >= meaMin){
                        item.setMeaLeft(0);
                    }else{
                        item.setMeaLeft(meaMin - meaTotal);
                    }
//                    Integer differ = (meaMax - meaTotal - mebTotal);
                    Integer differ = (meaMax - (meaTotal + item.getMeaLeft()) - mebTotal);
                    item.setMebLeft(differ.toString());
                    Integer mebMax = Integer.parseInt(mebUnits.split(Separator)[1]);
                    if(mebTotal >= mebMax){
                        item.setMebLeft("0");
                    }else if(differ > mebMax){
                        item.setMebLeft(mebMax.toString());
                    }
                }else{  //单个值计算
                    item.setMeaLeft(item.getMeaLeft() - meaTotal);
                    item.setMebLeft((mebLeft - mebTotal) + "");
                }

                item.setFeLeft(item.getFeLeft() - (item.getFePass() + item.getFeNr() + item.getFeIp()));
                //计算总分
                Integer passed = item.getUcPass() + item.getSpPass() + item.getMrPass() + item.getMeaPass() + item.getMebPass() + item.getFePass();
                Integer nr = item.getUcNr() + item.getSpNr() + item.getMrNr() + item.getMeaNr() + item.getMebNr() + item.getFeNr();
                Integer ip = item.getUcIp() + item.getSpIp() + item.getMrIp() + item.getMeaIp() + item.getMebIp() + item.getFeIp();
                Integer all = passed + nr + ip;
                item.setRemakrs(all.toString());
                item.setTotalUnitsPassed(passed.toString());
            });
            // 这里需要设置不关闭流
            ExcelUtil.exportCourseWithTemplate(mktCourseReportHead,courseReportList, response);
        } catch (Exception e) {
            log.error("导出excel文件失败，原因：" + e.getMessage(), e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Result result = Result.fail("下载文件失败，原因：" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(result));
        }
    }


    /**
     * 下载上传学生模板
     */
    @ApiOperation(value = "下载上传学生模板", notes = "下载上传学生模板")
    @SneakyThrows
    @GetMapping("/student/template")
    public void studentTemplate(HttpServletResponse response) {
        log.info("====> /biz/course-master/student/template");
        UserUtil.mustTeacher();
        ExcelUtil.writeExcel(response,"上传学生名单导入模板","Sheet1");
    }

    /**
     * 上传学生名单模板
     */
    @ApiOperation(value = "上传学生名单模板", notes = "上传学生名单模板")
    @SneakyThrows
    @PostMapping("/student/import")
    public Result<List<String>> studentImport(@RequestParam(value = "file" , required = true) MultipartFile file) {
        log.info("====> /biz/course-master/student/import");
        UserUtil.mustTeacher();
        return super.success(service.studentImport(file));
    }

    /**
     * 获取预计毕业学期
     */
    @ApiOperation(value = "获取预计毕业学期", notes = "获取预计毕业学期")
    @SneakyThrows
    @GetMapping("/student/expected-list")
    public Result<Map<String,String>> list() {
        log.info("====> /biz/course-master/student/expected-list");
        return Result.ok(service.getExpectedList());
    }

    /**
     * 批量更新状态接口
     */
    @ApiOperation(value = "批量更新状态接口", notes = "批量更新状态接口")
    @SneakyThrows
    @PostMapping(value = "/detail/teacher/update")
    public Result<String> status(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/teacher/update, vo={}", JsonUtil.toJson(vo));
        service.update(vo);
        return super.success();
    }

    /**
     * 批量更新Acknowledged状态接口
     */
    @ApiOperation(value = "批量更新Acknowledged状态接口", notes = "批量更新Acknowledged状态接口")
    @SneakyThrows
    @PostMapping(value = "/detail/teacher/acknowledged")
    public Result<String> acknowledged(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) CourseMasterVO vo) {
        log.info("====> /biz/course-master/detail/teacher/acknowledged, vo={}", JsonUtil.toJson(vo));
        service.acknowledged(vo);
        return super.success();
    }

    /**
     * 统计报表接口
     */
    @ApiOperation(value = "统计报表", notes = "统计报表")
    @SneakyThrows
    @PostMapping(value = "/detail/data/report-list")
    public Result<PageData<CourseExportVO>> reportList(@Valid @RequestBody @ApiParam(value = "数据对象", required = true) PageSearch<CourseExportVO> param) {
        log.info("====> /biz/course-master/teacher/report-list, vo={}", JsonUtil.toJson(param));
        UserUtil.mustTeacher();
        Page<CourseExportVO> page = new Page<>(param.getCurrent(), param.getSize());
        page = service.reportPage(page, param.getCondition());
        return Result.ok(new PageData(page.getRecords(), page));
    }

    /**
     * 统计报表接口
     */
    @ApiOperation(value = "统计报表下载接口", notes = "统计报表下载接口")
    @SneakyThrows
    @GetMapping(value = "/detail/data/export-list")
    public void exportList(HttpServletResponse response) {
        log.info("====> /biz/course-master/teacher/export-list");
        UserUtil.mustTeacher();
        CourseExportVO vo = new CourseExportVO();
        List<CourseExportVO> list = service.reportList(vo);
        List<CourseExportDto> dataList = Lists.newArrayList();
        list.stream().forEach(item -> {
            CourseExportDto dto = new CourseExportDto();
            dto.setGrade(item.getGrade());
            dto.setMajor(item.getMajor());
            dto.setTotal(item.getTotal());
            dto.setSubmitTotal(item.getSubmitTotal());
            dto.setUnfilledTotal(item.getUnfilledTotal());
            dto.setOfficerCheckedTotal(item.getOfficerCheckedTotal());
            dto.setSelfDeclarationTotal(item.getSelfDeclarationTotal());
            dataList.add(dto);
        });
        String fileName = "统计报表";
        String sheetName = "Sheet1";
        EasyExcel.write(ExcelUtil.getOutputStream(fileName,response, ExcelTypeEnum.XLSX) , CourseExportDto.class).sheet(sheetName).doWrite(dataList);
    }


    private String regexUnits(String title){
        if(StringUtils.isEmpty(title)){
            return "0";
        }
        String regEx="[^0-9\\-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(title);
        return m.replaceAll("").trim();
    }

    private void formatNoteSeven(UserVO studentVO){
        Info info = infoService.getOne();
        if(info != null){
            String expectedYear = studentVO.getExpectedYear();
            Map<String,String> expectedListMap = service.getExpectedList();
            String expectedYearDesc = expectedListMap.get(expectedYear);
            if(StringUtils.isNotEmpty(expectedYearDesc)){
                String[] expectedYearArray = expectedYearDesc.replaceFirst(" ",":").split(":");
                String noteSeven = info.getNoteSeven();
                String p1 = expectedYearArray[0];
                String p2 = expectedYearArray[1];
                String formatNoteSeven = noteSeven.replaceAll("\\d{4}-\\d{2}",p1).replaceAll("Term \\d{1}",p2);
                if(p2.equals("Term 1") || p2.equals("Term 2")){
                    formatNoteSeven = formatNoteSeven.replaceAll("第\\d{1}学", "第"+p2.split(" ")[1]+"学");
                }
                if(p2.equals("Summer")){
                    formatNoteSeven = formatNoteSeven.replaceAll("、第\\d{1}学期", "");
                }
                studentVO.setNoteSeven(formatNoteSeven);
            }else{
                studentVO.setNoteSeven(info.getNoteSeven());
            }
        }
    }
}
