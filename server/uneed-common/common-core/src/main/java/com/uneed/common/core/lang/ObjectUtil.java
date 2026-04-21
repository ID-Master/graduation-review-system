package com.uneed.common.core.lang;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.convert.Convert;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

/**
 * 通用的Object工具类。
 * 该类提供了对java对象的通用非空判断、java对象的通用比较等基本方法，便捷开发。
 *
 * @author diablo
 * @date 2017/6/13
 * @since 1.0.0
 */
public final class ObjectUtil {

    public static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 私有化构造函数，禁止实例化该类
     */
    private ObjectUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 判断对象是否为null
     *
     * @param obj 需要判断的对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 判断对象是否不为null
     *
     * @param obj 需要判断的对象
     * @return 对象是否不为null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 判断对象是否为null或empty的通用方法，支持字符串、集合、数组、枚举、迭代器等数据类型。
     * 1. Object类型对象，如果为null，返回true，同{@link java.util.Objects#isNull(Object)}
     * 2. 字符串类型，如果为null或是空白字符，返回true。同{@link com.uneed.common.core.lang.StringUtil#isEmpty(CharSequence)}
     * 3. 集合类型，如果为null或是集合的元素对象数量为0，返回true。集合类型可以是List,Set,Map。
     * 4. 数组类型，如果为null或是数组的长度为0，返回true。
     * 5. 迭代器，如果为null或是迭代器对象中不存在可迭代的元素，返回true。
     * 6. 枚举类型，如果为null或是枚举的元素数量为0，返回true。
     * 7. 其他类型，如果为null，返回true。
     *
     * @param obj 需要判断的对象
     * @return boolean 是否为null或empty
     */
    public static boolean isEmpty(Object obj) {
        // 判断对象是否为空
        if (isNull(obj)) {
            return true;
        }
        // 判断字符串
        if (obj instanceof CharSequence) {
            return StringUtil.isEmpty((CharSequence) obj);
        }
        // 判断集合
        return sizeIsEmpty(obj);
    }

    /**
     * 判断是否为空或空白字符串
     *
     * @param cs 需要判断的字符串
     * @return boolean 是否为null或empty
     */
    public static boolean isEmpty(CharSequence cs) {
        return StringUtil.isEmpty(cs);
    }

    /**
     * 判断是否为空或空元素的集合
     *
     * @param coll 需要判断的集合
     * @return boolean 是否为null或empty
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * 判断是否为空或空元素的Map
     *
     * @param map 需要判断的Map
     * @return boolean 是否为null或empty
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * 判断是否为空或空元素的数组
     *
     * @param array 需要判断的数组
     * @return boolean 是否为null或empty
     */
    public static boolean isEmpty(Object[] array) {
        return isNull(array) || array.length == 0;
    }

    /**
     * 判断对象是否非null或非empty的通用方法，也支持字符串、集合、数组、枚举、迭代器等数据类型。实际为{@link #isEmpty(Object)}取反。
     *
     * @param obj 需要判断的对象
     * @return boolean 是否非null或非empty
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断字符串是否非null或是非empty
     *
     * @param cs 需要判断的字符串
     * @return 是否为非null或非empty
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 判断是否为非空或非空元素的集合
     *
     * @param coll 需要判断的集合
     * @return boolean 是否为非null或非empty
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断是否为非空或非空元素的Map
     *
     * @param map 需要判断的Map
     * @return boolean 是否为非null或非empty
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    /**
     * 判断是否为非空或非空元素的数组
     *
     * @param array 需要判断的数组
     * @return boolean 是否为非null或非empty
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 比较俩个对象是否相等。该方法会先用"=="比较俩个对象是否相等，若不相等的话，再调用对象的equals方法进行比较。
     * 如果传入的对象为原生类型，则会先将原生类型对象转换为对应的包装类型对象，再进行比较。
     *
     * @param orig 源对象
     * @param dest 目标对象
     * @return boolean 俩个对象是否相等
     */
    public static boolean equal(Object orig, Object dest) {
        return (orig == dest) || (isNotNull(orig) && orig.equals(dest));
    }

    /**
     * 比较俩个对象是否不相等。取反equal(Object orig, Object dest)
     *
     * @param orig 源对象
     * @param dest 目标对象
     * @return boolean 俩个对象是否不相等
     */
    public static boolean notEqual(Object orig, Object dest) {
        return !equal(orig, dest);
    }

    /**
     * 接收一个java对象，若传入的对象为null，则返回设置的默认值value，否则，返回传入的对象。
     * 该方法主要是用来在编程过程中使用对象时出现NPE异常。
     *
     * @param obj          任意java对象
     * @param defaultValue 默认值，不能为空
     * @param <T>          泛型对象
     * @return 返回不为null的T类型对象
     */
    public static <T> T nullToDefault(T obj, T defaultValue) {
        return isNull(obj) ? defaultValue : obj;
    }

    /**
     * 通用的类型转换
     *
     * @param obj  需要转换的数据
     * @param type 需要转换的类型
     * @param <T>  转换后的类型泛型
     * @return T 转换后的数据对象
     */
    public static <T> T convert(Object obj, Class<T> type) {
        return Convert.convert(type, obj);
    }

    /**
     * 通用的类型转换，若转换不成功，则设置为默认值
     *
     * @param obj          需要转换的数据
     * @param defaultValue 默认值
     * @param <T>          转换后的类型泛型
     * @return T 转换后的数据对象
     */
    public static <T> T convert(Object obj, T defaultValue) {
        try {
            return Convert.convert(null, obj, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 对象安全的toString方法
     *
     * @param obj 需要toString的对象
     * @return toString后的值
     */
    public static String toString(Object obj) {
        return StringUtil.toString(obj);
    }

    /**
     * 判断数字是否大于0
     *
     * @param value 要判断的数字
     * @return boolean 是否大于0
     */
    public static boolean gtZero(Number value) {
        return isNotNull(value) && new BigDecimal(value.toString()).compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断数字是否大于等于0
     *
     * @param value 要判断的数字
     * @return boolean 是否大于等于0
     */
    public static boolean geZero(Number value) {
        return isNotNull(value) && new BigDecimal(value.toString()).compareTo(BigDecimal.ZERO) > -1;
    }

    /**
     * 对数据进行分组，返回分组后的集合，默认按数量为1000进行分组
     *
     * @param dataList 需要分组的数据
     * @param <T>      数据泛型
     * @return List<List < T>> 分组后的数据
     */
    public static <T> List<List<T>> group(List<T> dataList) {
        return group(dataList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 对数据进行分组，返回分组后的集合
     *
     * @param dataList 需要分组的数据
     * @param size     分组数据的大小
     * @param <T>      数据泛型
     * @return List<List < T>> 分组后的数据
     */
    public static <T> List<List<T>> group(List<T> dataList, int size) {
        List<List<T>> lists = Lists.newArrayList();
        if (isEmpty(dataList)) {
            return lists;
        }
        size = Math.max(size, 1);
        int len = dataList.size();
        if (len <= size) {
            lists.add(dataList);
            return lists;
        }
        //分批数
        int part = len / size;
        for (int i = 0; i < part; i++) {
            int form = i * size;
            lists.add(dataList.subList(i * size, form + size));
        }
        //是否有余数
        int remainder = len % size;
        if (remainder > 0) {
            lists.add(dataList.subList(part * size, len));
        }
        return lists;
    }

    /**
     * 对数据进行分页，会默认最小值为1
     *
     * @param dataList 要分页的数据
     * @param page     页码
     * @param size     每页大小
     * @param <T>      数据泛型
     * @return 分页后的数据
     */
    public static <T> List<T> dataPaging(List<T> dataList, int page, int size) {
        List<List<T>> group = group(dataList, size);
        page = Math.max(page, 1);
        return page <= group.size() ? group.get(page - 1) : Lists.newArrayList();
    }

    /**
     * 将集合转换成map对象，value为集合元素
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param <K>        key的泛型类型
     * @param <T>        值的泛型类型
     * @return 转换后的map对象
     */
    public static <K, T> Map<K, T> toMap(Collection<T> collection, String keyField) {
        return toMap(collection, it -> BeanUtil.getProperty(it, keyField));
    }

    /**
     * 将集合转换成map对象，value为集合元素
     *
     * @param collection  需要转换的集合
     * @param keyFunction key转换函数
     * @param <K>         key的泛型类型
     * @param <T>         值的泛型类型
     * @return 转换后的map对象
     */
    public static <K, T> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyFunction) {
        return toMap(collection, keyFunction, it -> it);
    }

    /**
     * 将集合转换成map对象，value为指定字段集合元素
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param valueField value的字段名称
     * @param <K>        key的泛型类型
     * @param <V>        值的泛型类型
     * @param <T>        集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, V> toMap(Collection<T> collection, String keyField, String valueField) {
        return toMap(collection, keyField, valueField, false);
    }

    /**
     * 将集合转换成map对象，value为指定字段集合元素
     *
     * @param collection    需要转换的集合
     * @param keyFunction   key转换函数
     * @param valueFunction value转换函数
     * @param <K>           key的泛型类型
     * @param <V>           值的泛型类型
     * @param <T>           集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        return toMap(collection, keyFunction, valueFunction, false);
    }

    /**
     * 将集合转换成map对象，value为指定字段集合元素
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param valueField value的字段名称
     * @param <K>        key的泛型类型
     * @param <V>        值的泛型类型
     * @param <T>        集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, V> toMap(Collection<T> collection, String keyField, String valueField, boolean filterNull) {
        return toMap(collection, it -> BeanUtil.getProperty(it, keyField), it -> BeanUtil.getProperty(it, valueField), filterNull);
    }

    /**
     * 将集合转换成map对象，value为指定字段集合元素
     *
     * @param collection    需要转换的集合
     * @param keyFunction   key转换函数
     * @param valueFunction value转换函数
     * @param <K>           key的泛型类型
     * @param <V>           值的泛型类型
     * @param <T>           集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyFunction, Function<T, V> valueFunction, boolean filterNull) {
        Map<K, V> map = Maps.newHashMap();
        if (isEmpty(collection)) {
            return map;
        }
        Validate.notNull(keyFunction, "keyFunction can't be null!");
        Validate.notNull(valueFunction, "valueFunction can't be null!");
        collection.stream().filter(ObjectUtil::isNotNull).forEach(it -> {
            K k = keyFunction.apply(it);
            if (isEmpty(k)) {
                return;
            }
            V v = valueFunction.apply(it);
            if (filterNull && isNull(v)) {
                return;
            }
            map.put(k, v);
        });
        return map;
    }

    /**
     * 将集合转换成map对象，value为相同key值的集合元素列表
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param <K>        key的泛型类型
     * @param <T>        值的泛型类型
     * @return 转换后的map对象
     */
    public static <K, T> Map<K, List<T>> toMapList(Collection<T> collection, String keyField) {
        return toMapList(collection, keyField, false);
    }

    /**
     * 将集合转换成map对象，value为相同key值的集合元素列表
     *
     * @param collection  需要转换的集合
     * @param keyFunction key转换函数
     * @param <K>         key的泛型类型
     * @param <T>         值的泛型类型
     * @return 转换后的map对象
     */
    public static <K, T> Map<K, List<T>> toMapList(Collection<T> collection, Function<T, K> keyFunction) {
        return toMapList(collection, keyFunction, false);
    }

    /**
     * 将集合转换成map对象，value为相同key值的集合元素列表
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param <K>        key的泛型类型
     * @param <T>        值的泛型类型
     * @return 转换后的map对象
     */
    public static <K, T> Map<K, List<T>> toMapList(Collection<T> collection, String keyField, boolean filterNull) {
        return toMapList(collection, it -> BeanUtil.getProperty(it, keyField), filterNull);
    }

    /**
     * 将集合转换成map对象，value为相同key值的集合元素列表
     *
     * @param collection  需要转换的集合
     * @param keyFunction key转换函数
     * @param filterNull  是否过滤null
     * @param <K>         key的泛型类型
     * @param <T>         值的泛型类型
     * @return 转换后的map对象
     */
    public static <K, T> Map<K, List<T>> toMapList(Collection<T> collection, Function<T, K> keyFunction, boolean filterNull) {
        return toMapList(collection, keyFunction, it -> it, filterNull);
    }

    /**
     * 将集合转换成map对象，value为相同key值的指定字段集合元素
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param valueField value的字段名称
     * @param <K>        key的泛型类型
     * @param <V>        值的泛型类型
     * @param <T>        集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, List<V>> toMapList(Collection<T> collection, String keyField, String valueField) {
        return toMapList(collection, keyField, valueField, false);
    }

    /**
     * 将集合转换成map对象，value为相同key值的指定字段集合元素
     *
     * @param collection    需要转换的集合
     * @param keyFunction   key转换函数
     * @param valueFunction value转换函数
     * @param <K>           key的泛型类型
     * @param <V>           值的泛型类型
     * @param <T>           集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, List<V>> toMapList(Collection<T> collection, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        return toMapList(collection, keyFunction, valueFunction, false);
    }

    /**
     * 将集合转换成map对象，value为相同key值的指定字段集合元素
     *
     * @param collection 需要转换的集合
     * @param keyField   key的字段名称
     * @param valueField value的字段名称
     * @param filterNull 是否过滤null
     * @param <K>        key的泛型类型
     * @param <V>        值的泛型类型
     * @param <T>        集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, List<V>> toMapList(Collection<T> collection, String keyField, String valueField, boolean filterNull) {
        return toMapList(collection, it -> BeanUtil.getProperty(it, keyField), it -> BeanUtil.getProperty(it, valueField), filterNull);
    }

    /**
     * 将集合转换成map对象，value为相同key值的指定字段集合元素
     *
     * @param collection    需要转换的集合
     * @param keyFunction   key转换
     * @param valueFunction value转换函数
     * @param filterNull    是否过滤null
     * @param <K>           key的泛型类型
     * @param <V>           值的泛型类型
     * @param <T>           集合对象的泛型类型
     * @return 转换后的map对象
     */
    public static <K, V, T> Map<K, List<V>> toMapList(Collection<T> collection, Function<T, K> keyFunction, Function<T, V> valueFunction, boolean filterNull) {
        Map<K, List<V>> map = Maps.newHashMap();
        if (isEmpty(collection)) {
            return map;
        }
        Validate.notNull(keyFunction, "keyFunction can't be null!");
        Validate.notNull(valueFunction, "valueFunction can't be null!");
        collection.stream().filter(ObjectUtil::isNotNull).forEach(it -> {
            K k = keyFunction.apply(it);
            if (isEmpty(k)) {
                return;
            }
            addMapValue(map, k, valueFunction.apply(it), filterNull);
        });
        return map;
    }

    /**
     * 对比俩个对象的hashCode是否相当，若俩个对象不为空，会调用对象的compareTo方法进行对比
     * 若源对象等于目标对象，返回0，
     * 若源对象为空，返回-1，
     * 若目标对象为空，返回1，
     * 其他情况，返回源目标对比目标对象的hashCode
     *
     * @param orig 源对象
     * @param dest 目标对象
     * @param <T>  用来限制参数的泛型，必须是继承了Comparable
     * @return int 对比后的值
     */
    public static <T extends Comparable<? super T>> int compare(T orig, T dest) {
        if (equal(orig, dest)) {
            return 0;
        }

        if (isNull(orig)) {
            return -1;
        }

        if (isNull(dest)) {
            return 1;
        }
        return orig.compareTo(dest);
    }

    @SuppressWarnings("rawtypes")
    private static boolean sizeIsEmpty(Object obj) {
        //集合
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        //map
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        //数组
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length == 0;
        }
        //迭代器
        if (obj instanceof Iterable) {
            return ((Iterable) obj).iterator().hasNext();
        }
        if (obj instanceof Iterator) {
            return !((Iterator) obj).hasNext();
        }
        //枚举
        if (obj instanceof Enumeration) {
            return !((Enumeration) obj).hasMoreElements();
        }
        return false;
    }

    private static <K, V> void addMapValue(Map<K, List<V>> map, K k, V v, boolean filterNull) {
        List<V> list = map.get(k);
        if (isNull(list)) {
            list = Lists.newArrayList();
            map.put(k, list);
        }
        if (filterNull && isNull(v)) {
            return;
        }
        list.add(v);
    }
}
