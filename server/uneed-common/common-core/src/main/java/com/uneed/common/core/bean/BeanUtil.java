package com.uneed.common.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uneed.common.core.Editor;
import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.core.reflect.ClassUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * @author diablo
 * @since 1.0.0
 */
@Slf4j
public final class BeanUtil {

    /**
     * 私有化构造函数
     */
    private BeanUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    private static final String CONNECT_OPERATOR = "#";

    private static final Map<String, BeanCopier> CACHE_COPIERS = new ConcurrentHashMap<>();

    private static final Map<String, Method> CACHE_METHOD = new ConcurrentHashMap<>();

    public enum MethodType {
        /**
         * 方法类型
         */
        READ,
        WRITE
    }

    //////////////////////////////// 工具方法 /////////////////////////////////////////////////////////////////////////////////


    /**
     * 实体类数据拷贝
     * 拷贝原则：
     * 1.只拷贝名称和类型都相同的属性，即使jdk的原类型与包装类型，都会被看成不同的类型，如int与Integer
     * 2.拷贝的属性必须存在getter和setter方法
     *
     * @param orig 源对象
     * @param dest 目标对象
     */
    public static void copy(Object orig, Object dest) {
        copy(orig, dest, new String[]{});
    }

    /**
     * 对象拷贝，会忽略ignores指定的字段集
     *
     * @param orig    源对象
     * @param dest    模板对象
     * @param ignores 忽略字段集
     */
    public static void copy(Object orig, Object dest, String... ignores) {
        BeanUtils.copyProperties(orig, dest, ignores);
    }

    /**
     * 实体类数据拷贝，该方法可以实现自定义规则转换
     * 拷贝原则：
     * 1.只拷贝名称和类型都相同的属性，即使jdk的原类型与包装类型，都会被看成不同的类型，如int与Integer
     * 2.拷贝的属性必须存在getter和setter方法
     *
     * @param orig      源对象
     * @param dest      目标对象
     * @param converter 自定义转换
     * @deprecated 1.3.0 保留至1.3.0版本
     */
    @Deprecated
    public static void copy(Object orig, Object dest, Converter converter) {
        Validate.notNull(orig, "orig [origin bean] not specified!");
        Validate.notNull(dest, "dest [destination bean] not specified!");
        getCopier(orig, dest, converter).copy(orig, dest, converter);
    }

    /**
     * 对象拷贝，会根据目标对象类型，构造一个新的对象，拷贝值后再返还
     *
     * @param orig      源对象
     * @param destClass 目标对象类型
     * @param ignores   忽略字段集
     * @param <T>       目标类型的泛型参数
     * @return T 目标类型对象
     */
    public static <T> T copyNew(Object orig, Class<T> destClass, String... ignores) {
        T dest = BeanUtil.newInstance(destClass);
        copy(orig, dest, ignores);
        return dest;
    }

    /**
     * 批量对象拷贝，会循源对象集合，逐个构造新对象，拷贝后填充到新集合中返回
     *
     * @param origins   源对象集合
     * @param destClass 目标对象类型
     * @param ignores   忽略字段集
     * @param <T>       目标类型的泛型参数
     * @return List<T> 目标类型对象集合
     */
    public static <T> List<T> copyNew(List<?> origins, Class<T> destClass, String... ignores) {
        return nullToDefault(origins, new ArrayList<>()).stream().map(s -> copyNew(s, destClass, ignores))
                .collect(Collectors.toList());
    }

    /**
     * 序列化拷贝对象，先将源对象序列化成json对象，再将json转换为目标类型对象
     *
     * @param orig      源对象
     * @param destClass 目标对象类型
     * @param ignores   忽略字段集
     * @param <T>       目标类型的泛型参数
     * @return T 目标类型对象
     */
    public static <T> T serializedCopy(Object orig, Class<T> destClass, String... ignores) {
        JSONObject object = (JSONObject) JSONObject.toJSON(orig);
        Arrays.stream(ignores).forEach(object::remove);
        return object.toJavaObject(destClass);
    }

    /**
     * 批量序列化拷贝对象，先将源对象集合序列化成json对象，再将json转换为目标类型对象集合
     *
     * @param origins   源对象集合
     * @param destClass 目标对象类型
     * @param ignores   忽略字段集
     * @param <T>       目标类型的泛型参数
     * @return List<T> 目标类型对象集合
     */
    public static <T> List<T> serializedCopy(List<?> origins, Class<T> destClass, String... ignores) {
        if (origins.size() <= 1000) {
            return serializedCollectionCopy(origins, destClass, ignores);
        }
        List<? extends List<?>> groups = group(origins);
        List<T> list = Lists.newArrayList();
        for (List<?> group : groups) {
            list.addAll(serializedCopy(group, destClass, ignores));
        }
        return list;
    }

    private static <T> List<T> serializedCollectionCopy(List<?> origins, Class<T> destClass, String... ignores) {
        JSONArray array = (JSONArray) JSONArray.toJSON(origins);
        Arrays.stream(ignores).forEach(ignore -> IntStream.range(0, array.size()).mapToObj(array::getJSONObject).forEach(obj -> obj.remove(ignore)));
        return array.toJavaList(destClass);
    }

    /**
     * 去空格通用方法，会做内存缓存处理。
     * <p>
     * 当前方法接收一个bean对象，对其字符串类型的字段值做去空格处理（不在排除字段集中）
     *
     * @param bean     实体对象
     * @param excludes 需要排除的字段集
     */
    public static void trimProperty(@NonNull Object bean, String... excludes) {
        BeanMethodUtil.beanMethodCache(bean).stream().filter(m -> !ArrayUtil.contains(excludes, m.getField()))
                .forEach(m -> trimProperty(bean, m));
    }

    /**
     * 执行java bean 的标准getter方法
     *
     * @param bean   bean对象
     * @param getter getter方法
     * @return Object 获取的值
     */
    public static Object executeGetter(Object bean, Method getter) {
        try {
            return getter.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("执行标准javaBean的getter方法异常！bean={}，getter={}，message={}", bean, getter, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行java bean 的标准setter方法
     *
     * @param bean   bean对象
     * @param setter setter方法
     * @param value  需要设置的值
     */
    public static void executeSetter(Object bean, Method setter, Object value) {
        try {
            setter.invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("执行标准javaBean的setter方法异常！bean={}，setter={}，value={}，message={}", bean, setter, value, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 判断是否为Bean对象<br>
     * 判定方法是是否存在只有一个参数的setXXX方法
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     * @see #hasSetter(Class)
     */
    public static boolean isBean(Class<?> clazz) {
        return hasSetter(clazz);
    }

    /**
     * 判断是否有Setter方法<br>
     * 判定方法是是否存在只有一个参数的setXXX方法
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     */
    public static boolean hasSetter(Class<?> clazz) {
        if (ClassUtil.isNormalClass(clazz)) {
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    // 检测包含标准的setXXX方法即视为标准的JavaBean
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否为Bean对象<br>
     * 判定方法是是否存在只有一个参数的getXXX方法
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     */
    public static boolean hasGetter(Class<?> clazz) {
        if (ClassUtil.isNormalClass(clazz)) {
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 0) {
                    if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // --------------------------------------------------------------------------------------------- beanToMap

    /**
     * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
     *
     * @param bean   bean对象
     * @param fields 指定的字段
     * @return Map
     */
    public static Map<String, Object> toMap(Object bean, String... fields) {
        return toMap(bean, false, false, fields);
    }

    /**
     * 对象转Map
     *
     * @param bean          bean对象
     * @param underlineCase 是否转换为下划线模式
     * @param ignoreNull    是否忽略值为空的字段
     * @param fields        指定的字段
     * @return Map
     */
    public static Map<String, Object> toMap(Object bean, boolean underlineCase, boolean ignoreNull, String... fields) {
        return toMap(bean, new LinkedHashMap<>(), underlineCase, ignoreNull, fields);
    }

    /**
     * 对象转Map
     *
     * @param bean          bean对象
     * @param target        目标的Map
     * @param underlineCase 是否转换为下划线模式
     * @param ignoreNull    是否忽略值为空的字段
     * @param fields        指定的字段
     * @return Map
     */
    public static Map<String, Object> toMap(Object bean, Map<String, Object> target, boolean underlineCase, boolean ignoreNull,
                                            String... fields) {
        return toMap(bean, target, ignoreNull, key -> {
            if (ArrayUtil.isNotEmpty(fields) && !ArrayUtil.contains(fields, key)) {
                return null;
            }
            return underlineCase ? StringUtil.camelToUnderline(key) : key;
        });
    }

    /**
     * 对象转Map<br>
     * 通过实现{@link Editor} 可以自定义字段值，如果这个Editor返回null则忽略这个字段，以便实现：
     *
     * <pre>
     * 1. 字段筛选，可以去除不需要的字段
     * 2. 字段变换，例如实现驼峰转下划线
     * 3. 自定义字段前缀或后缀等等
     * </pre>
     *
     * @param bean       bean对象
     * @param target     目标的Map
     * @param ignoreNull 是否忽略值为空的字段
     * @param keyEditor  属性字段（Map的key）编辑器，用于筛选、编辑key
     * @return Map
     */
    public static Map<String, Object> toMap(Object bean, Map<String, Object> target, boolean ignoreNull,
                                            Editor<String> keyEditor) {
        if (bean == null) {
            return new LinkedHashMap<>();
        }
        final Collection<BeanDesc.PropDesc> props = BeanUtil.getBeanDesc(bean.getClass()).getProps();
        for (BeanDesc.PropDesc prop : props) {
            // 过滤class属性
            // 得到property对应的getter方法
            Method getter = prop.getGetter();
            if (isNull(getter)) {
                continue;
            }
            Object value;
            // 只读取有getter方法的属性
            try {
                value = getter.invoke(bean);
            } catch (Exception ignore) {
                continue;
            }
            //判断是否过滤空
            if (isNull(value) && ignoreNull) {
                continue;
            }
            String key = keyEditor.edit(prop.getFieldName());
            if (isNotEmpty(key)) {
                target.put(key, value);
            }
        }
        return target;
    }

    /**
     * 集合对象转集合Map，不进行驼峰转下划线，不忽略值为空的字段
     *
     * @param beans  bean对象集合
     * @param fields 指定的字段
     * @return Map
     */
    public static <T> List<Map<String, Object>> toMapList(List<T> beans, String... fields) {
        return toMapList(beans, false, false, fields);
    }

    /**
     * 集合对象转集合Map
     *
     * @param beans         bean对象集合
     * @param underlineCase 是否转换为下划线模式
     * @param ignoreNull    是否忽略值为空的字段
     * @param fields        指定的字段
     * @return Map
     */
    public static <T> List<Map<String, Object>> toMapList(List<T> beans, boolean underlineCase, boolean ignoreNull,
                                                          String... fields) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        for (Object bean : nullToDefault(beans, new ArrayList<>())) {
            mapList.add(toMap(bean, underlineCase, ignoreNull, fields));
        }
        return mapList;
    }

    /**
     * 获取{@link BeanDesc} Bean描述信息
     *
     * @param clazz Bean类
     * @return {@link BeanDesc}
     */
    public static BeanDesc getBeanDesc(Class<?> clazz) {
        BeanDesc beanDesc = BeanDescCache.INSTANCE.getBeanDesc(clazz);
        if (null == beanDesc) {
            beanDesc = new BeanDesc(clazz);
            BeanDescCache.INSTANCE.putBeanDesc(clazz, beanDesc);
        }
        return beanDesc;
    }

    /**
     * 获取Bean类的 getxxx方法列表
     *
     * @param bean:Bean对象
     * @return List<Method>方法列表
     */
    public static List<Method> getReadMethod(Object bean) {
        Validate.notNull(bean, "bean can't be null!");
        return getReadMethod(bean.getClass());
    }

    /**
     * 获取Bean类某个字段的 getxxx方法列表
     *
     * @param bean:Bean对象
     * @param field:Bean类的字段
     * @return Method方法
     */
    public static Method getReadMethod(Object bean, String field) {
        Validate.notNull(bean, "bean can't be null!");
        return getReadMethod(bean.getClass(), field);
    }

    /**
     * 获取Bean类的 setxxx方法列表
     *
     * @param bean:Bean对象
     * @return List<Method>方法列表
     */
    public static List<Method> getWriteMethod(Object bean) {
        Validate.notNull(bean, "bean can't be null!");
        return getWriteMethod(bean.getClass());
    }

    /**
     * 获取Bean类某个字段的 setxxx方法列表
     *
     * @param bean:Bean对象
     * @param field:Bean类的字段
     * @return Method方法
     */
    public static Method getWriteMethod(Object bean, String field) {
        Validate.notNull(bean, "bean can't be null!");
        return getWriteMethod(bean.getClass(), field);
    }

    /**
     * 获取Bean类的 getxxx方法列表
     *
     * @param clazz:Bean类
     * @return List<Method>方法列表
     */
    public static List<Method> getReadMethod(Class<?> clazz) {
        return getReadOrWriteMethod(clazz, MethodType.READ);
    }

    /**
     * 获取Bean类某个字段的 getxxx方法列表
     *
     * @param clazz:Bean类
     * @param field:Bean类的字段
     * @return Method方法
     */
    public static Method getReadMethod(Class<?> clazz, String field) {
        List<Method> list = getReadOrWriteMethod(clazz, MethodType.READ, field);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取Bean类的 getxxx方法列表
     *
     * @param clazz:Bean类
     * @return List<Method>方法列表
     */
    public static List<Method> getWriteMethod(Class<?> clazz) {
        return getReadOrWriteMethod(clazz, MethodType.WRITE);
    }

    /**
     * 获取Bean类某个字段的 getxxx方法列表
     *
     * @param clazz:Bean类
     * @param field:Bean类的字段
     * @return Method方法
     */
    public static Method getWriteMethod(Class<?> clazz, String field) {
        List<Method> list = getReadOrWriteMethod(clazz, MethodType.WRITE, field);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取Bean类的 getxxx和setxxx方法列表方法列表
     *
     * @param clazz:Bean类
     * @return List<Method>方法列表
     */
    public static List<Method> getReadAndWriteMethod(Class<?> clazz) {
        return getReadOrWriteMethod(clazz, null);
    }

    /**
     * 获取Bean类某个字段的 getxxx和setxxx方法列表方法列表
     *
     * @param clazz:Bean类
     * @param field:Bean对象的字段
     * @return List<Method>方法列表
     */
    public static List<Method> getReadAndWriteMethod(Class<?> clazz, String field) {
        return getReadOrWriteMethod(clazz, null, field);
    }

    /**
     * 获取Bean类的 getxxx和setxxx方法列表方法列表
     *
     * @param bean:Bean对象
     * @return List<Method>方法列表
     */
    public static List<Method> getReadAndWriteMethod(Object bean) {
        Validate.notNull(bean, "bean can't be null!");
        return getReadOrWriteMethod(bean.getClass(), null);
    }

    /**
     * 获取Bean类某个字段的 getxxx和setxxx方法列表方法列表
     *
     * @param bean:Bean对象
     * @param field:Bean对象的字段
     * @return List<Method>方法列表
     */
    public static List<Method> getReadAndWriteMethod(Object bean, String field) {
        Validate.notNull(bean, "bean can't be null!");
        return getReadOrWriteMethod(bean.getClass(), null, field);
    }


    /**
     * 获取类属性对象
     *
     * @param bean:Bean对象
     * @param fieldName:属性名
     * @return 类属性对象
     */
    public static Field getField(Object bean, String fieldName) {
        Validate.notNull(bean, "bean can't be null!");
        return getField(bean.getClass(), fieldName);
    }

    /**
     * 获取类属性对象
     *
     * @param clazz:Bean类
     * @param fieldName:属性变量名
     * @return 类属性对象
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        List<Field> fieldList = getFields(clazz, true);
        for (Field field : fieldList) {
            if (ObjectUtil.equal(field.getName(), fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 获取类属性对象列表
     *
     * @param bean:Bean对象
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Object bean) {
        Validate.notNull(bean, "bean can't be null!");
        return getFields(bean.getClass());
    }

    /**
     * 获取类属性对象列表
     *
     * @param bean:Bean对象
     * @param excludes:需要排除的字段集
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Object bean, String... excludes) {
        return getFields(bean, false, excludes);
    }

    /**
     * 获取类属性对象列表
     *
     * @param bean:Bean对象
     * @param superclass:是否忽略父类的属性
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Object bean, boolean superclass) {
        Validate.notNull(bean, "bean can't be null!");
        return getFields(bean.getClass(), superclass);
    }

    /**
     * 获取类属性对象列表
     *
     * @param bean:Bean对象
     * @param superclass:是否忽略父类的属性
     * @param excludes:需要排除的字段集
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Object bean, boolean superclass, String... excludes) {
        Validate.notNull(bean, "bean can't be null!");
        return getFields(bean.getClass(), superclass, excludes);
    }

    /**
     * 获取类属性对象列表
     *
     * @param clazz:Bean类
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Class<?> clazz) {
        return getFields(clazz, false);
    }

    /**
     * 获取类属性对象列表
     *
     * @param clazz:Bean类
     * @param excludes:需要排除的字段集
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Class<?> clazz, String... excludes) {
        return getFields(clazz, false, excludes);
    }

    /**
     * 获取类的属性对象列表
     *
     * @param clazz:Bean类
     * @param superclass: 是否忽略父类的属性
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Class<?> clazz, boolean superclass) {
        return getFields(clazz, superclass, "");
    }

    /**
     * 获取类的属性对象列表
     *
     * @param clazz:Bean类
     * @param superclass:       是否忽略父类的属性
     * @param excludes:需要排除的字段集
     * @return 类属性对象列表
     */
    public static List<Field> getFields(Class<?> clazz, boolean superclass, String... excludes) {
        if (superclass) {
            return screenFields(clazz.getDeclaredFields(), CollectionUtil.toList(excludes));
        }
        List<Field> fieldList = CollectionUtil.newArrayList();
        if (ObjectUtil.isNotEmpty(clazz)) {
            for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
                fieldList.addAll(screenFields(c.getDeclaredFields(), CollectionUtil.toList(excludes)));
            }
        }
        return fieldList;
    }

    /**
     * 往bean对象的属性注入值
     *
     * @param bean:bean对象
     * @param property:属性
     * @param value:对应的值
     */
    public static void setProperty(Object bean, String property, Object value) {
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(bean, property, value);
        } catch (Exception e) {
            throw new RuntimeException("设置数据的属性值异常：" + e.getMessage(), e);
        }
    }

    /**
     * 获取指定类型超类中泛型的实际类型
     *
     * @param clazz 指定类型
     * @param index 需要获取的第几个泛型的类型
     * @return Class
     */
    public static Class<?> getSuperClassActualType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        //检验泛型是否被参数化
        if (!(genType instanceof ParameterizedType)) {
            log.warn(StringUtil.format("{}'s superclass not ParameterizedType", clazz.getSimpleName()));
            return null;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index < 0 || index >= params.length) {
            log.warn(StringUtil.format("Index: {}, Size of {}'s Parameterized Type: {} .", index, clazz
                    .getSimpleName(), params.length));
            return null;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(StringUtil.format("{} not set the actual class on superclass generic parameter", clazz.getSimpleName()));
            return null;
        }
        return (Class<?>) params[index];
    }

    /**
     * 获取类型的泛型参数类型
     *
     * @param type  指定类型
     * @param index 需要获取的第几个泛型类型
     * @return Class 泛型类型
     */
    public static Class<?> getParameterizedType(Type type, int index) {
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (index >= 0 && index < types.length) {
                return (Class<?>) types[index];
            }
        }
        return null;
    }

    /**
     * 根据传入的类型，构造一个类型的对象
     *
     * @param clazz class类型
     * @param <T>   泛型参数
     * @return 实例化后的对象
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("根据class类型[" + clazz + "]实例化对象异常！原因：" + e.getMessage(), e);
        }
    }

    /**
     * 安全获取bean对象的属性值
     *
     * @param bean     bean对象
     * @param property 属性名
     * @param <V>      返回值泛型类型
     * @return V 属性值
     */
    @SuppressWarnings("unchecked")
    public static <V> V getProperty(Object bean, String property) {
        try {
            //先从缓存获取执行方法，若没有，则通过bean对象获取，并添加到缓存中
            String cacheKey = bean.getClass().getName() + "#" + property;
            Method method = CACHE_METHOD.get(cacheKey);
            if (isNull(method)) {
                method = bean.getClass().getMethod(StringUtil.getterName(property));
                CACHE_METHOD.put(cacheKey, method);
            }
            return (V) method.invoke(bean);
        } catch (Exception e) {
            log.error("获取bean属性异常！bean=" + bean + " ,property" + property + ", 异常：" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据bean类型、字段名称、annotation类型获取字段上的annotation属性，获取规则：
     * <p>
     * 1. 首先从子类中获取，若取不到时会到父类获取
     * <p>
     * 2. 子类和父类同时存在的情况，取子类
     *
     * @param beanClass       bean类型
     * @param property        字段属性名
     * @param annotationClass annotation类型
     * @param <T>             annotation泛型参数
     * @return T annotation属性
     */
    public static <T extends Annotation> T getPropertyAnnotation(Class<?> beanClass, String property, Class<T> annotationClass) {
        return getFields(beanClass, false).stream().filter(field -> equal(field.getName(), property))
                .map(field -> field.getAnnotation(annotationClass)).filter(ObjectUtil::isNotNull)
                .findFirst().orElse(null);
    }

    //////////////////////////////// 私有方法 /////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取方法对象列表
     */
    private static List<Method> getReadOrWriteMethod(Class<?> clazz, MethodType methodType) {
        List<Method> methods = CollectionUtil.newArrayList();
        PropertyDescriptor[] descriptors = PropertyUtil.getPropertyDescriptors(clazz);
        for (PropertyDescriptor descriptor : descriptors) {
            if ("class".equals(descriptor.getName())) {
                continue;
            }
            if (ObjectUtil.equal(methodType, MethodType.READ)) {
                methods.add(descriptor.getReadMethod());
                continue;
            }
            if (ObjectUtil.equal(methodType, MethodType.WRITE)) {
                methods.add(descriptor.getWriteMethod());
                continue;
            }
            methods.add(descriptor.getReadMethod());
            methods.add(descriptor.getWriteMethod());
        }
        return methods;
    }

    /**
     * 获取方法对象
     */
    private static List<Method> getReadOrWriteMethod(Class<?> clazz, MethodType methodType, String field) {
        List<Method> methods = CollectionUtil.newArrayList();
        PropertyDescriptor[] descriptors = PropertyUtil.getPropertyDescriptors(clazz);
        for (PropertyDescriptor descriptor : descriptors) {
            if (ObjectUtil.equal(field, descriptor.getName())) {
                if (ObjectUtil.equal(methodType, MethodType.READ)) {
                    methods.add(descriptor.getReadMethod());
                } else if (ObjectUtil.equal(methodType, MethodType.WRITE)) {
                    methods.add(descriptor.getWriteMethod());
                } else {
                    methods.add(descriptor.getReadMethod());
                    methods.add(descriptor.getWriteMethod());
                }
                break;
            }
        }
        return methods;
    }

    /**
     * 获取复制对象
     */
    private static BeanCopier getCopier(Object orig, Object dest, Converter converter) {
        boolean useConvert = ObjectUtil.isNotEmpty(converter);
        String key = connectKey(orig.getClass().getName(), dest.getClass().getName(), useConvert);
        BeanCopier copier = CACHE_COPIERS.get(key);
        if (ObjectUtil.isEmpty(copier)) {
            copier = BeanCopier.create(orig.getClass(), dest.getClass(), useConvert);
            CACHE_COPIERS.put(key, copier);
        }
        return copier;
    }

    /**
     * 生成connectKey
     */
    private static String connectKey(String orig, String dest, boolean useConvert) {
        return orig + CONNECT_OPERATOR + dest + CONNECT_OPERATOR + useConvert;
    }

    /**
     * 过滤字段
     */
    private static List<Field> screenFields(Field[] fields, List<String> excludes) {
        return Arrays.stream(fields).filter(field -> !excludes.contains(field.getName())).collect(Collectors.toList());
    }

    /**
     * 执行去空格处理，会先执行getter获取字段值，对值去空格后再复制给字段
     */
    private static void trimProperty(Object bean, BeanMethod beanMethod) {
        Object value = executeGetter(bean, beanMethod.getGetterMethod());
        if (isNotNull(value)) {
            executeSetter(bean, beanMethod.getSetterMethod(), value.toString().trim());
        }
    }
}
