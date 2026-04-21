package cn.edu.cuhk.mkt.service.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseDetail;
import cn.edu.cuhk.mkt.entity.biz.CourseDetailVO;
import com.uneed.common.mybatis.base.SuperService;

import java.util.List;

/**
 * 业务模块-学生课程数据明细表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseDetailService extends SuperService<CourseDetail> {

    /**
     * 保存课程明细数据
     *
     * @param courseDetailVOList
     * @return
     */
    int save(String courseMasterId, List<CourseDetailVO> courseDetailVOList);

}