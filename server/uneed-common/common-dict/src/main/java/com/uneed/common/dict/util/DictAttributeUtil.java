package com.uneed.common.dict.util;

import com.uneed.common.annotation.dict.DictField;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.dict.attribute.DictAttribute;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uneed.common.core.bean.BeanUtil.getReadAndWriteMethod;
import static com.uneed.common.core.lang.ObjectUtil.isNotEmpty;
import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.dict.constant.DictConstant.END_FILE_NAME;

/**
 * 数据字典工具类
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/17
 */
@Slf4j
public class DictAttributeUtil {

    /**
     * 私有化构造函数
     */
    private DictAttributeUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 根据数据对象类型，获取字典数据集合
     *
     * @param beanClass 数据对象类型
     * @return List<DictAttribute>
     */
    public static List<DictAttribute> buildAttributes(Class<?> beanClass) {
        List<DictAttribute> list = Lists.newArrayList();
        //循环条件条件类型
        Class<?> clazz = beanClass;
        //根据java类，获取getter和setter方法集合
        Map<String, Method> methodMap = getMethod(beanClass);
        while (clazz != Object.class) {
            List<Field> fields = BeanUtil.getFields(clazz, true, "serialVersionUID");
            fields.stream().map(f -> buildAttribute(f, methodMap)).filter(ObjectUtil::isNotNull).forEach(list::add);
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    private static DictAttribute buildAttribute(Field field, Map<String, Method> methodMap) {
        DictField annotation = field.getAnnotation(DictField.class);
        if (isNotNull(annotation)) {
            DictAttribute attribute = new DictAttribute();
            attribute.setFileName(field.getName());
            attribute.setKeyField(getKeyField(annotation.value(), field.getName(), methodMap));
            attribute.setKey(annotation.key());
            attribute.setProperty(annotation.property());
            attribute.setGetter(methodMap.get(StringUtil.getterName(attribute.getKeyField())));
            attribute.setSetter(methodMap.get(StringUtil.setterName(field.getName())));
            if (isNotNull(attribute.getGetter())) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * 优先取annotation上的value值作为字典key值来源，若为空，则取当前字段做为字典key值来源
     */
    private static String getKeyField(String annotationField, String fieldName, Map<String, Method> methodMap) {
        //优先取annotation上的field值
        if (isNotEmpty(annotationField)) {
            return annotationField;
        }
        //判断字典是否已"Name"结尾
        if (fieldName.endsWith(END_FILE_NAME)) {
            //截取"name"之前的数据
            String field = substringLast(fieldName);
            //判断类中是否存在对应该字典的getter方法，若存在，直接返回
            Method method = methodMap.get(StringUtil.getterName(field));
            if (isNotNull(method)) {
                return field;
            }
        }
        return fieldName;
    }

    private static String substringLast(String str) {
        return str.substring(0, str.lastIndexOf(END_FILE_NAME));
    }

    private static Map<String, Method> getMethod(Class<?> cs) {
        return getReadAndWriteMethod(cs).stream().filter(ObjectUtil::isNotNull).collect(Collectors.toMap(Method::getName, method -> method));
    }

}
