package cn.edu.cuhk.mkt.service.impl.biz;

import cn.edu.cuhk.mkt.common.consts.BizConst;
import cn.edu.cuhk.mkt.common.enums.BizCodeEnum;
import cn.edu.cuhk.mkt.common.enums.CourseMasterEnum;
import cn.edu.cuhk.mkt.common.enums.EmailStatusEnum;
import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.common.util.SpringContextUtil;
import cn.edu.cuhk.mkt.common.util.UserUtil;
import cn.edu.cuhk.mkt.config.MailConfig;
import cn.edu.cuhk.mkt.converter.biz.CourseMasterConverter;
import cn.edu.cuhk.mkt.converter.biz.EmailRecordConverter;
import cn.edu.cuhk.mkt.entity.biz.*;
import cn.edu.cuhk.mkt.entity.mail.MailDTO;
import cn.edu.cuhk.mkt.entity.report.MktCourseReport;
import cn.edu.cuhk.mkt.entity.report.MktUserDto;
import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.listener.MktUserListener;
import cn.edu.cuhk.mkt.mapper.biz.CourseMasterMapper;
import cn.edu.cuhk.mkt.param.CommonParam;
import cn.edu.cuhk.mkt.param.biz.CourseMasterParam;
import cn.edu.cuhk.mkt.service.biz.CourseDetailService;
import cn.edu.cuhk.mkt.service.biz.CourseMasterService;
import cn.edu.cuhk.mkt.service.biz.EmailRecordService;
import cn.edu.cuhk.mkt.service.common.EmailService;
import cn.edu.cuhk.mkt.service.sys.InfoService;
import cn.edu.cuhk.mkt.service.sys.ProfilesService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.mybatis.base.SuperServiceImpl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务模块-学生课程数据主表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class CourseMasterServiceImpl extends SuperServiceImpl<CourseMasterMapper, CourseMaster> implements CourseMasterService {
    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private EmailRecordService emailRecordService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseMasterService courseMasterService;

    @Autowired
    private ProfilesService profilesService;

    @Autowired
    private InfoService infoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int next(CourseMasterVO courseMasterVO) {
        // 基础数据校验
        UserUtil.mustStudent();
        UserInfo userInfo = UserSession.getUser();
        int nextResult = 0;
        String courseMasterId = null;
        List<CourseDetailVO> courseDetailVOList = courseMasterVO.getCourseDetailVOList();

        // 判断学生是否有课程数据记录
        CourseMaster courseMaster = super.lambdaQuery().eq(CourseMaster::getStudentId, userInfo.getId())
                .eq(CourseMaster::getMajor, userInfo.getMajor())
                .eq(CourseMaster::getGrade, userInfo.getGrade())
                .one();
        if(courseMaster != null){
            courseMasterId = courseMaster.getId();
            Integer status = courseMaster.getStatus();
            // 待审核
            if(CourseMasterEnum.STATUS.REVIEWED.getCode().equals(status)){
                AssertUtil.isNull(null, "审核中，不允许修改");
            }
            // 待审核
            else if(CourseMasterEnum.STATUS.OFFICE_CHECKED.getCode().equals(status)){
                AssertUtil.isNull(null, "已完成，不允许修改");
            }else {
                //1.预计毕业年份发生改变
                Info info = infoService.getOne();
                if(info != null){
                    courseMaster.setSelfDeclaration(info.getNoteSeven());
                    nextResult = super.update(courseMaster);
                }
            }
        }else {
            // 新增课程主数据
            courseMaster = new CourseMasterConverter().toEntity(courseMasterVO);
            courseMaster.setId(null);
            courseMaster.setStatus(CourseMasterEnum.STATUS.DRAFT.getCode());
            courseMaster.setStudentId(userInfo.getId());
            courseMaster.setStudentSubmitTime(new Date());
            courseMaster.setMajor(userInfo.getMajor());
            courseMaster.setGrade(userInfo.getGrade());
            courseMaster.setCreatedDate(LocalDateTime.now());
            courseMaster.setUpdatedDate(LocalDateTime.now());
            // 保存数据时记录自我声明
            Info info = infoService.getOne();
            if(info != null){
                courseMaster.setSelfDeclaration(info.getNoteSeven());
            }
            nextResult = super.insert(courseMaster);
            courseMasterId = courseMaster.getId();
            courseMasterVO.setId(courseMasterId);
        }
        //过滤为空的
        List<CourseDetailVO> saveList = Lists.newArrayList();
        List<String> filterCategoryCode = Lists.newArrayList(
            BizConst.CIVIC_EDUCATION_COURSE,
            BizConst.UNIVERSITY_CORE,
            BizConst.SCHOOL_PACKAGE,
            BizConst.MAJOR_REQUIRED_COURSES,
            BizConst.MAJOR_ELECTIVE_COURSES,
            BizConst.FREE_ELECTIVE)
        ;
        courseDetailVOList.stream().forEach(item -> {
            String courseCategoryCode = item.getCourseCategoryCode();
            String courseCode = item.getCourseCode();
            if(StringUtils.isNotBlank(courseCode) && filterCategoryCode.contains(courseCategoryCode)){
                saveList.add(item);
            }
        });
        // 保存（新增或修改）课程明细数据
        courseDetailService.save(courseMasterId, saveList);
        return nextResult;
    }

    @Override
    public Page<StudentCourseVO> listCourse(Page<StudentCourseVO> page, CourseMasterParam condition) {
        return mapper.listCourseByCondition(page, condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(CourseMasterVO courseMasterVO) {
        // 数据校验
        UserUtil.mustStudent();
        UserInfo userInfo = UserSession.getUser();
        String id = courseMasterVO.getId();
        CourseMaster courseMaster = null;
        if(StringUtils.isBlank(id)){
            courseMaster = super.lambdaQuery().eq(CourseMaster::getStudentId, userInfo.getId()).eq(CourseMaster::getMajor, userInfo.getMajor()).one();
        }else {
            courseMaster = super.getById(id);
        }
        AssertUtil.isNull(courseMaster, "课程数据不存在");

        List<Integer> allowStatus = new ArrayList<>();
        allowStatus.add(CourseMasterEnum.STATUS.DRAFT.getCode());
        allowStatus.add(CourseMasterEnum.STATUS.REJECTED.getCode());

//        AssertUtil.isFalse(allowStatus.contains(courseMaster.getStatus()), "不允许提交");

        if(allowStatus.contains(courseMaster.getStatus())){
            // 学生复选框
//          courseMaster.setStudentCheckBox(courseMasterVO.getStudentCheckBox());
            courseMaster.setStudentSubmitTime(new Date());
            // 签名文件
            courseMaster.setFileId(courseMasterVO.getFileId());
            courseMaster.setSignatureUrl(courseMasterVO.getSignatureUrl());
            // 签字日期
            courseMaster.setSignatureDate(courseMasterVO.getSignatureDate());
            courseMaster.setStatus(CourseMasterEnum.STATUS.REVIEWED.getCode());
            courseMaster.setEnglishSchoolReport(courseMasterVO.getEnglishSchoolReport());
            int updateResult = super.update(courseMaster);
            return updateResult;
        }else{
            return 1;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int checkSubmit(CourseMasterVO courseMasterVO) {
        // 数据校验
        UserUtil.mustStudent();
        UserInfo userInfo = UserSession.getUser();
        String id = courseMasterVO.getId();
        CourseMaster courseMaster = null;
        if(StringUtils.isBlank(id)){
            courseMaster = super.lambdaQuery().eq(CourseMaster::getStudentId, userInfo.getId()).eq(CourseMaster::getMajor, userInfo.getMajor()).one();
        }else {
            courseMaster = super.getById(id);
        }
        AssertUtil.isNull(courseMaster, "课程数据不存在");
        // 学生复选框
        Integer studentCheckBox = courseMasterVO.getStudentCheckBox();
        if(studentCheckBox != null){
            courseMaster.setStudentCheckBox(studentCheckBox);
        }
        // 学生反馈
        String studentCheckFeedback = courseMasterVO.getStudentCheckFeedback();
        if(studentCheckFeedback != null && !studentCheckFeedback.isEmpty()){
            courseMaster.setStudentCheckFeedback(studentCheckFeedback);
        }
        //学生签名
        String signatureUrl= courseMasterVO.getSignatureUrl();
        if(signatureUrl != null && !signatureUrl.isEmpty()){
            courseMaster.setSignatureUrl(signatureUrl);
        }
        //英文成绩单
        String englishSchoolReport= courseMasterVO.getEnglishSchoolReport();
        if(englishSchoolReport != null && !englishSchoolReport.isEmpty()){
            courseMaster.setEnglishSchoolReport(englishSchoolReport);
        }
        //填写日期
        Date signatureDate= courseMasterVO.getSignatureDate();
        if(signatureDate != null){
            courseMaster.setSignatureDate(signatureDate);
        }

        int updateResult = super.update(courseMaster);
        return updateResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(CourseMasterVO courseMasterVO) {
        // 数据校验
        UserUtil.mustTeacher();
        UserInfo userInfo = UserSession.getUser();
        String id = courseMasterVO.getId();
        CourseMaster courseMaster = super.getById(id);
        AssertUtil.isNull(courseMaster, "课程数据不存在");

        List<Integer> allowStatus = new ArrayList<>();
        allowStatus.add(CourseMasterEnum.STATUS.REVIEWED.getCode());
        AssertUtil.isFalse(allowStatus.contains(courseMaster.getStatus()), "不满足审核条件");

        String action = courseMasterVO.getAction();
        AssertUtil.isBlank(action, "审核参数不能为空");
        Optional<CourseMasterEnum.ACTIOIN> actionEnum = EnumSet.allOf(CourseMasterEnum.ACTIOIN.class)
                .stream()
                .filter(e -> e.getCode().equals(action))
                .findFirst();
        AssertUtil.isFalse(actionEnum.isPresent(), "审核参数不存在");

        // 同意
        if(action.equals(CourseMasterEnum.ACTIOIN.AGREE.getCode())){
            courseMaster.setStatus(CourseMasterEnum.STATUS.OFFICE_CHECKED.getCode());
        }
        // 驳回
        else if(action.equals(CourseMasterEnum.ACTIOIN.REJECT.getCode())){
            courseMaster.setStatus(CourseMasterEnum.STATUS.REJECTED.getCode());

            // 驳回发送邮件通知
            EmailRecordVO emailVO = courseMasterVO.getEmailVO();
            sendEmail(emailVO);
        }

        // 保存数据
        courseMaster.setTeacherId(userInfo.getId());
        courseMaster.setTeacherApproveTime(new Date());
        courseMaster.setTeacherCheckBox(courseMasterVO.getTeacherCheckBox());
        courseMaster.setReason(courseMasterVO.getReason());
        courseMaster.setOfficerCheckedDate(courseMasterVO.getOfficerCheckedDate());

        int updateResult = super.update(courseMaster);
        log.info("--------------- 老师审批课程事务执行完成 ---------------");
        return updateResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(CourseMasterVO courseMasterVO) {
        Integer status =  courseMasterVO.getStatus();
        List<String> ids = courseMasterVO.getIds();
        List<CourseMaster> courseMasterList =  getByIds(ids);
        //过滤
        courseMasterList = courseMasterList.stream().filter(item -> !Lists.newArrayList(4).contains(item.getStatus())).collect(Collectors.toList());
        courseMasterList.stream().forEach(item -> {
            item.setStatus(status);
        });
        if(courseMasterList.size() > 0){
            return super.updateBatch(courseMasterList);
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int acknowledged(CourseMasterVO courseMasterVO) {
        Integer teacherCheckBox =  courseMasterVO.getTeacherCheckBox();
        List<String> ids = courseMasterVO.getIds();
        if(ObjectUtils.isNotEmpty(teacherCheckBox) && ObjectUtils.isNotEmpty(ids)) {
            List<CourseMaster> courseMasterList =  getByIds(ids);
            List<Integer> statusList = Lists.newArrayList(1);
            courseMasterList.stream().forEach(item -> {
                // 待审核的申请单
                if(statusList.contains(item.getStatus())) {
                    item.setTeacherCheckBox(teacherCheckBox);
                    item.setOfficerCheckedDate(new Date());
                    item.setStatus(3);
                }
            });
            if(courseMasterList.size() > 0){
                return super.updateBatch(courseMasterList);
            }
        }
        return 0;
    }

    @Override
    public List<MktCourseReport> listCourseMktReport(CommonParam param){
        return this.mapper.listCourseMktReport(param);
    }

    @Override
    public int getStudentCourseEditable(CourseMaster courseMaster, String studentId, String courseMasterId) {
        int editable = 0;
        if(courseMaster == null){
            if(StringUtils.isNotBlank(courseMasterId)){
                courseMaster = super.getById(courseMasterId);
            }
            if(StringUtils.isNotBlank(studentId) && courseMaster == null){
                User user = userService.getById(studentId);
                String major = user.getMajor();
                String grade = user.getGrade();
                courseMaster = super.lambdaQuery().eq(CourseMaster::getStudentId, studentId)
                        .eq(CourseMaster::getMajor, major)
                        .eq(CourseMaster::getGrade, grade)
                        .one();
            }
        }
        if(courseMaster != null){
            List<Integer> allowStatus = new ArrayList<>();
            allowStatus.add(CourseMasterEnum.STATUS.DRAFT.getCode());
            allowStatus.add(CourseMasterEnum.STATUS.REJECTED.getCode());
            if(allowStatus.contains(courseMaster.getStatus())){
                editable = 1;
            }
        }
        return editable;
    }

    @Override
    public Map<String,String> getExpectedList() {
        Map<String,String> map = Maps.newHashMap();
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        for(int i=-1; i<5; i++){
            String yyyy = today.plusYears(i).format(formatter);
            String prefix =  yyyy.substring(2,4);
            int next = Integer.parseInt(prefix) + 1;
            map.put(prefix + "10", yyyy + "-" + next + " Term 1");
            map.put(prefix + "20", yyyy + "-" + next + " Term 2");
            map.put(prefix + "50", yyyy + "-" + next + " Summer");
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void email(EmailRecordVO emailRecordVO) {
        sendEmail(emailRecordVO);
    }

    @Override
    public List<String> studentImport(MultipartFile file) {
        try {
            MktUserListener mktUserListener = new MktUserListener(userService,courseMasterService,profilesService);
            EasyExcel.read(file.getInputStream(), MktUserDto.class, mktUserListener).sheet().doRead();
        } catch (IOException e) {
            String message = "导入excel附件失败！文件名[" + file.getOriginalFilename() + "]，原因：" + e.getMessage();
            log.error(message, e);
        }
        return Lists.newArrayList();
    }

    @Override
    public Page<CourseExportVO> reportPage(Page<CourseExportVO> page, CourseExportVO condition) {
        return this.mapper.reportPage(page, condition);
    }

    @Override
    public List<CourseExportVO> reportList(CourseExportVO condition) {
        return this.mapper.reportList(condition);
    }

    @Override
    public List<CourseMaster> getByIds(List<String> ids) {
        return lambdaQuery().in(CourseMaster::getId, ids).list();
    }

    public void sendEmail(EmailRecordVO emailVO){
        AssertUtil.isNull(emailVO, "邮件不能为空");
        AssertUtil.isBlank(emailVO.getMailTo(), "接收人不能为空");
        AssertUtil.isBlank(emailVO.getSubject(), "主题不能为空");

        String[] tos = emailVO.getMailTo().split(",|;|，|；");
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
              @Override
              public void afterCommit() {
                  ThreadPoolTaskExecutor taskExecutor = SpringContextUtil.getBean(ThreadPoolTaskExecutor.class);
                  taskExecutor.submit(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Arrays.asList(tos).stream().forEach(item-> buildEmail(emailVO, new String[]{item}));
                          }catch (Exception e){
                              log.error("----------->邮件发送失败: {}", e.getMessage(), e);
                          }
                      }
                  });
              }
          });
    }

    @Transactional
    public void buildEmail(EmailRecordVO emailVO,String[] tos){
        EmailRecord emailRecord = new EmailRecordConverter().toEntity(emailVO);
        emailRecord.setBizId(emailVO.getBizId());
        emailRecord.setBizType(BizCodeEnum.COURSE_MASTER.getCode());
        emailRecord.setMailFrom(MailConfig.getFrom());
        emailRecord.setStatus(EmailStatusEnum.WAIT_SEND.getCode());
        emailRecord.setRetrys(0);
        emailRecordService.insert(emailRecord);
        log.info("--------------- 老师审批课程事务提交完成后执行 ---------------");
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTos(tos);
        mailDTO.setSubject(emailVO.getSubject());
        String content = StringUtils.defaultString(emailVO.getContent());
        mailDTO.setContent(content);
        emailService.htmlEmail(mailDTO);
        log.info("--------------- 邮件发送成功: {} ---------------", JSON.toJSONString(mailDTO));
        emailRecord.setStatus(EmailStatusEnum.SUCCESS.getCode());
        emailRecordService.update(emailRecord);
    }

}
