package cn.edu.cuhk.mkt.service.sys;

import cn.edu.cuhk.mkt.entity.sys.Info;
import cn.edu.cuhk.mkt.entity.sys.InfoVO;
import com.uneed.common.mybatis.base.SuperService;

/**
 * 系统模块-配置表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface InfoService extends SuperService<Info> {
    public Integer updateInfo(InfoVO vo);
    public Info getOne();
}