package com.uneed.common.dict.api;

import com.uneed.common.dict.entity.Popup;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.Collection;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;

/**
 * 弹窗配置缓存操作接口，包含了在缓存中对弹窗配置进行增、删、改、查等操作
 *
 * @author diablo
 * @date 2020/2/27
 */
public interface PopupRepertory {

    /**
     * 根据弹窗配置编号从缓存获取弹窗配置
     *
     * @param code 配置编号
     * @return Popup 弹窗配置
     */
    Popup get(Integer code);

    /**
     * 根据弹窗配置编号从缓存获取弹窗配置的内容
     *
     * @param code 配置编号
     * @return String 弹窗配置内容
     */
    default String getContent(Integer code) {
        Popup popup = get(code);
        return isNotNull(popup) ? popup.getContent() : null;
    }

    /**
     * 将弹窗配置添加至缓存中
     *
     * @param popup 弹窗配置
     */
    void add(Popup popup);

    /**
     * 将弹窗配置集合添加至缓存中
     *
     * @param popups 弹窗配置
     */
    void add(Collection<Popup> popups);

    /**
     * 根据弹窗配置编号，从缓存中删除弹窗配置信息
     *
     * @param code 弹窗配置编号
     */
    void del(Integer code);

    /**
     * 根据弹窗配置编号集合，从缓存中删除配置信息
     *
     * @param codes 弹窗配置编号集合
     */
    void del(Collection<Integer> codes);

    /**
     * 清空弹窗配置信息
     */
    void clear();

    /**
     * 初始化弹窗配置信息
     *
     * @param popups 弹窗配置集合
     */
    void initialization(Collection<Popup> popups);

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为每一个字典集合构建一个hash对象
     *
     * @return BoundHashOperations<String, String, Popup>
     * @since 1.1.0
     */
    BoundHashOperations<String, String, Popup> getHash();
}
