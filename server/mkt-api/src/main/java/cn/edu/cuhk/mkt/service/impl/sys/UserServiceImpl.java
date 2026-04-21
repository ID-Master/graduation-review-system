package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.common.consts.StringConst;
import cn.edu.cuhk.mkt.common.consts.SystemConst;
import cn.edu.cuhk.mkt.common.enums.StatusEnum;
import cn.edu.cuhk.mkt.common.enums.UserEnum;
import cn.edu.cuhk.mkt.common.util.JasyptUtil;
import cn.edu.cuhk.mkt.entity.cache.UserCacheDTO;
import cn.edu.cuhk.mkt.entity.sys.User;
import cn.edu.cuhk.mkt.mapper.sys.UserMapper;
import cn.edu.cuhk.mkt.service.sys.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.common.collect.Lists;
import com.uneed.common.core.reflect.ReflectUtil;
import com.uneed.common.mybatis.base.SuperServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 系统模块-用户表 服务实现
 *
 * @author taok
 * @date 2021-08-13
 */
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User initAdminUser() {
        // 默认管理员账号
        String adminLoginName = SystemConst.SYS_ADMIN_NAME;
        User adminUser = this.lambdaQuery().eq(User::getLoginName, adminLoginName).one();
        if(adminUser == null){
            adminUser = new User();
            adminUser.setLoginName(adminLoginName);
            adminUser.setNameCh("管理员");
            // 加密
            String encryptPwd = JasyptUtil.encryptPbeWithMD5AndDES(JasyptUtil.SECRET_KEY, SystemConst.SYS_ADMIN_PASSWORD);
            adminUser.setPassword(encryptPwd);
            adminUser.setUserType(UserEnum.USER_TYPE.SYSTEM.getCode());
            adminUser.setStatus(StatusEnum.EFFECTIVE.getCode());
            this.insert(adminUser);
        }
        return adminUser;
    }

    @Override
    public void refreshUserCache(boolean force) {
        long currentThreadId = Thread.currentThread().getId();
        int total = 0;
        long duration = 0;
        log.info("******************** 开始刷新用户缓存 start，线程id: {} ********************", currentThreadId);
        Long startTime = System.currentTimeMillis();
        // 总用户条数
        total = this.lambdaQuery().count();
        // 所有用户id集合
        QueryWrapper<User> columnWrapper = new QueryWrapper<>();
        List<String> columnNames = new ArrayList<>();
        columnNames.add("id");
        columnNames.removeAll(Collections.singleton(null));
        columnWrapper.select(columnNames.toArray(new String[columnNames.size()]));
        List<User> userList = super.list(columnWrapper);
        List<String> userIds = new ArrayList<>();
        Optional.ofNullable(userList).orElse(new ArrayList<>()).forEach(user -> {
            userIds.add(user.getId());
        });

        // 按组分割缓存用户
        int partition = 999;
        List<List<String>> userIdParts = Lists.partition(userIds, partition);
        userIdParts.stream().forEach(partUserIds -> {
            try {
                getCacheUserByIds(partUserIds, force);
            }catch (Exception e){
                log.error("============>缓存用户信息异常: {}", e.getMessage(), e);
            }
        });

        Long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        log.info("******************** 开始刷新用户缓存 end，线程id: {}，耗时（毫秒）: {}，总条数: {} ********************",
                currentThreadId,
                duration,
                total
        );
    }

    @Override
    public UserCacheDTO getCacheUserById(String id) {
        Map<String, UserCacheDTO> dataMap = getCacheUserByIds(Arrays.asList(id), false);
        for (String key : dataMap.keySet()){
            return dataMap.get(key);
        }
        return null;
    }

    @Override
    public Map<String, UserCacheDTO> getCacheUserByIds(List<String> ids, boolean force) {
        // 从缓存中获取的用户信息
        Map<String, UserCacheDTO> userMap = new HashMap();
        List<String> noCacheIds = new ArrayList<>();
        // 强制刷新用户缓存
        if(force){
            noCacheIds.addAll(ids);
        }
        // 非强制刷新用户缓存
        else {
            Optional.ofNullable(ids).orElse(new ArrayList<>()).forEach(userId -> {
                String redisKey = SystemConst.CHACHE_USER_PREFIX_KEY + userId;
                UserCacheDTO userCacheDTO = null;
                try {
                    String userCacheJson = redisTemplate.opsForValue().get(redisKey);
                    if(StringUtils.isNotBlank(userCacheJson)){
                        userCacheDTO = JSONObject.parseObject(userCacheJson, UserCacheDTO.class);
                    }
                } catch (Exception e) {
                    log.error("==========>从redis缓存读取用户信息异常: {}", e.getMessage(), e);
                }
                if (userCacheDTO != null) {
                    log.info("-------->从redis缓存获取用户信息: {}", JSON.toJSONString(userCacheDTO));
                    userMap.put(userId, userCacheDTO);
                }
                else if(!StringConst.ZERO.equals(userId)){
                    noCacheIds.add(userId);
                }
            });
        }

        // 未被缓存的用户
        if (noCacheIds.size() > 0) {
            List<User> userList = super.listByIds(noCacheIds);
            Optional.ofNullable(userList).orElse(new ArrayList<>()).forEach(user -> {
                String userId = user.getId();
                UserCacheDTO userCacheDTO = new UserCacheDTO();
                userCacheDTO.setId(userId);
                userCacheDTO.setNameEn(user.getNameEn());
                userCacheDTO.setNameCh(user.getNameCh());
                userMap.put(userId, userCacheDTO);
                try {
                    String redisKey = SystemConst.CHACHE_USER_PREFIX_KEY + userId;
                    String userCacheJson = JSON.toJSONString(userCacheDTO);
                    redisTemplate.opsForValue().set(redisKey, userCacheJson, SystemConst.CHACHE_USER_MAX_SECONDS, TimeUnit.SECONDS);
                    log.info("==========>用户信息写入redis缓存: {}", userCacheJson);
                } catch (Exception e) {
                    log.error("==========>用户信息写入redis缓存异常: {}", e.getMessage(), e);
                }
            });
        }
        return userMap;
    }

    @Override
    public void fillUserData(List dataList){
        if(CollectionUtils.isEmpty(dataList)){ return; }
        Optional.ofNullable(dataList).orElse(new ArrayList<>()).forEach(data -> {
            // 创建人
            try {
                Field fieldCreatedBy = ReflectUtil.getField(data.getClass(), "createdBy");
                Object fieldValue = ReflectUtil.getFieldValue(data, fieldCreatedBy);
                String createdById = fieldValue.toString();
                UserCacheDTO userCacheDTO = getCacheUserById(createdById);
                if(userCacheDTO != null){
                    ReflectUtil.setFieldValue(data, "createNameCh", userCacheDTO.getNameCh());
                    ReflectUtil.setFieldValue(data, "createNameEn", userCacheDTO.getNameEn());
                }
            }catch (Exception e){
                log.warn("-------->填充创建人名称异常: {}", e.getMessage());
            }

            // 修改人
            try {
                Field fieldUpdatedBy = ReflectUtil.getField(data.getClass(), "updatedBy");
                Object fieldValue = ReflectUtil.getFieldValue(data, fieldUpdatedBy);
                String updateById = fieldValue.toString();
                UserCacheDTO userCacheDTO = getCacheUserById(updateById);
                if(userCacheDTO != null){
                    ReflectUtil.setFieldValue(data, "updateNameCh", userCacheDTO.getNameCh());
                    ReflectUtil.setFieldValue(data, "updateNameEn", userCacheDTO.getNameEn());
                }
            }catch (Exception e){
                log.warn("-------->填充修改人名称异常: {}", e.getMessage());
            }
        });
    }

    @Override
    public User findByLoginName(String loginName) {
        return super.lambdaQuery().eq(User::getLoginName, loginName).one();
    }

}
