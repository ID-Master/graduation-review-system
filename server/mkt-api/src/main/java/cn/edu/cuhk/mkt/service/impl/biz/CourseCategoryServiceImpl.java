package cn.edu.cuhk.mkt.service.impl.biz;

import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.common.util.UserUtil;
import cn.edu.cuhk.mkt.converter.biz.CourseCategoryConverter;
import cn.edu.cuhk.mkt.entity.biz.CourseCategory;
import cn.edu.cuhk.mkt.entity.biz.CourseCategoryVO;
import cn.edu.cuhk.mkt.entity.biz.CourseTemplate;
import cn.edu.cuhk.mkt.mapper.biz.CourseCategoryMapper;
import cn.edu.cuhk.mkt.param.biz.CourseCategoryParam;
import cn.edu.cuhk.mkt.service.biz.CourseCategoryService;
import cn.edu.cuhk.mkt.service.biz.CourseTemplateService;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.session.UserInfo;
import com.uneed.common.core.session.UserSession;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 业务模块-课程分类表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class CourseCategoryServiceImpl extends SuperServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Autowired
    private CourseTemplateService courseTemplateService;

    @Override
    public List<CourseCategory> listByCourseMasterId(String courseMasterId) {
        return mapper.listByCourseMasterId(courseMasterId);
    }

    @Override
    public List<CourseCategory> listByMajor(String major) {
        return mapper.listByMajor(major);
    }

    @Override
    @Transactional
    public Integer copy(CourseCategoryParam param){
        int flag = 0;
        String id = ObjectUtils.defaultIfNull(param.getId(), null);
        String major = ObjectUtils.defaultIfNull(param.getMajor(), null);
        String grade = ObjectUtils.defaultIfNull(param.getGrade(), null);
        AssertUtil.isNull(id, "课程分类Id不能为空");
        AssertUtil.isNull(major, "复制目标专业不能为空");
        AssertUtil.isNull(grade, "复制目标年级不能为空");
        CourseCategory courseCategory = getById(id);
        AssertUtil.isNull(courseCategory, "复制课程分类不存在");
        // 1.查询是否已存在课程分类
        CourseCategory one = lambdaQuery()
            .eq(CourseCategory::getMajor, major)
            .eq(CourseCategory::getGrade, grade)
            .eq(CourseCategory::getCategoryCode, courseCategory.getCategoryCode())
            .eq(CourseCategory::getPart, courseCategory.getPart())
            .one();
        AssertUtil.isNotEmpty(one, "目标课程分类已存在");
        // 2.复制课程
        UserInfo userInfo = UserUtil.getUserInfo();
        if(ObjectUtils.isNotEmpty(courseCategory)){
            String courseCategoryId = courseCategory.getId();
            courseCategory.setId(null);
            courseCategory.setCreatedBy(userInfo.getId());
            courseCategory.setUpdatedBy(userInfo.getId());
            courseCategory.setCreatedDate(LocalDateTime.now());
            courseCategory.setUpdatedDate(LocalDateTime.now());
            courseCategory.setMajor(major);
            courseCategory.setGrade(grade);
            //1.插入课程分类
            flag = insert(courseCategory);
            //2.插入课程明细
            List<CourseTemplate> courseTemplateList = courseTemplateService.lambdaQuery().in(CourseTemplate::getCourseCategoryId, courseCategoryId).list();
            if(!courseTemplateList.isEmpty()){
                courseTemplateList.forEach(item -> {
                    item.setId(null);
                    item.setCourseCategoryId(courseCategory.getId());
                    item.setCreatedBy(userInfo.getId());
                    item.setUpdatedBy(userInfo.getId());
                    item.setCreatedDate(LocalDateTime.now());
                    item.setUpdatedDate(LocalDateTime.now());
                });
                courseTemplateService.insertBatch(courseTemplateList);
            }
        }
        return flag;
    }

    /**
     * 复制课程分类
     * @return
     */
    @Override
    @Transactional
    public Integer copy(String id) {
        UserInfo userInfo = UserSession.getUser();
        int flag = 0;
        CourseCategory courseCategory = getById(id);
        if(ObjectUtils.isNotEmpty(courseCategory)){
            String courseCategoryId = courseCategory.getId();
            courseCategory.setId(null);
            courseCategory.setCreatedBy(userInfo.getId());
            courseCategory.setUpdatedBy(userInfo.getId());
            courseCategory.setCreatedDate(LocalDateTime.now());
            courseCategory.setUpdatedDate(LocalDateTime.now());

            String grade = courseCategory.getGrade();
            //查询最大年级
            CourseCategory one = lambdaQuery()
                    .eq(CourseCategory::getMajor, courseCategory.getMajor())
                    .eq(CourseCategory::getCategoryCode, courseCategory.getCategoryCode())
                    .eq(CourseCategory::getPart, courseCategory.getPart())
                    .eq(CourseCategory::getGrade, grade)
                    .orderByDesc(CourseCategory::getCreatedDate).one();
            if(ObjectUtils.isNotEmpty(one)){
                grade = one.getGrade();
            }
            int gradeValue = Integer.parseInt(grade) + 1;
            courseCategory.setGrade(Integer.toString(gradeValue));
            //1.插入课程分类
            flag = insert(courseCategory);
            //2.插入课程明细
            List<CourseTemplate> courseTemplateList = courseTemplateService.lambdaQuery().in(CourseTemplate::getCourseCategoryId, courseCategoryId).list();
            if(!courseTemplateList.isEmpty()){
                courseTemplateList.stream().forEach(item -> {
                    item.setId(null);
                    item.setCourseCategoryId(courseCategory.getId());
                    item.setCreatedBy(userInfo.getId());
                    item.setUpdatedBy(userInfo.getId());
                    item.setCreatedDate(LocalDateTime.now());
                    item.setUpdatedDate(LocalDateTime.now());
                });
                courseTemplateService.insertBatch(courseTemplateList);
            }
        }
        return flag;
    }

    /**
     * 查询学生当前课程分类
     */
    @Override
    public List<CourseCategoryVO> getStudentCategoryList(CourseCategoryParam param) {
        LambdaQueryChainWrapper<CourseCategory> query = lambdaQuery();
        String major = param.getMajor();
        String grade = param.getGrade();
        UserInfo userInfo = UserSession.getUser();
        boolean isStudent = UserUtil.isStudent();
        // if(ObjectUtils.isNotEmpty(userInfo) && isStudent) {
        //     // 查询是否为国际生
        //     Integer internationalStudent = ObjectUtils.defaultIfNull(userInfo.getInternationalStudent(), 0);
        //     major = userInfo.getMajor();
        //     grade = userInfo.getGrade();
        //     if(internationalStudent == 1) {
        //         query.notIn(CourseCategory::getCategoryCode, BizConst.CIVIC_EDUCATION_COURSE);
        //     }
        // }
        if(ObjectUtils.isNotEmpty(userInfo) && isStudent) {
            major = userInfo.getMajor();
            grade = userInfo.getGrade();
        }
        if(ObjectUtils.isEmpty(major) || ObjectUtils.isEmpty(grade)){
            return Lists.newArrayList();
        }
        query.eq(CourseCategory::getMajor, major).eq(CourseCategory::getGrade, grade);
        List<CourseCategory> list = query.orderByAsc(CourseCategory::getSortIndex).list();
        if(ObjectUtils.isNotEmpty(userInfo) && isStudent) {
            // 查询是否为国际生
            Integer internationalStudent = ObjectUtils.defaultIfNull(userInfo.getInternationalStudent(), 0);
            if(internationalStudent == 1){
                list = list.stream().filter(l -> l.getInternationalStudent().equals(internationalStudent) ).collect(Collectors.toList());
            }
        }
        list = list.stream().filter(distinctByKey(CourseCategory::getCategoryCode)).collect(Collectors.toList());
        return new CourseCategoryConverter().toVO(list);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }



}
