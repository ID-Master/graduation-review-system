package cn.edu.cuhk.mkt.service.impl.auth;

import cn.edu.cuhk.mkt.common.enums.AdSyncEnum;
import cn.edu.cuhk.mkt.common.enums.UserEnum;
import cn.edu.cuhk.mkt.common.util.AdUtil;
import cn.edu.cuhk.mkt.config.AdConfig;
import cn.edu.cuhk.mkt.entity.ad.AdStaffDTO;
import cn.edu.cuhk.mkt.entity.ad.AdStudentDTO;
import cn.edu.cuhk.mkt.entity.sys.AdStudentTemp;
import cn.edu.cuhk.mkt.entity.sys.AdTeacherTemp;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.service.auth.AdService;
import cn.edu.cuhk.mkt.service.sys.AdStudentTempService;
import cn.edu.cuhk.mkt.service.sys.AdTeacherTempService;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.uneed.common.core.lang.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author taokai
 */
@Slf4j
@Service
public class AdServiceImpl implements AdService {
    @Autowired
    private UserService userService;

    @Autowired
    private AdStudentTempService studentTempService;

    @Autowired
    private AdTeacherTempService teacherTempService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAdStudent() {
        log.info("------------- start 同步AD学生数据 -------------");
        Long startTime = System.currentTimeMillis();
        String batchNumber = UUIDUtil.gen32UUID();
        List<AdStudentTemp> studentTempList = handlerAdStudent(batchNumber);
        studentTempService.insertBatch(studentTempList);

        studentTempService.insertAdStudent(batchNumber);

        studentTempService.updateAdStudent(batchNumber);

        Long endTime = System.currentTimeMillis();
        log.info("------------- end 同步AD学生数据, 耗时：{} 秒 -------------", (endTime - startTime) / 1000);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAdTeacher() {
        log.info("------------- start 同步AD老师数据 -------------");
        Long startTime = System.currentTimeMillis();
        String batchNumber = UUIDUtil.gen32UUID();
        List<AdTeacherTemp> teacherTempList = handlerAdTeacher(batchNumber);
        teacherTempService.insertBatch(teacherTempList);

        teacherTempService.insertAdTeacher(batchNumber);

        teacherTempService.updateAdTeacher(batchNumber);

        Long endTime = System.currentTimeMillis();
        log.info("------------- end 同步AD老师数据, 耗时：{} 秒 -------------", (endTime - startTime) / 1000);
    }

    @Transactional(readOnly = true)
    public List<AdStudentTemp> handlerAdStudent(String batchNumber){
        List<AdStudentDTO> adStudentDTOList = AdUtil.getAdStudentList(AdConfig.getDomain(), AdConfig.getPort(), AdSyncEnum.STUDENT);

        List<AdStudentTemp> adStudentTempList = new ArrayList<>();
        Optional.ofNullable(adStudentDTOList).orElse(new ArrayList<>()).forEach(adStudentDTO -> {
            AdStudentTemp adStudentTemp = new AdStudentTemp();
            BeanUtils.copyProperties(adStudentDTO, adStudentTemp);
            adStudentTemp.setId(null);
            adStudentTemp.setBatchNumber(batchNumber);
            adStudentTempList.add(adStudentTemp);
        });

        return adStudentTempList;
    }

    @Transactional(readOnly = true)
    public List<AdTeacherTemp> handlerAdTeacher(String batchNumber){
        List<AdStaffDTO> adStaffDTOList = AdUtil.getAdStaffList(AdConfig.getDomain(), AdConfig.getPort(), AdSyncEnum.STAFF);

        List<AdTeacherTemp> adTeacherTempList = new ArrayList<>();
        Optional.ofNullable(adStaffDTOList).orElse(new ArrayList<>()).forEach(adStaffDTO -> {
            AdTeacherTemp adTeacherTemp = new AdTeacherTemp();
            BeanUtils.copyProperties(adStaffDTO, adTeacherTemp);
            adTeacherTemp.setId(null);
            adTeacherTemp.setBatchNumber(batchNumber);
            adTeacherTempList.add(adTeacherTemp);
        });

        return adTeacherTempList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initAdStudent() {
        log.info("------------------- start 初始AD化学生数据 -------------------");
        int count = userService.lambdaQuery().eq(User::getUserType, UserEnum.USER_TYPE.STUDENT.getCode()).count();
        if(count < 1){
            syncAdStudent();
        }
        log.info("------------------- end 初始AD化学生数据 -------------------");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initAdTeacher() {
        log.info("------------------- start 初始化AD老师数据 -------------------");
        int count = userService.lambdaQuery().eq(User::getUserType, UserEnum.USER_TYPE.TEACHER.getCode()).count();
        if(count < 1){
            syncAdTeacher();
        }
        log.info("------------------- end 初始化AD老师数据 -------------------");
    }

}
