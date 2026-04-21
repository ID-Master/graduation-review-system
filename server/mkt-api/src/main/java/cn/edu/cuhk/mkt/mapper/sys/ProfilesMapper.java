package cn.edu.cuhk.mkt.mapper.sys;

import cn.edu.cuhk.mkt.entity.sys.Profiles;
import com.uneed.common.mybatis.base.SuperMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统模块-全局配置表 Mapper接口
 *
 * @author taok
 * @date 2021-08-17
 */
public interface ProfilesMapper extends SuperMapper<Profiles> {

    int deleteByCondition(@Param(value = "condition") Profiles profiles);

}