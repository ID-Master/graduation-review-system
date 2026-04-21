package cn.edu.cuhk.mkt.mapper.biz;

import cn.edu.cuhk.mkt.entity.biz.CourseDetail;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 业务模块-学生课程数据明细表 Mapper接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface CourseDetailMapper extends SuperMapper<CourseDetail> {

    /**
     * 根据条件删除课程明细数据
     * @param courseDetail
     * @return
     */
    int deleteByCondition(@Param(value = "condition") CourseDetail courseDetail);

}