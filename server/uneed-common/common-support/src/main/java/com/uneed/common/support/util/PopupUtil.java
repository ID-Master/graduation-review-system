package com.uneed.common.support.util;

import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.PopupRepertory;
import com.uneed.common.dict.entity.Popup;
import org.springframework.data.redis.core.BoundHashOperations;

import static com.uneed.common.core.lang.ObjectUtil.isNotEmpty;
import static com.uneed.common.core.lang.ObjectUtil.isNotNull;

/**
 * 弹窗配置工具类
 *
 * @author diablo
 * @date 2019/12/18
 */
public class PopupUtil {

    /**
     * 私有化构造函数
     */
    private PopupUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 通过弹窗配置编号获取弹窗配置内容
     *
     * @param code 配置编号
     * @return String 弹窗配置内容
     * @since 1.1.0
     */
    public static String get(Integer code) {
        return get(code, "");
    }

    /**
     * 通过弹窗配置编号、参数值获取填充了参数的弹窗配置内容
     *
     * @param code   配置编号
     * @param params 参数集
     * @return String 填充了参数的弹窗配置内容
     */
    public static String get(Integer code, Object... params) {
        Popup popup = getPopup(code);
        if (isNotNull(popup) && isNotEmpty(popup.getContent())) {
            return StringUtil.format(popup.getContent(), params);
        }
        return "编号【" + code + "】没有配置弹窗内容。";
    }

    /**
     * 通过弹窗配置编号获取弹窗配置对象数据
     *
     * @param code 配置编号
     * @return Popup 弹窗配置对象数据
     */
    public static Popup getPopup(Integer code) {
        return getRepertory().get(code);
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为每一个字典集合构建一个hash对象
     *
     * @return BoundHashOperations<String, String, Popup>
     * @since 1.1.0
     */
    public BoundHashOperations<String, String, Popup> getHash() {
        return getRepertory().getHash();
    }

    /**
     * 用来缓存弹窗配置的工具对象
     */
    private static PopupRepertory repertory;

    /**
     * spring容器中获取弹窗配置工具对象
     */
    private static PopupRepertory getRepertory() {
        if (isNotNull(repertory)) {
            return repertory;
        }
        repertory = SpringUtil.getBean(PopupRepertory.class);
        Validate.notNull(repertory, "Cannot get PopupRepertory object from spring container!");
        return repertory;
    }
}
