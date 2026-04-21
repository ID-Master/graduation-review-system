package cn.edu.cuhk.mkt.common.util;

import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 断言工具类
 *
 * @author taokai
 */
public class AssertUtil {

    /**
     * 对象为null，抛出业务异常
     * @param obj 对象
     * @param message 消息
     */
    public static void isNull(Object obj, String message){
        if(ObjectUtil.isNull(obj)){
            throw new BusinessException(message);
        }
    }

    /**
     * 对象不为null，抛出业务异常
     * @param obj 对象
     * @param message 消息
     */
    public static void isNotEmpty(Object obj, String message){
        if(ObjectUtil.isNotEmpty(obj)){
            throw new BusinessException(message);
        }
    }

    /**
     * 字符串相等，抛出业务异常
     * @param source
     * @param taget
     * @param message
     */
    public static void equals(String source, String taget, String message){
        if(StringUtil.equals(source, taget)){
            throw new BusinessException(message);
        }
    }

    /**
     * 字符串不相等，抛出业务异常
     * @param source
     * @param taget
     * @param message
     */
    public static void notEquals(String source, String taget, String message){
        if(!StringUtil.equals(source, taget)){
            throw new BusinessException(message);
        }
    }

    /**
     * 值为true，抛出业务异常
     * @param flag 布尔值
     * @param message 消息
     */
    public static void isTrue(Boolean flag, String message){
        if(flag.equals(Boolean.TRUE)){
            throw new BusinessException(message);
        }
    }

    /**
     * 值为false，抛出业务异常
     * @param flag 布尔值
     * @param message 消息
     */
    public static void isFalse(Boolean flag, String message){
        if(flag.equals(Boolean.FALSE)){
            throw new BusinessException(message);
        }
    }

    /**
     * 集合为空，抛出业务异常
     * @param obj 集合对象
     * @param message 消息
     */
    public static void isEmpty(Collection obj, String message){
        AssertUtil.isTrue(CollectionUtils.isEmpty(obj), message);
    }

    /**
     * 值为空，抛出业务异常
     * @param value 值
     * @param message 消息
     */
    public static void isBlank(String value, String message){
        if(StringUtils.isBlank(value)){
            throw new BusinessException(message);
        }
    }

    /**
     * 大于0，抛出业务异常
     * @param srcNumber 数值
     * @param message 消息
     */
    public static void isGtZero(Integer srcNumber, String message){
        isGtNumber(srcNumber, 0, message);
    }

    /**
     * 大于某个数字，抛出业务异常
     * @param srcNumber
     * @param targetNumber
     * @param message
     */
    public static void isGtNumber(Integer srcNumber, Integer targetNumber, String message){
        if(srcNumber > targetNumber){
            throw new BusinessException(message);
        }
    }

}
