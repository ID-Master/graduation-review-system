package cn.edu.cuhk.mkt.service.sys;

import cn.edu.cuhk.mkt.entity.cache.UserCacheDTO;
import cn.edu.cuhk.mkt.entity.sys.User;
import com.uneed.common.mybatis.base.SuperService;

import java.util.List;
import java.util.Map;

/**
 * 系统模块-用户表  服务接口
 *
 * @author taok
 * @date 2021-08-13
 */
public interface UserService extends SuperService<User> {

    /**
     * 初始化系统管理员用户
     * @return
     */
    User initAdminUser();

    /**
     * 刷新用户缓存
     * @param force （强制刷新：true, 非强制刷新： false）
     */
    void refreshUserCache(boolean force);

    /**
     * 通过id获取用户缓存信息
     * @param id
     * @return
     */
    UserCacheDTO getCacheUserById(String id);

    /**
     * 通过id集合获取用户缓存信息
     * @param ids
     * @param force 是否强制刷新缓存，是：true，否：false
     * @return
     */
    Map<String, UserCacheDTO> getCacheUserByIds(List<String> ids, boolean force);

    /**
     * 填充数据创建人，修改人信息
     * @param dataList
     */
    void fillUserData(List dataList);

    User findByLoginName(String loginName);

}