package com.uneed.common.core.lang;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 校验某些对象或值是否符合规定，否则抛出异常。经常用于做变量检查
 *
 * @author diablo
 * @date 2019/9/4
 * @since 1.0.0
 */
public class Validate {

    /**
     * 私有化构造函数，禁止实例化该类
     */
    private Validate() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    // ----------------------------------------------------------------------------------------------------------- Check is true

    /**
     * 断言是否为真，如果为 {@code false} 抛出 {@code IllegalArgumentException} 异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
     * </pre>
     *
     * @param expression 波尔值
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isTrue(final boolean expression) {
        isTrue(expression, "[Validate failed] - this expression must be true");
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出 {@code IllegalArgumentException} 异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
     * </pre>
     *
     * @param expression 波尔值
     * @param message    错误抛出异常附带的消息模板，变量用{}代替
     * @param params     参数列表
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isTrue(final boolean expression, final String message, final Object... params) {
        if (!expression) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
    }

    // ----------------------------------------------------------------------------------------------------------- Check not null

    /**
     * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常
     *
     * <pre class="code">
     * Assert.notNull(clazz);
     * </pre>
     *
     * @param <T> 被检查对象类型
     * @param obj 被检查对象
     * @return 非空对象
     * @throws IllegalArgumentException if the obj is {@code null}
     */
    public static <T> T notNull(final T obj) {
        return notNull(obj, "[Validate failed] - this argument is required; it must not be null");
    }

    /**
     * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常 Assert that an obj is not {@code null} .
     *
     * <pre class="code">
     * Assert.notNull(clazz, "The class must not be null");
     * </pre>
     *
     * @param <T>     被检查对象泛型类型
     * @param obj     被检查对象
     * @param message 错误消息模板，变量使用{}表示
     * @param params  参数
     * @return 被检查后的对象
     * @throws IllegalArgumentException if the obj is {@code null}
     */
    public static <T> T notNull(final T obj, final String message, final Object... params) {
        if (obj == null) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return obj;
    }

    // ----------------------------------------------------------------------------------------------------------- Check empty

    /**
     * 断言给定数组是否包含元素，数组必须不为 {@code null} 且至少包含一个元素
     *
     * <pre class="code">
     * Assert.notEmpty(array, "The array must have elements");
     * </pre>
     *
     * @param array 被检查的数组
     * @return 被检查的数组
     * @throws IllegalArgumentException if the obj array is {@code null} or has no elements
     */
    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, "[Validate failed] - this array must not be empty: it must contain at least 1 element");
    }

    /**
     * 断言给定数组是否包含元素，数组必须不为 {@code null} 且至少包含一个元素
     *
     * <pre class="code">
     * Assert.notEmpty(array, "The array must have elements");
     * </pre>
     *
     * @param array   被检查的数组
     * @param message 异常时的消息模板
     * @param params  参数列表
     * @return 被检查的数组
     * @throws IllegalArgumentException if the obj array is {@code null} or has no elements
     */
    public static <T> T[] notEmpty(final T[] array, final String message, final Object... params) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return array;
    }

    /**
     * 检查给定字符串是否为空或空白字符串，为空抛出 {@link IllegalArgumentException}
     *
     * <pre class="code">
     * Assert.notEmpty(name);
     * </pre>
     *
     * @param text 被检查字符串
     * @return 被检查的字符串
     * @throws IllegalArgumentException 被检查字符串为空或是空白字符
     * @see StringUtil#isNotEmpty(CharSequence)
     */
    public static <T extends CharSequence> T notEmpty(final T text) {
        return notEmpty(text, "[Validate failed] - this String argument must have length; it must not be null or empty");
    }

    /**
     * 检查给定字符串是否为空或空白字符串，为空抛出 {@link IllegalArgumentException}
     *
     * <pre class="code">
     * Assert.notEmpty(name, "Name must not be empty");
     * </pre>
     *
     * @param text    被检查字符串
     * @param message 错误消息模板，变量使用{}表示
     * @param params  参数
     * @return 非空字符串
     * @throws IllegalArgumentException 被检查字符串为空或空白字符串
     * @see StringUtil#isNotEmpty(CharSequence)
     */
    public static <T extends CharSequence> T notEmpty(final T text, final String message, final Object... params) {
        if (StringUtil.isEmpty(text)) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return text;
    }

    /**
     * 断言给定集合非空
     *
     * <pre class="code">
     * Assert.notEmpty(collection);
     * </pre>
     *
     * @param <T>        集合元素类型
     * @param collection 被检查的集合
     * @return 被检查集合
     * @throws IllegalArgumentException if the collection is {@code null} or has no elements
     */
    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, "[Validate failed] - this collection must not be empty: it must contain at least 1 element");
    }

    /**
     * 断言给定集合非空
     *
     * <pre class="code">
     * Assert.notEmpty(collection, "Collection must have elements");
     * </pre>
     *
     * @param <T>        集合元素类型
     * @param collection 被检查的集合
     * @param message    异常时的消息模板
     * @param params     参数列表
     * @return 非空集合
     * @throws IllegalArgumentException if the collection is {@code null} or has no elements
     */
    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... params) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return collection;
    }

    /**
     * 断言给定Map非空
     *
     * <pre class="code">
     * Assert.notEmpty(map, "Map must have entries");
     * </pre>
     *
     * @param map 被检查的Map
     * @return 被检查的Map
     * @throws IllegalArgumentException if the map is {@code null} or has no entries
     */
    public static <T extends Map<?, ?>> T notEmpty(T map) {
        return notEmpty(map, "[Validate failed] - this map must not be empty; it must contain at least one entry");
    }

    /**
     * 断言给定Map非空
     *
     * <pre class="code">
     * Assert.notEmpty(map, "Map must have entries");
     * </pre>
     *
     * @param map     被检查的Map
     * @param message 异常时的消息模板
     * @param params  参数列表
     * @return 被检查的Map
     * @throws IllegalArgumentException if the map is {@code null} or has no entries
     */
    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... params) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return map;
    }

    /**
     * 断言给定Iterable迭代器非空
     *
     * <pre class="code">
     * Assert.notEmpty(iterable, "Iterable must have entries");
     * </pre>
     *
     * @param iterable 被检查的迭代器
     * @return 被检查的Iterable
     * @throws IllegalArgumentException if the map is {@code null} or has no entries
     */
    public static <T extends Iterable<?>> T notEmpty(T iterable) {
        return notEmpty(iterable, "[Validate failed] - this iterable must not be empty; it must contain at least one entry");
    }

    /**
     * 断言给定Iterable迭代器非空
     *
     * <pre class="code">
     * Assert.notEmpty(iterable, "Iterable must have entries");
     * </pre>
     *
     * @param iterable 被检查的迭代器
     * @param message  异常时的消息模板
     * @param params   参数列表
     * @return 被检查的Iterable
     * @throws IllegalArgumentException if the map is {@code null} or has no entries
     */
    public static <T extends Iterable<?>> T notEmpty(T iterable, String message, Object... params) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return iterable;
    }

    // ----------------------------------------------------------------------------------------------------------- Check type instanceOf

    /**
     * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array);
     * </pre>
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 被检查的数组
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static <T> T[] noNullElements(final T[] array) {
        return noNullElements(array, "[Validate failed] - this array must not contain any null elements");
    }

    /**
     * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array, "The array must have non-null elements");
     * </pre>
     *
     * @param <T>     数组元素类型
     * @param array   被检查的数组
     * @param message 异常时的消息模板
     * @param params  参数列表
     * @return 被检查的数组
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static <T> T[] noNullElements(final T[] array, final String message, final Object... params) {
        notNull(array);
        for (T t : array) {
            if (t == null) {
                throw new IllegalArgumentException(StringUtil.format(message, params));
            }
        }
        return array;
    }

    /**
     * 断言给定集合是否不包含{@code null}元素，如果集合为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array);
     * </pre>
     *
     * @param <T>      集合元素类型
     * @param iterable 被检查的集合
     * @return 被检查的集合
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static <T extends Iterable<?>> T noNullElements(final T iterable) {
        return noNullElements(iterable, "[Validate failed] - this collection must not contain any null elements");
    }

    /**
     * 断言给定集合是否不包含{@code null}元素，如果集合为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array, "The array must have non-null elements");
     * </pre>
     *
     * @param <T>      集合元素类型
     * @param iterable 被检查的集合
     * @param message  异常时的消息模板
     * @param params   参数列表
     * @return 被检查的集合
     * @throws IllegalArgumentException if the object collection contains a {@code null} element
     */
    public static <T extends Iterable<?>> T noNullElements(final T iterable, final String message, final Object... params) {
        notNull(iterable);
        int i = 0;
        for (final Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                throw new IllegalArgumentException(StringUtil.format(message, params));
            }
        }
        return iterable;
    }

    // ----------------------------------------------------------------------------------------------------------- Check type instanceOf

    /**
     * 断言给定对象是否是给定类的实例
     *
     * <pre class="code">
     * Assert.instanceOf(Foo.class, foo);
     * </pre>
     *
     * @param <T>  被检查对象泛型类型
     * @param type 被检查对象匹配的类型
     * @param obj  被检查对象
     * @return 被检查的对象
     * @throws IllegalArgumentException if the obj is not an instance of clazz
     * @see Class#isInstance(Object)
     */
    public static <T> T isInstanceOf(Class<?> type, T obj) {
        return isInstanceOf(type, obj, "Object [{}] is not instanceof [{}]", obj, type);
    }

    /**
     * 断言给定对象是否是给定类的实例
     *
     * <pre class="code">
     * Assert.instanceOf(Foo.class, foo);
     * </pre>
     *
     * @param <T>     被检查对象泛型类型
     * @param type    被检查对象匹配的类型
     * @param obj     被检查对象
     * @param message 异常时的消息模板
     * @param params  参数列表
     * @return 被检查对象
     * @throws IllegalArgumentException if the obj is not an instance of clazz
     * @see Class#isInstance(Object)
     */
    public static <T> T isInstanceOf(Class<?> type, T obj, String message, Object... params) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
        return obj;
    }

    // ----------------------------------------------------------------------------------------------------------- Check superType isAssignable subType

    /**
     * 断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.
     *
     * <pre class="code">
     * Assert.isAssignable(Number.class, myClass);
     * </pre>
     *
     * @param superType 需要检查的父类或接口
     * @param subType   需要检查的子类
     * @throws IllegalArgumentException 如果子类非继承父类，抛出此异常
     */
    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "[{}] is not assignable to [{}]", subType, superType);
    }

    /**
     * 断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.
     *
     * <pre class="code">
     * Assert.isAssignable(Number.class, myClass);
     * </pre>
     *
     * @param superType 需要检查的父类或接口
     * @param subType   需要检查的子类
     * @param message   异常时的消息模板
     * @param params    参数列表
     * @throws IllegalArgumentException 如果子类非继承父类，抛出此异常
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, String message, Object... params) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(StringUtil.format(message, params));
        }
    }

    // ----------------------------------------------------------------------------------------------------------- Check state

    /**
     * 检查boolean表达式，当检查结果为false时抛出 {@code IllegalStateException}。
     *
     * <pre class="code">
     * Assert.state(id == null);
     * </pre>
     *
     * @param expression boolean 表达式
     * @throws IllegalStateException 表达式为 {@code false} 抛出此异常
     */
    public static void state(boolean expression) throws IllegalStateException {
        state(expression, "[Validate failed] - this state invariant must be true");
    }

    /**
     * 检查boolean表达式，当检查结果为false时抛出 {@code IllegalStateException}。
     *
     * <pre class="code">
     * Assert.state(id == null, "The id property must not already be initialized");
     * </pre>
     *
     * @param expression boolean 表达式
     * @param message    异常时的消息模板
     * @param params     参数列表
     * @throws IllegalStateException 表达式为 {@code false} 抛出此异常
     */
    public static void state(boolean expression, String message, Object... params) {
        if (!expression) {
            throw new IllegalStateException(StringUtil.format(message, params));
        }
    }

    // ----------------------------------------------------------------------------------------------------------- Check index

    /**
     * 检查字符串下标是否越界
     *
     * @param chars 需要检查的字符串对象
     * @param index 长度
     * @param <T>   集合泛型
     * @return 被检查的字符串对象
     * @throws IllegalArgumentException  如果chars is null 抛出此异常
     * @throws IndexOutOfBoundsException 如果index < 0或者 index >= size 抛出此异常
     */
    public static <T extends CharSequence> T checkIndex(final T chars, final int index) {
        return checkIndex(chars, index, "[Validate failed] - The validated character sequence index is invalid: {}", index);
    }

    /**
     * 检查字符串下标是否越界
     *
     * @param chars   需要检查的字符串对象
     * @param index   长度
     * @param message 异常时的消息模板
     * @param params  参数列表
     * @param <T>     集合泛型
     * @return 被检查的字符串对象
     * @throws IllegalArgumentException  如果chars is null 抛出此异常
     * @throws IndexOutOfBoundsException 如果index < 0或者 index >= size 抛出此异常
     */
    public static <T extends CharSequence> T checkIndex(final T chars, final int index, final String message,
                                                        final Object... params) {
        notNull(chars);
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(StringUtil.format(message, params));
        }
        return chars;
    }

    /**
     * 检查数组下标是否越界
     *
     * @param array 需要检查的数组对象
     * @param index 长度
     * @return 被检查的数组对象
     * @throws IllegalArgumentException  如果array is null 抛出此异常
     * @throws IndexOutOfBoundsException 如果index < 0或者 index >= size 抛出此异常
     */
    public static <T> T[] checkIndex(final T[] array, final int index) {
        return checkIndex(array, index, "[Validate failed] - The validated array index is invalid: {}", index);
    }

    /**
     * 检查数组下标是否越界
     *
     * @param array   需要检查的数组对象
     * @param index   长度
     * @param message 异常时的消息模板
     * @param params  参数列表
     * @return 被检查的数组对象
     * @throws IllegalArgumentException  如果array is null 抛出此异常
     * @throws IndexOutOfBoundsException 如果index < 0或者 index >= size 抛出此异常
     */
    public static <T> T[] checkIndex(final T[] array, final int index, final String message, final Object... params) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(StringUtil.format(message, params));
        }
        return array;
    }

    /**
     * 检查集合下标是否越界
     *
     * @param collection 需要检查的集合对象
     * @param index      长度
     * @param <T>        集合泛型
     * @return 被检查的集合对象
     * @throws IllegalArgumentException  如果collection is null 抛出此异常
     * @throws IndexOutOfBoundsException 如果index < 0或者 index >= size 抛出此异常
     */
    public static <T extends Collection<?>> T checkIndex(final T collection, final int index) {
        return checkIndex(collection, index, "[Validate failed] - The validated collection index is invalid: {}", index);
    }

    /**
     * 检查集合下标是否越界
     *
     * @param collection 需要检查的集合对象
     * @param index      长度
     * @param message    异常时的消息模板
     * @param params     参数列表
     * @param <T>        集合泛型
     * @return 被检查的集合对象
     * @throws IllegalArgumentException  如果collection is null 抛出此异常
     * @throws IndexOutOfBoundsException 如果index < 0或者 index >= size 抛出此异常
     */
    public static <T extends Collection<?>> T checkIndex(final T collection, final int index, final String message,
                                                         final Object... params) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(StringUtil.format(message, params));
        }
        return collection;
    }

    // ----------------------------------------------------------------------------------------------------------- Check index

    /**
     * 检查值是否在指定范围内
     *
     * @param value 值
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 检查后的长度值
     */
    public static int checkBetween(int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", min, max));
        }
        return value;
    }

    /**
     * 检查值是否在指定范围内
     *
     * @param value 值
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 检查后的长度值
     */
    public static long checkBetween(long value, long min, long max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", min, max));
        }
        return value;
    }

    /**
     * 检查值是否在指定范围内
     *
     * @param value 值
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 检查后的长度值
     */
    public static double checkBetween(double value, double min, double max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", min, max));
        }
        return value;
    }

    /**
     * 检查值是否在指定范围内
     *
     * @param value 值
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 检查后的长度值
     */
    public static Number checkBetween(Number value, Number min, Number max) {
        notNull(value);
        notNull(min);
        notNull(max);
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        if (valueDouble < minDouble || valueDouble > maxDouble) {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", min, max));
        }
        return value;
    }
}
