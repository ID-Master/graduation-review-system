package com.uneed.common.core.lang;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Validate Tester.
 *
 * @author huangad@coracle.com
 * @date 09/27/2019
 */
public class ValidateTest {

    @Before
    public void before() {
        //TODO: Test before goes here...
    }

    @After
    public void after() {
        //TODO: Test after goes here...
    }


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Method: isTrue(final boolean expression)
     */
    @Test
    public void testIsTrueExpression() {
        Validate.isTrue(true);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Validate failed");
        Validate.isTrue(false);


    }

    /**
     * Method: isTrue(final boolean expression, final String message, final Object... params)
     */
    @Test
    public void testIsTrueForExpressionMessageParams() {
        String a = "Validate failed,params is not true but {}";
        String paramsA = "false";
        Validate.isTrue(true, a, paramsA);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Validate failed,params is not true but false");
        Validate.isTrue(false, a, paramsA);


        int batchSize = 2;
        Validate.isTrue(batchSize < 1, "batchSize must not be less than one");

        batchSize = 0;
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("batchSize must not be less than one");
        Validate.isTrue(batchSize < 1, "batchSize must not be less than one");

    }

    /**
     * Method: isTrue(final boolean expression, final String message, final Object... params)
     */
    @Test
    public void testIsTrueForExpressionMessageParams2() {
        int batchSize = 1;
        Validate.isTrue(batchSize > 0, "batchSize must not be less than one");
    }

    /**
     * Method: notNull(final T obj)
     */
    @Test
    public void testNotNullObj() {

        Object obj = 1;
        assertEquals(obj, Validate.notNull(1));

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Validate failed");
        Object object = null;
        Validate.notNull(object);


    }

    /**
     * Method: notNull(final T obj, final String message, final Object... params)
     */
    @Test
    public void testNotNullForObjMessageParams() {
        String a = "Validate failed,params must be not {}";
        String paramsA = "null";
        Validate.notNull(true, a, paramsA);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Validate failed,params must be not null");
        Validate.notNull(null, a, paramsA);

    }

    /**
     * Method: notEmpty(final T[] array)
     */
    @Test
    public void testNotEmptyArray() {

        Long l[] = new Long[5];
        Validate.notEmpty(l);
        assertEquals(5, l.length);


        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this array must not be empty: it must contain at least 1 element");
        Object object[] = new Object[0];
        Validate.notEmpty(object);


    }

    /**
     * Method: notEmpty(final T[] array, final String message, final Object... params)
     */
    @Test
    public void testNotEmptyForArrayMessageParams() {
        String a = "this array must not be {}: it must contain at least 1 element";
        String paramsA = "empty";

        Long l[] = new Long[5];
        Validate.notEmpty(l, a, paramsA);
        assertEquals(5, l.length);


        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this array must not be empty: it must contain at least 1 element");
        Object object[] = new Object[0];
        Validate.notEmpty(object, a, paramsA);
    }

    /**
     * Method: notEmpty(final T text)
     */
    @Test
    public void testNotEmptyText() {
        String a = "this String argument must have {}; it must not be null or {}";
        String paramsA = "length";
        String paramsB = "empty";

        String str = "hello";
        String s = Validate.notEmpty(str, a, paramsA, paramsB);
        assertEquals("hello", s);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this String argument must have length; it must not be null or empty");

        String strThrow = "          ";
        Validate.notEmpty(strThrow, a, paramsA, paramsB);

    }

    /**
     * Method: notEmpty(final T text, final String message, final Object... params)
     */
    @Test
    public void testNotEmptyForTextMessageParams() {
        String str = "hello";
        String s = Validate.notEmpty(str);
        assertEquals("hello", s);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this String argument must have length; it must not be null or empty");
        String strThrow = "";
        Validate.notEmpty(strThrow);
    }

    /**
     * Method: notEmpty(final T collection)
     */
    @Test
    public void testNotEmptyCollection() {
        List<Integer> list = new ArrayList<>();
        Integer integer = 1;
        list.add(integer);
        List<Integer> list1 = Validate.notEmpty(list);
        assertFalse(list1.isEmpty());
        list.remove(0);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this collection must not be empty: it must contain at least 1 element");
        Validate.notEmpty(list);
    }

    /**
     * Method:notEmpty(final T collection, final String message, final Object... params)
     */
    @Test
    public void testNotEmptyForCollectionMessageParams() {
        String a = "this collection must not be {}: it must contain at least 1 {}";
        String paramsA = "empty";
        String paramsB = "element";

        List<Integer> list = new ArrayList<>();
        Integer integer = 1;
        list.add(integer);
        List<Integer> list1 = Validate.notEmpty(list, a, paramsA);
        assertFalse(list1.isEmpty());
        list.remove(0);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this collection must not be empty: it must contain at least 1 element");
        Validate.notEmpty(list, a, paramsA, paramsB);
    }

    /**
     * Method: notEmpty(T map)
     */
    @Test
    public void testNotEmptyMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("test", "test");
        Map<String, Object> map1 = Validate.notEmpty(map);
        assertFalse(map1.isEmpty());
        map.remove("test");

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this map must not be empty; it must contain at least one entry");
        Validate.notEmpty(map);
    }

    /**
     * Method: notEmpty(T map, String message, Object... params)
     */
    @Test
    public void testNotEmptyForMapMessageParams() {
        String a = "this map must not be {}: it must contain at least 1 {}";
        String paramsA = "empty";
        String paramsB = "element";

        Map<String, Object> map = new HashMap<>();
        map.put("test", "test");
        Map<String, Object> map1 = Validate.notEmpty(map, a, paramsA);
        assertFalse(map1.isEmpty());
        map.remove("test");

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this map must not be empty: it must contain at least 1 element");
        Validate.notEmpty(map, a, paramsA, paramsB);
    }

    /**
     * 断言迭代器非空
     * Method: notEmpty(T iterable)
     */
    @Test
    public void testNotEmptyIterable() {
        List<Integer> list = new ArrayList<>();

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this iterable must not be empty; it must contain at least one entry");
        Validate.notEmpty((Iterable<Integer>) list);

    }

    /**
     * 断言迭代器非空
     * Method: notEmpty(T iterable, String message, Object... params)
     */
    @Test
    public void testNotEmptyForIterableMessageParams() {
        String a = "this iterable must not be {}; it must contain at least one {}";
        String paramsA = "empty";
        String paramsB = "entry";

        List<Integer> list = new ArrayList<>();


        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this iterable must not be empty; it must contain at least one entry");
        Validate.notEmpty((Iterable<Integer>) list, a, paramsA, paramsB);

    }

    /**
     * 测试断言数组不包含null元素
     * Method: noNullElements(final T[] array)
     */
    @Test
    public void testNoNullElementsArray() {

        Integer arr[] = new Integer[]{1, 2};
        Integer[] integers = Validate.noNullElements(arr);
        for (Integer integer : integers) {
            assertNotNull(integer);

        }

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this array must not contain any null elements");
        Integer integer[] = new Integer[]{null, 5};
        Validate.noNullElements(integer);
    }

    /**
     * 测试断言数组不包含null元素
     * Method: noNullElements(final T[] array, final String message, final Object... params)
     */
    @Test
    public void testNoNullElementsForArrayMessageParams() {
        String a = "this array must not contain any {} {}";
        String paramsA = "null";
        String paramsB = "elements";

        Integer arr[] = new Integer[]{1, 2};
        Integer[] integers = Validate.noNullElements(arr, a, paramsA, paramsB);
        for (Integer integer : integers) {
            assertNotNull(integer);
        }

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this array must not contain any null elements");
        Integer integers1[] = new Integer[]{null, 6};
        Validate.noNullElements(integers1, a, paramsA, paramsB);
    }

    /**
     * 测试断言集合是否包含null元素
     * Method: noNullElements(final T iterable)
     */
    @Test
    public void testNoNullElementsIterable() {


        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Iterable<Integer> integers = Validate.noNullElements((Iterable<Integer>) list);
        for (Integer integer : integers) {
            assertNotNull(integer);
        }

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this collection must not contain any null elements");
        list.add(null);
        Validate.noNullElements((Iterable<Integer>) list);
    }

    /**
     * 测试断言集合是否包含null元素
     * Method: noNullElements(final T iterable, final String message, final Object... params)
     */
    @Test
    public void testNoNullElementsForIterableMessageParams() {
        String a = "this collection must not contain any {} {}";
        String paramsA = "null";
        String paramsB = "elements";

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Iterable<Integer> integers = Validate.noNullElements((Iterable<Integer>) list, a, paramsA, paramsB);
        for (Integer integer : integers) {
            assertNotNull(integer);
        }


        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this collection must not contain any null elements");
        list.add(null);
        Validate.noNullElements((Iterable<Integer>) list, a, paramsA, paramsB);
    }

    /**
     * 测试断言给定对象是否是给定类的实例
     * Method: isInstanceOf(Class<?> type, T obj)
     */
    @Test
    public void testIsInstanceOfForTypeObj() {
        String s = "5";
        Integer integer = 5;
        assertEquals(s, Validate.isInstanceOf(String.class, s));

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Object [5] is not instanceof [class java.lang.String]");
        Validate.isInstanceOf(String.class, integer);


    }

    /**
     * 测试断言给定对象是否是给定类的实例
     * Method: isInstanceOf(Class<?> type, T obj, String message, Object... params)
     */
    @Test
    public void testIsInstanceOfForTypeObjMessageParams() {
        String a = "Object [5] is not instanceof [class java.lang.String]";
        String paramsA = "[5]";
        String paramsB = "String";
        assertEquals(a, Validate.isInstanceOf(String.class, a));

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Object [5] is not instanceof [class java.lang.String]");
        Integer integer = 5;
        Validate.isInstanceOf(String.class, integer, a, paramsA, paramsB);
    }

    /**
     * 测试断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.（父类、子类，接口、实现类）
     * Method: isAssignable(Class<?> superType, Class<?> subType)
     */
    @Test
    public void testIsAssignableForSuperTypeSubType() {
        Validate.isAssignable(List.class, ArrayList.class);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("[class java.lang.String] is not assignable to [interface java.util.List]");
        Validate.isAssignable(List.class, String.class);
    }

    /**
     * 测试断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.（父类、子类，接口、实现类）
     * Method: isAssignable(Class<?> superType, Class<?> subType, String message, Object... params)
     */
    @Test
    public void testIsAssignableForSuperTypeSubTypeMessageParams() {
        String a = "[class java.lang.{}] is not assignable to [interface java.util.{}]";
        String paramsA = "String";
        String paramsB = "List";
        Validate.isAssignable(List.class, ArrayList.class, a, paramsA, paramsB);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("[class java.lang.String] is not assignable to [interface java.util.List]");
        Validate.isAssignable(List.class, String.class, a, paramsA, paramsB);
    }

    /**
     * 测试检查boolean表达式
     * Method: state(boolean expression)
     */
    @Test
    public void testStateExpression() {
        Validate.state(true);

        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("[Validate failed] - this state invariant must be true");
        Validate.state(false);

    }

    /**
     * 检查boolean表达式
     * Method: state(boolean expression, String message, Object... params)
     */
    @Test
    public void testStateForExpressionMessageParams() {

        String a = "[Validate failed] - this {} invariant must be {}";
        String paramsA = "state";
        String paramsB = "true";
        Validate.state(true, a, paramsA, paramsB);

        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("[Validate failed] - this state invariant must be true");
        Validate.state(false, a, paramsA, paramsB);
    }

    /**
     * 检查字符串下标是否越界
     * 如果index < 0或者 index >= size
     * Method: checkIndex(final T chars, final int index)
     */
    @Test
    public void testCheckIndexForCharsIndex() {
        String s = " ";
        Validate.checkIndex(s, 0);
        s = "abc";
        String s1 = Validate.checkIndex(s, 2);
        assertEquals("abc", s1);

        expectedEx.expect(IndexOutOfBoundsException.class);
        expectedEx.expectMessage("The validated character sequence index is invalid: 3");
        Validate.checkIndex(s, 3);


    }

    /**
     * 检查字符串下标是否越界
     * 如果chars is null
     * Method: checkIndex(final T chars, final int index)
     */
    @Test
    public void testCheckNullIndexForCharsIndex() {

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("his argument is required; it must not be null");
        String s = null;
        Validate.checkIndex(s, 5);

    }

    /**
     * 检查字符串下标是否越界
     * 如果index < 0或者 index >= size
     * Method: checkIndex(final T chars, final int index, final String message, final Object... params)
     */
    @Test
    public void testCheckIndexForCharsIndexMessageParams() {
        String a = "The validated character sequence {} is invalid: {}";
        String paramsA = "index";
        String paramsB = "3";

        String s = " ";
        Validate.checkIndex(s, 0, a, paramsA, paramsB);
        s = "abc";
        String s1 = Validate.checkIndex(s, 2, a, paramsA, paramsB);
        assertEquals("abc", s1);

        expectedEx.expect(IndexOutOfBoundsException.class);
        expectedEx.expectMessage("The validated character sequence index is invalid: 3");
        Validate.checkIndex(s, 3, a, paramsA, paramsB);
    }

    /**
     * 检查字符串下标是否越界
     * 如果chars is null
     * Method: checkIndex(final T chars, final int index, final String message, final Object... params)
     */
    @Test
    public void testCheckNullIndexForCharsIndexMessageParams() {
        String a = "his argument is {}; it must not be {}";
        String paramsA = "required";
        String paramsB = "null";

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("his argument is required; it must not be null");
        String s = null;
        Validate.checkIndex(s, 5, a, paramsA, paramsB);
    }

    /**
     * 检查数组下标是否越界
     * 如果index < 0或者 index >= size
     * Method: checkIndex(final T[] array, final int index)
     */
    @Test
    public void testCheckIndexForArrayIndex() {
        Integer arr[] = new Integer[4];
        Integer[] integers = Validate.checkIndex(arr, 3);
        assertSame(arr, integers);

        expectedEx.expect(IndexOutOfBoundsException.class);
        expectedEx.expectMessage("The validated array index is invalid: 8");
        Validate.checkIndex(arr, 8);

    }

    /**
     * 检查数组下标是否越界
     * 如果array is null
     * Method: checkIndex(final T[] array, final int index)
     */
    @Test
    public void testCheckIndexForArrayNullIndex() {

        Integer arr[] = null;
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this argument is required; it must not be null");
        Validate.checkIndex(arr, 4);


    }

    /**
     * 检查数组下标是否越界
     * 如果index < 0或者 index >= size
     * Method: checkIndex(final T[] array, final int index, final String message, final Object... params)
     */
    @Test
    public void testCheckIndexForArrayNullIndexMessageParams() {
        String a = "The validated {} {} is invalid: 8";
        String paramsA = "array";
        String paramsB = "index";

        Integer arr[] = new Integer[4];
        Integer[] integers = Validate.checkIndex(arr, 3, a, paramsA, paramsB);
        assertSame(arr, integers);

        expectedEx.expect(IndexOutOfBoundsException.class);
        expectedEx.expectMessage("The validated array index is invalid: 8");
        Validate.checkIndex(arr, 8, a, paramsA, paramsB);
    }

    /**
     * 检查数组下标是否越界
     * 如果array is null
     * Method: checkIndex(final T[] array, final int index, final String message, final Object... params)
     */
    @Test
    public void testCheckIndexForArrayIndexMessageParams() {
        String a = "this argument is {}; it must{} not be {}";
        String paramsA = "required";
        String paramsB = "null";

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this argument is required; it must not be null");
        Integer arr[] = null;
        Validate.checkIndex(arr, 4, a, paramsA, paramsB);
    }

    /**
     * 检查集合下标是否越界
     * 如果index < 0或者 index >= size
     * Method: checkIndex(final T collection, final int index)
     */
    @Test
    public void testCheckIndexForCollectionIndex() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        List<Integer> list1 = Validate.checkIndex(list, 1);
        assertSame(list, list1);

        expectedEx.expect(IndexOutOfBoundsException.class);
        expectedEx.expectMessage("The validated collection index is invalid: 8");
        Validate.checkIndex(list, 8);
    }

    /**
     * 检查集合下标是否越界
     * 如果collection is null
     * Method: checkIndex(final T collection, final int index)
     */
    @Test
    public void testCheckIndexForCollectionNullIndex() {
        List<Integer> list;

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this argument is required; it must not be null");
        list = null;
        Validate.checkIndex(list, 6);
    }

    /**
     * 检查集合下标是否越界
     * 如果index < 0或者 index >= size
     * Method: checkIndex(final T collection, final int index, final String message, final Object... params)
     */
    @Test
    public void testCheckIndexForCollectionIndexMessageParams() {
        String a = "The validated {} index is invalid: {}";
        String paramsA = "collection";
        String paramsB = "8";

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        List<Integer> list1 = Validate.checkIndex(list, 1);
        assertSame(list, list1);

        expectedEx.expect(IndexOutOfBoundsException.class);
        expectedEx.expectMessage("The validated collection index is invalid: 8");
        Validate.checkIndex(list, 8, a, paramsA, paramsB);
    }

    /**
     * 检查集合下标是否越界
     * 如果collection is null
     * Method: checkIndex(final T collection, final int index, final String message, final Object... params)
     */
    @Test
    public void testCheckIndexForCollectionNullIndexMessageParams() {
        String a = "this argument is {}; it must not be {}";
        String paramsA = "required";
        String paramsB = "null";
        List<Integer> list;

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("this argument is required; it must not be null");
        list = null;
        Validate.checkIndex(list, 6, a, paramsA, paramsB);
    }

    /**
     * 检查值是否在指定范围内int
     * Method: checkBetween(int value, int min, int max)
     */
    @Test
    public void testCheckBetweenForValueIntMinMax() {
        //int
        int value = 55;
        int max = 60;
        int min = 40;
        int i = Validate.checkBetween(value, min, max);
        assertEquals(55, i);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Length must be between 40 and 60");
        value = 88;
        Validate.checkBetween(value, min, max);
    }

    /**
     * 检查值是否在指定范围内long
     * Method: checkBetween(int value, int min, int max)
     */
    @Test
    public void testCheckBetweenForValueLongMinMax() {
        //long
        long longValue = 55;
        long longMin = 40;
        long longMax = 60;

        long longI = Validate.checkBetween(longValue, longMin, longMax);
        assertEquals(55, longI);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Length must be between 40 and 60");
        longValue = 88;
        Validate.checkBetween(longValue, longMin, longMax);
    }

    /**
     * 检查值是否在指定范围内double
     * Method: checkBetween(int value, int min, int max)
     */
    @Test
    public void testCheckBetweenForValueDoubleMinMax() {
        //double
        double doubleValue = 55d;
        double doubleMin = 40d;
        double doubleMax = 60d;

        double doubleI = Validate.checkBetween(doubleValue, doubleMin, doubleMax);
        assertEquals(55d, doubleI, 0.01d);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Length must be between 40.0 and 60.0");
        doubleValue = 88d;
        Validate.checkBetween(doubleValue, doubleMin, doubleMax);

    }

    /**
     * 检查值是否在指定范围内Number
     * Method: checkBetween(int value, int min, int max)
     */
    @Test
    public void testCheckBetweenForValueNumberMinMax() {
        //double
        Number numberValue = 55;
        Number numberMin = 40;
        Number numberMax = 60;

        Number numberI = Validate.checkBetween(numberValue, numberMin, numberMax);
        assertEquals(55, numberI);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Length must be between 40 and 60");
        numberValue = 88;
        Validate.checkBetween(numberValue, numberMin, numberMax);

    }

}
