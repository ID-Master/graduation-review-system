package cn.edu.cuhk.mkt.service.impl.biz;

import cn.edu.cuhk.mkt.common.consts.BizConst;
import cn.edu.cuhk.mkt.common.util.AssertUtil;
import cn.edu.cuhk.mkt.converter.biz.CourseDetailConverter;
import cn.edu.cuhk.mkt.entity.biz.CourseDetail;
import cn.edu.cuhk.mkt.entity.biz.CourseDetailVO;
import cn.edu.cuhk.mkt.entity.biz.CourseMaster;
import cn.edu.cuhk.mkt.entity.biz.CourseTemplate;
import cn.edu.cuhk.mkt.mapper.biz.CourseDetailMapper;
import cn.edu.cuhk.mkt.service.biz.CourseDetailService;
import cn.edu.cuhk.mkt.service.biz.CourseMasterService;
import cn.edu.cuhk.mkt.service.biz.CourseTemplateService;

import com.google.common.collect.Maps;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.base.SuperServiceImpl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务模块-学生课程数据明细表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class CourseDetailServiceImpl extends SuperServiceImpl<CourseDetailMapper, CourseDetail> implements CourseDetailService {
    @Autowired
    private CourseMasterService courseMasterService;

    @Autowired
    private CourseTemplateService courseTemplateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(String courseMasterId, List<CourseDetailVO> courseDetailVOList) {
        AssertUtil.isEmpty(courseDetailVOList, "课程明细数据不能为空");
        Set<String> categoryCodeSet = new HashSet<>();
        for (CourseDetailVO courseDetailVO : courseDetailVOList) {
            String courseCategoryCode = courseDetailVO.getCourseCategoryCode();
            AssertUtil.isBlank(courseCategoryCode, "课程分类编码不能为空");
            categoryCodeSet.add(courseCategoryCode);
        }
        AssertUtil.isGtNumber(categoryCodeSet.size(), 1, "课程分类编码不允许重复");
        CourseMaster courseMaster = courseMasterService.getById(courseMasterId);
        AssertUtil.isNull(courseMaster, "课程主数据id不存在");
        List<String> courseTemplateIds = new ArrayList<>();
        for (CourseDetailVO courseDetailVO : courseDetailVOList) {
            String courseTemplateId = courseDetailVO.getCourseTemplateId();
            if(StringUtils.isNotBlank(courseTemplateId)){
                courseTemplateIds.add(courseTemplateId);
            }
        }

        if(courseTemplateIds.size() > 0){
            int count = courseTemplateService.lambdaQuery().in(CourseTemplate::getId, courseTemplateIds).count();
            if(courseTemplateIds.size() != count){
                AssertUtil.isNull(null, "课程模板id不存在");
            }
        }
        //1. 查询已填课程明细
        List<CourseDetail> resultList = this.lambdaQuery()
            .eq(CourseDetail::getCourseMasterId, courseMaster.getId())
            .notIn(CourseDetail::getSelfCheck, 0)
            .notIn(CourseDetail::getCourseCategoryCode, BizConst.FREE_ELECTIVE)
            .list();
        if(ObjectUtils.isNotEmpty(resultList)) {
            Map<String, String> courseCodeMap = Maps.newHashMap();
            resultList.forEach(item-> {
                courseCodeMap.put(item.getCourseCode(), item.getCourseCategoryCode());
            });
            // Map<String, String> courseCodeMap = resultList.stream().collect(Collectors.toMap(CourseDetail::getCourseCode, CourseDetail::getCourseCategoryCode));
            log.info("-----> courseCodeMap: {}", JsonUtil.toJson(courseCodeMap));
            // 检查自由选择课程是否互斥
            courseDetailVOList.forEach(item-> {
                String courseCategoryCode = item.getCourseCategoryCode();
                String courseCode = item.getCourseCode();
                if(courseCategoryCode.equals(BizConst.FREE_ELECTIVE) && courseCodeMap.containsKey((courseCode))) {
                    // throw new BusinessException("课程【" + courseCode + "】已在【" + courseCodeMap.get(courseCode) + "】选修，请勿重复填写");
                    throw new BusinessException("课程重复填写: 有课程与之前大学核心课程/学院必修课程/专业必修课程/专业选修课程部分已填写课程重复，请检查。\r\n Course Repeat: The course has been filled in part of the University Core/School Package/Major Required Courses/Major Elective Courses before. Please check.");
                }
            });
        }
        String courseCategoryCode = categoryCodeSet.iterator().next();
        // 清空课程明细数据（物理删除）
        CourseDetail delCondition = new CourseDetail();
        delCondition.setCourseMasterId(courseMaster.getId());
        delCondition.setCourseCategoryCode(courseCategoryCode);
        super.mapper.deleteByCondition(delCondition);

        List<CourseDetail> batchInsertList = new ArrayList<>();
        Integer sortIndex = 1;
        for (CourseDetailVO courseDetailVO : courseDetailVOList) {
            CourseDetail courseDetail = new CourseDetailConverter().toEntity(courseDetailVO);
            courseDetail.setId(null);
            courseDetail.setCourseMasterId(courseMaster.getId());
            courseDetail.setSortIndex(sortIndex);
            courseDetail.setCreatedDate(LocalDateTime.now());
            courseDetail.setUpdatedDate(LocalDateTime.now());
            batchInsertList.add(courseDetail);
            sortIndex = sortIndex + 1;
        }
        // 批量新增（入库数据顺序会乱）
        int insertResult = super.insertBatch(batchInsertList);
        courseDetailVOList = new CourseDetailConverter().toVO(batchInsertList);

        return insertResult;
    }

}
