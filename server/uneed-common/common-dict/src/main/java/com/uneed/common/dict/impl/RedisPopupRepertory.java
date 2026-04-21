package com.uneed.common.dict.impl;

import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.PopupRepertory;
import com.uneed.common.dict.constant.DictConstant;
import com.uneed.common.dict.entity.Popup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/2/27
 */
@Slf4j
public class RedisPopupRepertory implements PopupRepertory {

    /**
     * redisTemplate对象，需要注入
     */
    private final RedisTemplate<String, Popup> redisTemplate;

    /**
     * 带redisTemplate参数的构造函数
     *
     * @param redisTemplate redisTemplate对象
     */
    public RedisPopupRepertory(RedisTemplate<String, Popup> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //////////////////////////////////////// 接口方法实现 /////////////////////////////////////////////////////////////////////

    /**
     * 根据弹窗配置编号从缓存获取弹窗配置
     */
    @Override
    public Popup get(Integer code) {
        return isNotNull(code) ? getHash().get(getKey(code)) : null;
    }

    /**
     * 将弹窗配置添加至缓存中
     */
    @Override
    public void add(Popup popup) {
        if (isNotNull(popup) && isNotEmpty(popup.getCode())) {
            getHash().put(popup.getCode(), popup);
        }
    }

    /**
     * 将弹窗配置集合添加至缓存中
     */
    @Override
    public void add(Collection<Popup> popups) {
        TimeMeter meter = new TimeMeter();
        Map<String, Popup> popupMap = buildPopupMap(popups);
        if (log.isDebugEnabled()) {
            log.debug("=====>> <<<构建>>>所有【弹窗配置】数据完成！用时：{}ms", meter.sign("build"));
        }
        if (!popupMap.isEmpty()) {
            getHash().putAll(popupMap);
        }
        if (log.isDebugEnabled()) {
            log.debug("----------->> <<<新增>>>所有【弹窗配置】数据完成！配置数量：{}，用时：{}ms", popupMap.size(), meter.sign());
        }
    }

    /**
     * 根据弹窗配置编号，从缓存中删除弹窗配置信息
     */
    @Override
    public void del(Integer code) {
        if (isNotNull(code)) {
            getHash().delete(getKey(code));
        }
    }

    /**
     * 根据弹窗配置编号集合，从缓存中删除配置信息
     */
    @Override
    public void del(Collection<Integer> codes) {
        if (isNotEmpty(codes)) {
            getHash().delete(toKeyArray(codes));
        }
    }

    /**
     * 清空弹窗配置信息
     */
    @Override
    public void clear() {
        TimeMeter meter = new TimeMeter();
        Boolean result = redisTemplate.delete(DictConstant.POPUP_REDIS_KEY);
        if (log.isDebugEnabled()) {
            Long count = getHash().size();
            if (notEqual(result, Boolean.TRUE)) {
                count = 0L;
            }
            log.debug("----------->> <<<清空>>>所有【弹窗配置】数据完成！弹窗配置数量：{}，用时：{}ms", nullToDefault(count, 0L), meter.sign());
        }
    }

    /**
     * 初始化弹窗配置信息
     */
    @Override
    public void initialization(Collection<Popup> popups) {
        TimeMeter meter = new TimeMeter();
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【弹窗配置】数据【开始】，缓存中弹窗配置总数：{} ============", getHash().size());
        }
        //先清空弹窗配置缓存数据
        clear();
        //批量添加弹窗配置信息
        add(popups);
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【弹窗配置】数据<<<完成>>>，缓存中弹窗配置总数：{}，总耗时{}ms ============", getHash().size(), meter.sign());
        }
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为每一个字典集合构建一个hash对象
     */
    @Override
    public BoundHashOperations<String, String, Popup> getHash() {
        Validate.notNull(this.redisTemplate, "redisTemplate can't be null!");
        return this.redisTemplate.boundHashOps(DictConstant.POPUP_REDIS_KEY);
    }

    //////////////////////////////////////// 私有方法 /////////////////////////////////////////////////////////////////////

    /**
     * 将整型编号转换为字符串的key值
     */
    private String getKey(Integer code) {
        return StringUtil.toString(code);
    }

    /**
     * 根据弹窗对象集合构建弹窗的map集合，key为弹窗数据编号，value为弹窗对象数据
     */
    private Map<String, Popup> buildPopupMap(Collection<Popup> popups) {
        Map<String, Popup> map = Maps.newHashMap();
        if (isNotEmpty(popups)) {
            popups.stream().filter(p -> isNotEmpty(p.getCode())).forEach(p -> map.put(p.getCode(), p));
        }
        return map;
    }

    /**
     * 将编号集合转换为数组
     */
    private Object[] toKeyArray(Collection<Integer> codes) {
        return codes.stream().map(this::getKey).toArray();
    }
}
