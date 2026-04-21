package cn.edu.cuhk.mkt.mapper.sys;

import cn.edu.cuhk.mkt.entity.sys.AdStudentTemp;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统模块-ad学生临时表 Mapper接口
 *
 * @author taok
 * @date 2021-08-23
 */
public interface AdStudentTempMapper extends SuperMapper<AdStudentTemp> {

    /**
     * 新增AD学生数据
     * @param batchNumber
     * @return
     */
    int insertAdStudent(@Param(value = "batchNumber") String batchNumber);

    /**
     * 修改AD学生数据
     * @param batchNumber
     * @return
     */
    int updateAdStudent(@Param(value = "batchNumber") String batchNumber);

    /**
     * 删除AD学生数据
     * @param batchNumber
     * @return
     */
    int deleteAdStudent(@Param(value = "batchNumber") String batchNumber);

}