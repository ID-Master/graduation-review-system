package cn.edu.cuhk.mkt.mapper.sys;

import cn.edu.cuhk.mkt.entity.sys.AdTeacherTemp;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统模块-ad老师临时表 Mapper接口
 *
 * @author taok
 * @date 2021-08-23
 */
public interface AdTeacherTempMapper extends SuperMapper<AdTeacherTemp> {

    /**
     * 新增AD老师数据
     * @param batchNumber
     * @return
     */
    int insertAdTeacher(@Param(value = "batchNumber") String batchNumber);

    /**
     * 修改AD老师数据
     * @param batchNumber
     * @return
     */
    int updateAdTeacher(@Param(value = "batchNumber") String batchNumber);


}