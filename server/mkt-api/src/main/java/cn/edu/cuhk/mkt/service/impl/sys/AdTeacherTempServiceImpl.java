package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.entity.sys.AdTeacherTemp;
import cn.edu.cuhk.mkt.mapper.sys.AdTeacherTempMapper;
import cn.edu.cuhk.mkt.service.sys.AdTeacherTempService;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统模块-ad老师临时表 服务实现
 *
 * @author taok
 * @date 2021-08-23
 */
@Service
public class AdTeacherTempServiceImpl extends SuperServiceImpl<AdTeacherTempMapper, AdTeacherTemp> implements AdTeacherTempService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAdTeacher(String batchNumber) {
        return mapper.insertAdTeacher(batchNumber);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdTeacher(String batchNumber) {
        return mapper.updateAdTeacher(batchNumber);
    }

}
