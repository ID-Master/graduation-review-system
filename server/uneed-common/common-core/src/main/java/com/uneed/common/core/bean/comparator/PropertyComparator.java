package com.uneed.common.core.bean.comparator;

import com.uneed.common.core.bean.PropertyUtil;
import com.uneed.common.core.convert.Convert;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.Validate;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 属性比较器
 *
 * @author diablo
 * @date 201717/12/27
 */
public class PropertyComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = 5067468416943506563L;

    private final String property;

    private Comparator comparator;

    /**
     * 反射提取出来的值,需要类型转成到的类型.
     */
    private Class<? extends Comparable> convertClass;

    public PropertyComparator(String property) {
        Validate.notEmpty(property, "property can't be blank!");
        this.property = property;
    }

    public PropertyComparator(String property, Comparator comparator) {
        Validate.notEmpty(property, "property can't be blank!");
        this.property = property;
        this.comparator = comparator;
    }

    public PropertyComparator(String property, Class<? extends Comparable> convertClass) {
        Validate.notEmpty(property, "property can't be blank!");
        this.property = property;
        this.convertClass = convertClass;
    }

    public PropertyComparator(String property, Class<? extends Comparable> convertClass, Comparator comparator) {
        Validate.notEmpty(property, "property can't be blank!");
        this.property = property;
        this.convertClass = convertClass;
        this.comparator = comparator;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int compare(T t1, T t2) {
        if (t1 == t2) {
            return 0;
        } else if (t1 == null) {
            return 1;
        } else if (t2 == null) {
            return -1;
        }

        Comparable value1 = PropertyUtil.getProperty(t1, property);
        Comparable value2 = PropertyUtil.getProperty(t2, property);

        //如果值需要类型转换
        if (convertClass != null) {
            value1 = Convert.convert(convertClass,value1);
            value2 = Convert.convert(convertClass,value2);
        }
        return null != comparator ? comparator.compare(value1, value2) : compare(t1, t2, value1, value2);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compare(T t1, T t2, Comparable value1, Comparable value2) {
        int compareTo = ObjectUtil.compare(value1, value2);

        if (compareTo == 0) {
            //避免TreeSet / TreeMap 过滤掉同sort字段但是对象不相同的情况
            int hashCode1 = t1.hashCode();
            int hashCode2 = t2.hashCode();
            compareTo = ObjectUtil.compare(hashCode1, hashCode2);
            return compareTo;
        }
        return compareTo;
    }
}
