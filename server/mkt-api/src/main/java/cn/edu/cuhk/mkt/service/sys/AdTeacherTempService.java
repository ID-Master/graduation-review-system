package cn.edu.cuhk.mkt.service.sys;

import cn.edu.cuhk.mkt.entity.sys.AdTeacherTemp;
import com.uneed.common.mybatis.base.SuperService;

/**
 * 系统模块-ad老师临时表  服务接口
 *
 * @author taok
 * @date 2021-08-23
 */
public interface AdTeacherTempService extends SuperService<AdTeacherTemp> {

    /**
     * 新增AD老师数据
     * @param batchNumber
     * @return
     */
    int insertAdTeacher(String batchNumber);

    /**
     * 修改AD老师数据
     * @param batchNumber
     * @return
     */
    int updateAdTeacher(String batchNumber);

}