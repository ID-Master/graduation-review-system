package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.entity.sys.AdStudentTemp;
import cn.edu.cuhk.mkt.mapper.sys.AdStudentTempMapper;
import cn.edu.cuhk.mkt.service.sys.AdStudentTempService;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统模块-ad学生临时表 服务实现
 *
 * @author taok
 * @date 2021-08-23
 */
@Service
public class AdStudentTempServiceImpl extends SuperServiceImpl<AdStudentTempMapper, AdStudentTemp> implements AdStudentTempService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAdStudent(String batchNumber) {
        return mapper.insertAdStudent(batchNumber);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdStudent(String batchNumber) {
        return mapper.updateAdStudent(batchNumber);
    }

}
