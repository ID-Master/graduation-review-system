package cn.edu.cuhk.mkt.service.sys;

import cn.edu.cuhk.mkt.entity.sys.AdStudentTemp;
import com.uneed.common.mybatis.base.SuperService;

/**
 * 系统模块-ad学生临时表  服务接口
 *
 * @author taok
 * @date 2021-08-23
 */
public interface AdStudentTempService extends SuperService<AdStudentTemp> {

    /**
     * 新增AD学生数据
     * @param batchNumber
     * @return
     */
    int insertAdStudent(String batchNumber);

    /**
     * 修改AD学生数据
     * @param batchNumber
     * @return
     */
    int updateAdStudent(String batchNumber);

}