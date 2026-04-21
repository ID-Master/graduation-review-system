package cn.edu.cuhk.mkt.service.impl.biz;

import cn.edu.cuhk.mkt.entity.biz.EmailRecord;
import cn.edu.cuhk.mkt.mapper.biz.EmailRecordMapper;
import cn.edu.cuhk.mkt.service.biz.EmailRecordService;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 业务模块-邮件记录表 服务实现
 *
 * @author taok
 * @date 2021-08-26
 */
@Service
public class EmailRecordServiceImpl extends SuperServiceImpl<EmailRecordMapper, EmailRecord> implements EmailRecordService {

}
