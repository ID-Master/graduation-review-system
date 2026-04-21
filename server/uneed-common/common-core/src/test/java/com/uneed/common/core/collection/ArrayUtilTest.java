package com.uneed.common.core.collection;

import com.uneed.common.core.Editor;
import com.uneed.common.core.Filter;
import com.uneed.common.core.convert.Convert;
import com.uneed.common.core.entity.Person;
import com.uneed.common.core.entity.User;
import com.uneed.common.core.exception.UtilException;
import com.uneed.common.core.lang.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ArrayUtil Tester.
 *
 * @author huangad@coracle.com
 * @date 10/17/2019
 */
public class ArrayUtilTest {

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
     * 判断数组是否为null或是数组长度为0
     * Method: isEmpty(final T[] array)
     */
    @Test
    public void testIsEmpty() {
        Integer[] a = new Integer[0];
        Validate.isTrue(ArrayUtil.isEmpty(a));
        String[] b = new String[]{"1"};
        Validate.isTrue(!ArrayUtil.isEmpty(b));
        int[][] c = new int[][]{{1, 3}, {2}};
        Validate.isTrue(!ArrayUtil.isEmpty(c));


    }

    /**
     * 判断数组元素不少于1个
     * Method: isNotEmpty(final T[] array)
     */
    @Test
    public void testIsNotEmpty() {
        Integer[] a = new Integer[0];
        Validate.isTrue(!ArrayUtil.isNotEmpty(a));
        String[] b = new String[]{"1"};
        Validate.isTrue(ArrayUtil.isNotEmpty(b));
        int[][] c = new int[][]{{1, 3}, {2}};
        Validate.isTrue(ArrayUtil.isNotEmpty(c));
    }


    /**
     * 对象是否为数组对象
     * Method: isArray(Object obj)
     */
    @Test
    public void testIsArray() {
        Object object = new Object();
        Validate.isTrue(!ArrayUtil.isArray(object));
        Integer[] a = new Integer[0];
        Validate.isTrue(ArrayUtil.isArray(a));
        int[][] c = new int[][]{{1, 3}, {2}};
        Validate.isTrue(ArrayUtil.isArray(c));


    }


    /**
     * 数组的长度
     * Method: length(Object array)
     */
    @Test
    public void testLength() {
        int[] a = {};
        Integer[] b = null;
        String[] c = new String[2];
        Double[] d = new Double[]{1d, 2d};
        assertEquals(0, ArrayUtil.length(a));
        assertEquals(0, ArrayUtil.length(b));
        assertEquals(2, ArrayUtil.length(c));
        assertEquals(2, ArrayUtil.length(d));

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Argument is not an array");
        Object object = new Object();
        ArrayUtil.length(object);

    }

    /**
     * 数组或集合转String
     * Method: toString(Object obj)
     */
    @Test
    public void testToString() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(333);
        assertEquals("[1, 333]", ArrayUtil.toString(list));
    }

    /**
     * 根据数组元素类型，获取数组的类型
     * Method: getArrayType(Class<?> componentType)
     */
    @Test
    public void testGetArrayType() {
        assertEquals("Object[]", ArrayUtil.getArrayType(Object.class).getSimpleName());
        assertEquals("String[]", ArrayUtil.getArrayType(String.class).getSimpleName());
    }

    /**
     * 获取数组对象的元素类型
     * Method: getComponentType(Object array)
     */
    @Test
    public void testGetComponentTypeArray() {
        Object object[] = new Object[]{1, 2};
        Integer integers[] = new Integer[5];
        assertEquals("Object", ArrayUtil.getComponentType(object).getSimpleName());
        assertEquals("Integer", ArrayUtil.getComponentType(integers).getSimpleName());
    }

    /**
     * 获取数组对象的元素类型
     * Method: getComponentType(Class<?> arrayClass)
     */
    @Test
    public void testGetComponentTypeArrayClass() {
        Object object[] = new Object[5];
        Integer integers[] = new Integer[5];
        assertEquals("Object", ArrayUtil.getComponentType(object.getClass()).getSimpleName());
        assertEquals("Integer", ArrayUtil.getComponentType(integers.getClass()).getSimpleName());
    }

    /**
     * 反转数组，会变更原数组
     * Method: reverse(final T[] array, final int startIndexInclusive, final int endIndexExclusive)
     */
    @Test
    public void testReverseForArrayStartIndexInclusiveEndIndexExclusive() {
        //long
        long longArr[] = {1, 2, 3, 4, 5, 6};
        long[] reverseLong = ArrayUtil.reverse(longArr, 0, 3);
        assertEquals("[3, 2, 1, 4, 5, 6]", ArrayUtil.toString(reverseLong));
        //int
        int arr[] = {1, 2, 3, 4, 5, 6};
        int[] reverseInt = ArrayUtil.reverse(arr, 0, 2);
        assertEquals("[2, 1, 3, 4, 5, 6]", ArrayUtil.toString(reverseInt));
        //short
        short shortArr[] = {1, 2, 3, 4, 5, 6};
        short[] reverseShort = ArrayUtil.reverse(shortArr, 2, 5);
        assertEquals("[1, 2, 5, 4, 3, 6]", ArrayUtil.toString(reverseShort));
        //char
        char charArr[] = {'a', 'b', 'c', 'd', 'e'};
        char[] reverseChar = ArrayUtil.reverse(charArr, 2, 4);
        assertEquals("[a, b, d, c, e]", ArrayUtil.toString(reverseChar));
        //byte
        byte byteArr[] = {1, 2, 3, 4, 5, 6};
        byte[] reverseByte = ArrayUtil.reverse(byteArr, 2, 5);
        assertEquals("[1, 2, 5, 4, 3, 6]", ArrayUtil.toString(reverseByte));
        //double
        double doubleArr[] = {1, 2, 3, 4, 5, 6};
        double[] reverseDouble = ArrayUtil.reverse(doubleArr, 3, 5);
        assertEquals("[1.0, 2.0, 3.0, 5.0, 4.0, 6.0]", ArrayUtil.toString(reverseDouble));
        //float
        float floatArr[] = {1, 2, 3, 4, 5, 6};
        float[] reverseFloat = ArrayUtil.reverse(floatArr, 1, 4);
        assertEquals("[1.0, 4.0, 3.0, 2.0, 5.0, 6.0]", ArrayUtil.toString(reverseFloat));
        //boolean
        boolean booleanArr[] = {true, false, false};
        boolean[] reverseBoolean = ArrayUtil.reverse(booleanArr, 0, 2);
        assertEquals("[false, true, false]", ArrayUtil.toString(reverseBoolean));
        //T
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setAge(1);
        user2.setAge(2);
        user3.setAge(3);
        User userArr[] = {user1, user2, user3};
        User[] reverseUser = ArrayUtil.reverse(userArr, 0, 2);
        assertEquals("[User(name=null, age=2, studentId=null), User(name=null, age=1, studentId=null), User(name=null, age=3, studentId=null)]", ArrayUtil
                .toString(reverseUser));


    }

    /**
     * 反转数组，会变更原数组
     * Method: reverse(final T[] array)
     */
    @Test
    public void testReverseArray() {
        //long
        long longArr[] = {1, 2, 3};
        long[] reverseLong = ArrayUtil.reverse(longArr);
        assertEquals("[3, 2, 1]", ArrayUtil.toString(reverseLong));
        //int
        int arr[] = {1, 2, 3};
        int[] reverseInt = ArrayUtil.reverse(arr);
        assertEquals("[3, 2, 1]", ArrayUtil.toString(reverseInt));
        //short
        short shortArr[] = {1, 2, 3};
        short[] reverseShort = ArrayUtil.reverse(shortArr);
        assertEquals("[3, 2, 1]", ArrayUtil.toString(reverseShort));
        //char
        char charArr[] = {'a', 'b', 'c'};
        char[] reverseChar = ArrayUtil.reverse(charArr);
        assertEquals("[c, b, a]", ArrayUtil.toString(reverseChar));
        //byte
        byte byteArr[] = {125, 126, 127};
        byte[] reverseByte = ArrayUtil.reverse(byteArr);
        assertEquals("[127, 126, 125]", ArrayUtil.toString(reverseByte));
        //double
        double doubleArr[] = {125d, 126d, 127d};
        double[] reverseDouble = ArrayUtil.reverse(doubleArr);
        assertEquals("[127.0, 126.0, 125.0]", ArrayUtil.toString(reverseDouble));
        //float
        float floatArr[] = {125f, 126f, 127f};
        float[] reverseFloat = ArrayUtil.reverse(floatArr);
        assertEquals("[127.0, 126.0, 125.0]", ArrayUtil.toString(reverseFloat));
        //boolean
        boolean booleanArr[] = {true, false, false};
        boolean[] reverseBoolean = ArrayUtil.reverse(booleanArr);
        assertEquals("[false, false, true]", ArrayUtil.toString(reverseBoolean));
        //T
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setAge(1);
        user2.setAge(2);
        user3.setAge(3);
        User userArr[] = {user1, user2, user3};
        User[] reverseUser = ArrayUtil.reverse(userArr);
        assertEquals("[User(name=null, age=3, studentId=null), User(name=null, age=2, studentId=null), User(name=null, age=1, studentId=null)]", ArrayUtil
                .toString(reverseUser));


    }

    /**
     * 取最小值
     * Method: min(T[] numberArray)
     */
    @Test
    public void testMinNumberArray() {
        //T
        Person person1 = new Person(23, "aaa");
        Person person2 = new Person(22, "bbb");
        Person person3 = new Person(33, "ccc");

        Person persons[] = new Person[]{person1, person2, person3};
        assertEquals(person2, ArrayUtil.min(persons));

        //long
        long longs[] = new long[]{5L, 2L, 3L};
        assertEquals(2L, ArrayUtil.min(longs));
        //int
        int ints[] = new int[]{4, 5, 8, 9};
        assertEquals(4, ArrayUtil.min(ints));
        //short
        short shorts[] = new short[]{4, 5, 8, 9};
        assertEquals(4, ArrayUtil.min(shorts));
        //char
        char chars[] = new char[]{'a', 'b', 'c', 'd'};
        assertEquals('a', ArrayUtil.min(chars));
        //byte
        byte bytes[] = new byte[]{4, 5, 8, 9};
        assertEquals(4, ArrayUtil.min(bytes));
        //double
        double doubles[] = new double[]{4d, 5d, 8d, 9d};
        assertEquals(4d, ArrayUtil.min(doubles), 0.01d);
        //float
        float floats[] = new float[]{4f, 5f, 8f, 9f};
        assertEquals(4f, ArrayUtil.min(floats), 0.01f);
        //empty
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Number array must not empty");
        int ints1[] = new int[0];
        ArrayUtil.min(ints1);


    }

    /**
     * Method: max(T[] numberArray)
     */
    @Test
    public void testMaxNumberArray() {
        //T
        Person person1 = new Person(23, "aaa");
        Person person2 = new Person(22, "bbb");
        Person person3 = new Person(33, "ccc");

        Person persons[] = new Person[]{person1, person2, person3};
        assertEquals(person3, ArrayUtil.max(persons));

        //long
        long longs[] = new long[]{5L, 2L, 3L};
        assertEquals(5L, ArrayUtil.max(longs));
        //int
        int ints[] = new int[]{4, 5, 8, 9};
        assertEquals(9, ArrayUtil.max(ints));
        //short
        short shorts[] = new short[]{4, 5, 8, 9};
        assertEquals(9, ArrayUtil.max(shorts));
        //char
        char chars[] = new char[]{'a', 'b', 'c', 'd'};
        assertEquals('d', ArrayUtil.max(chars));
        //byte
        byte bytes[] = new byte[]{4, 5, 8, 9};
        assertEquals(9, ArrayUtil.max(bytes));
        //double
        double doubles[] = new double[]{4d, 5d, 8d, 9d};
        assertEquals(9d, ArrayUtil.max(doubles), 0.01d);
        //float
        float floats[] = new float[]{4f, 5f, 8f, 9f};
        assertEquals(9f, ArrayUtil.max(floats), 0.01f);
        //empty
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Number array must not empty");
        int ints1[] = new int[0];
        ArrayUtil.min(ints1);
        System.out.println(ArrayUtil.max(ints1));
    }

    /**
     * 交换数组中两个位置的值
     * Method: swap(int[] array, int index1, int index2)
     */
    @Test
    public void testSwapForArrayIndex1Index2() {

        //int
        int ints[] = new int[]{4, 5, 8, 9};
        int ints1[] = new int[]{4, 5, 9, 8};
        assertTrue(Arrays.equals(ints1, ArrayUtil.swap(ints, 2, 3)));
        //long
        long longs[] = new long[]{5L, 2L, 3L};
        long longs1[] = new long[]{3L, 2L, 5L};
        assertTrue(Arrays.equals(longs1, ArrayUtil.swap(longs, 0, 2)));
        //double
        double doubles[] = new double[]{4d, 5d, 8d, 9d};
        double doubles1[] = new double[]{8d, 5d, 4d, 9d};
        assertTrue(Arrays.equals(doubles1, ArrayUtil.swap(doubles, 0, 2)));
        //float
        float floats[] = new float[]{4f, 5f, 8f, 9f};
        float floats1[] = new float[]{9f, 5f, 8f, 4f};
        assertTrue(Arrays.equals(floats1, ArrayUtil.swap(floats, 0, 3)));
        //boolean
        boolean booleanArr[] = new boolean[]{true, false, false};
        boolean booleanArr1[] = new boolean[]{false, true, false};
        assertTrue(Arrays.equals(booleanArr1, ArrayUtil.swap(booleanArr, 0, 1)));
        //byte
        byte bytes[] = new byte[]{4, 5, 8, 9};
        byte bytes1[] = new byte[]{9, 5, 8, 4};
        assertTrue(Arrays.equals(bytes1, ArrayUtil.swap(bytes, 0, 3)));
        //char
        char chars[] = new char[]{'a', 'b', 'c', 'd'};
        char chars1[] = new char[]{'a', 'd', 'c', 'b'};
        assertTrue(Arrays.equals(chars1, ArrayUtil.swap(chars, 1, 3)));
        //short
        short shorts[] = new short[]{4, 5, 8, 9};
        short shorts1[] = new short[]{4, 9, 8, 5};
        assertTrue(Arrays.equals(shorts1, ArrayUtil.swap(shorts, 1, 3)));
        //T
        Person person1 = new Person(23, "aaa");
        Person person2 = new Person(22, "bbb");
        Person person3 = new Person(33, "ccc");

        Person persons[] = new Person[]{person1, person2, person3};
        Person persons1[] = new Person[]{person1, person3, person2};
        assertTrue(Arrays.equals(persons1, ArrayUtil.swap(persons, 1, 2)));
        //Object
        Object swap = ArrayUtil.swap((Object) new Integer[]{1, 2, 3}, 1, 2);
        Integer[] integers2 = Convert.toIntegerArray(swap);
        assertEquals("[1, 3, 2]", Arrays.toString(integers2));
        //empty
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Number array must not empty");
        int ints3[] = new int[0];
        ArrayUtil.swap(ints3, 2, 3);

    }

    /**
     * 计算null或空元素对象的个数，通过 ObjectUtil#isEmpty(Object)判断元素
     * Method: emptyCount(Object... args)
     */
    @Test
    public void testEmptyCount() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();
        int i = ArrayUtil.emptyCount(object1, null, object2, object3);
        assertEquals(1, i);
    }

    /**
     * 是否存在null或空元素对象
     * Method: hasEmpty(Object... args)
     */
    @Test
    public void testHasEmpty() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();
        assertTrue(ArrayUtil.hasEmpty(object1, null, object2));
        assertFalse(ArrayUtil.hasEmpty(object3));
    }

    /**
     * 获取数组对象中指定index的值，支持负数，例如-1表示倒数第一个值
     * Method: get(Object array, int index)
     */
    @Test
    public void testGet() {
        int value = ArrayUtil.get(new int[]{4, 5, 8, 9}, -1);
        int value1 = ArrayUtil.get(new Integer[]{4, 5, 8, 9}, 1);
        assertEquals(9, value);
        assertEquals(5, value1);
    }

    /**
     * 获取数组中指定多个下标元素值，组成新数组
     * Method: getAny(Object array, int... indexes)
     */
    @Test
    public void testGetAny() {
        assertEquals("[1, 2, 3]", Arrays.toString(ArrayUtil.getAny(new Integer[]{1, 2, 3, 4}, 0, 1, 2)));
        assertEquals("[false, true]", Arrays.toString(ArrayUtil.getAny(new Boolean[]{false, true, false}, 0, 1)));
    }

    /**
     * 是否都为空
     * Method: isAllEmpty(Object... args)
     */
    @Test
    public void testIsAllEmpty() {
        Integer a = 4;
        Object object = new Object();
        assertFalse(ArrayUtil.isAllEmpty(a, null, object));
        a = null;
        assertTrue(ArrayUtil.isAllEmpty(a));

    }

    /**
     * 是否都不为空
     * Method: isAllNotEmpty(Object... args)
     */
    @Test
    public void testIsAllNotEmpty() {
        Integer a = 4;
        Integer b = null;
        Object object = new Object();

        assertTrue(ArrayUtil.isAllNotEmpty(a, object));
        assertFalse(ArrayUtil.isAllNotEmpty(b));
    }

    /**
     * 去重数组中的元素，去重后生成新的数组，原数组不变
     * Method: distinct(T[] array)
     */
    @Test
    public void testDistinct() {
        Integer[] arr = new Integer[]{1, 2, 3, 3, 2, 5, 6};
        Integer[] distinct = ArrayUtil.distinct(arr);
        assertEquals("[1, 2, 3, 5, 6]", Arrays.toString(distinct));
    }

    /**
     * 转byte数组
     * Method: toArray(ByteBuffer bytebuffer)
     */
    @Test
    public void testToArrayBytebuffer() {
        ByteBuffer directBuffer = ByteBuffer.wrap(new byte[]{12, 3});
        byte[] bytes = ArrayUtil.toArray(directBuffer);
        assertEquals("[12, 3]", Arrays.toString(bytes));

    }

    /**
     * 将集合转为数组
     * Method: toArray(Iterator<T> iterator, Class<T> componentType)
     */
    @Test
    public void testToArrayForIteratorComponentType() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        Integer[] integers = ArrayUtil.toArray((Iterable<Integer>) list, Integer.class);
        String s = ArrayUtil.toString(integers);
        assertEquals("[1, 3]", s);

    }

    /**
     * 将集合转为数组
     * Method: toArray(Iterable<T> iterable, Class<T> componentType)
     */
    @Test
    public void testToArrayForIterableComponentType() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();
        Integer[] integers = ArrayUtil.toArray(iterator, Integer.class);
        String s = ArrayUtil.toString(integers);
        assertEquals("[1, 3]", s);
    }

    /**
     * 将集合转为数组
     * Method: toArray(Collection<T> collection, Class<T> componentType)
     */
    @Test
    public void testToArrayForCollectionComponentType() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        Integer[] integers = ArrayUtil.toArray(list, Integer.class);
        String s = ArrayUtil.toString(integers);
        assertEquals("[1, 3]", s);
    }

    /**
     * 将新元素添加到已有数组中
     * 添加新元素会生成一个新的数组，不影响原数组
     * Method: append(T[] buffer, T... newElements)
     */
    @Test
    public void testAppendForBufferNewElements() {
        Integer[] integers = new Integer[]{1, 3, 5, 7};
        Integer integer1 = 9;
        Integer integer2 = 11;
        Integer[] append = ArrayUtil.append(integers, integer1, integer2);
        String s = ArrayUtil.toString(append);
        assertEquals("[1, 3, 5, 7, 9, 11]", s);

    }

    /**
     * 将新元素添加到已有数组中
     * 添加新元素会生成一个新的数组，不影响原数组
     * Method: append(Object array, T... newElements)
     */
    @Test
    public void testAppendForArrayNewElements() {
        Integer[] integers = new Integer[]{1, 3, 5, 7};
        Integer integer1 = 9;
        Integer integer2 = 11;
        Object append = ArrayUtil.append((Object) integers, integer1, integer2);
        String s = ArrayUtil.toString(append);
        assertEquals("[1, 3, 5, 7, 9, 11]", s);

    }

    /**
     * 将新元素插入到到已有数组中的某个位置
     * Method: insert(T[] buffer, int index, T... newElements)
     */
    @Test
    public void testInsertForBufferIndexNewElements() {
        Integer[] integers = new Integer[]{1, 3, 5, 7};
        Integer integer1 = 9;
        Integer integer2 = 11;
        Integer[] insert = ArrayUtil.insert(integers, 4, integer1, integer2);
        String s = ArrayUtil.toString(insert);
        assertEquals("[1, 3, 5, 7, 9, 11]", s);
    }

    /**
     * 将新元素插入到到已有数组中的某个位置
     * Method: insert(Object array, int index, T... newElements)
     */
    @Test
    public void testInsertForArrayIndexNewElements() {
        Integer[] integers = new Integer[]{1, 3, 5, 7};
        Integer integer1 = 9;
        Integer integer2 = 11;
        Object insert = ArrayUtil.insert((Object) integers, 4, integer1, integer2);
        String s = ArrayUtil.toString(insert);
        assertEquals("[1, 3, 5, 7, 9, 11]", s);
    }

    /**
     * 过滤
     * Method: filter(T[] array, Editor<T> editor)
     */
    @Test
    public void testFilterForArrayEditor() {
        Double[] doubles = new Double[]{2d, 4d, 8d, 10d};
        Editor<Double> editor = v -> {
            if (v > 5d) {
                return v;
            } else {
                return null;
            }

        };
        Double[] filter = ArrayUtil.filter(doubles, editor);
        String s = ArrayUtil.toString(filter);
        assertEquals("[8.0, 10.0]", s);
    }


    /**
     * 过滤
     * Method: filter(T[] array, Filter<T> filter)
     */
    @Test
    public void testFilterForArrayFilter() {
        Float[] floats = new Float[]{4f, 5f, 7f, 10f};
        Filter<Float> floatFilter = aFloat -> aFloat < 10f;
        Float[] filter = ArrayUtil.filter(floats, floatFilter);
        String s = ArrayUtil.toString(filter);
        assertEquals("[4.0, 5.0, 7.0]", s);
    }

    /**
     * 获取数组元素
     * Method: getElement(Object array, int index)
     */
    @Test
    public void testGetElement() {
        Float[] floats = new Float[]{4f, 5f, 7f, 10f};
        Object element = ArrayUtil.getElement(floats, 1);
        assertEquals(5f, element);

    }

    /**
     * 新建一个空数组
     * Method: newArray(Class<?> componentType, int newSize)
     */
    @Test
    public void testNewArrayForComponentTypeNewSize() {
        Integer[] integers = ArrayUtil.newArray(Integer.class, 5);
        ArrayUtil.isArray(integers);
        ArrayUtil.isEmpty(integers);
    }

    /**
     * 新建一个空数组
     * Method: newArray(int newSize)
     */
    @Test
    public void testNewArrayNewSize() {
        Object[] objects = ArrayUtil.newArray(5);
        ArrayUtil.isArray(objects);
        ArrayUtil.isEmpty(objects);
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     * Method: join(T[] array, CharSequence conjunction)
     */
    @Test
    public void testJoinForArrayConjunction() {
        CharSequence charSequence = ".";
        //T
        Object[] objects = new Object[]{1, 2, 5};
        assertEquals("1.2.5", ArrayUtil.join(objects, charSequence));
        //long
        long[] longs = new long[]{2L, 3L, 4L};
        assertEquals("2.3.4", ArrayUtil.join(longs, charSequence));
        //int
        int[] ints = new int[]{4, 3, 4, 9};
        assertEquals("4.3.4.9", ArrayUtil.join(ints, charSequence));
        //short
        short[] shorts = new short[]{4, 3, 4, 8};
        assertEquals("4.3.4.8", ArrayUtil.join(shorts, charSequence));
        //char
        char[] chars = new char[]{'a', 'b', 'c', 'd'};
        assertEquals("a.b.c.d", ArrayUtil.join(chars, charSequence));
        //byte
        byte[] bytes = new byte[]{5, 8, 10, 11};
        assertEquals("5.8.10.11", ArrayUtil.join(bytes, charSequence));
        //boolean
        boolean[] booleans = new boolean[]{true, false, true};
        assertEquals("true.false.true", ArrayUtil.join(booleans, charSequence));
        //float
        float[] floats = new float[]{4f, 9f, 11f, 22f};
        assertEquals("4.0.9.0.11.0.22.0", ArrayUtil.join(floats, charSequence));
        //double
        double[] doubles = new double[]{4d, 9d, 13d, 22d};
        assertEquals("4.0.9.0.13.0.22.0", ArrayUtil.join(doubles, charSequence));
        //Object
        assertEquals("4.0.9.0.13.0.22.0", ArrayUtil.join((Object) doubles, charSequence));

    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     * Method: join(T[] array, CharSequence conjunction, String prefix, String suffix)
     */
    @Test
    public void testJoinForArrayConjunctionPrefixSuffix() {
        CharSequence charSequence = "-";
        Object[] objects = new Object[]{1, 2, 5};
        assertEquals("<1>-<2>-<5>", ArrayUtil.join(objects, charSequence, "<", ">"));
    }

    /**
     * 将原始类型数组包装为包装类型
     * Method: wrap(int... values)
     */
    @Test
    public void testWrapValues() {
        //int
        int a = 2, b = 3, c = 5, d = 6;
        Integer[] wrap = ArrayUtil.wrap(a, b, c, d);
        String s = ArrayUtil.toString(wrap);
        assertEquals("[2, 3, 5, 6]", s);
        //long
        long e = 2L, f = 3L, g = 5L, h = 6L;
        Long[] wrap1 = ArrayUtil.wrap(e, f, g, h);
        String s1 = ArrayUtil.toString(wrap1);
        assertEquals("[2, 3, 5, 6]", s1);
        //char
        char char1 = 'a', char2 = 'b', char3 = 'c', char4 = 'd';
        Character[] wrap2 = ArrayUtil.wrap(char1, char2, char3, char4);
        String s2 = ArrayUtil.toString(wrap2);
        assertEquals("[a, b, c, d]", s2);
        //byte
        byte byte1 = 4, byte2 = 33, byte3 = 66;
        Byte[] wrap3 = ArrayUtil.wrap(byte1, byte2, byte3);
        String s3 = ArrayUtil.toString(wrap3);
        assertEquals("[4, 33, 66]", s3);
        //short
        short short1 = 4, short2 = 33, short3 = 6;
        Short[] wrap4 = ArrayUtil.wrap(short1, short2, short3);
        String s4 = ArrayUtil.toString(wrap4);
        assertEquals("[4, 33, 6]", s4);
        //float
        float float1 = 4f, float2 = 33f, float3 = 6f;
        Float[] wrap5 = ArrayUtil.wrap(float1, float2, float3);
        String s5 = ArrayUtil.toString(wrap5);
        assertEquals("[4.0, 33.0, 6.0]", s5);
        //double
        double double1 = 4d, double2 = 33d, double3 = 6d;
        Double[] wrap6 = ArrayUtil.wrap(double1, double2, double3);
        String s6 = ArrayUtil.toString(wrap5);
        assertEquals("[4.0, 33.0, 6.0]", s6);
        //boolean
        Boolean[] wrap7 = ArrayUtil.wrap(false, true, false);
        String s7 = ArrayUtil.toString(wrap7);
        assertEquals("[false, true, false]", s7);
    }

    /**
     * 包装类数组转为原始类型数组
     * Method: unWrap(Integer... values)
     */
    @Test
    public void testUnWrapValues() {
        //Integer
        Integer integer1 = 3, integer2 = 4, integer3 = 55, integer4 = 1;
        int[] ints = ArrayUtil.unWrap(integer1, integer2, integer3, integer4);
        String s = ArrayUtil.toString(ints);
        assertEquals("[3, 4, 55, 1]", s);
        //Long
        Long long1 = 3L, long2 = 4L, long3 = 55L, long4 = 1L;
        long[] longs = ArrayUtil.unWrap(long1, long2, long3, long4);
        String s1 = ArrayUtil.toString(longs);
        assertEquals("[3, 4, 55, 1]", s1);
        //Character
        Character character1 = 'a', character2 = 'b', character3 = 'c', character4 = 'd';
        char[] chars = ArrayUtil.unWrap(character1, character2, character3, character4);
        String s2 = ArrayUtil.toString(chars);
        assertEquals("[a, b, c, d]", s2);
        //Byte
        Byte byte1 = 3, byte2 = 4, byte3 = 55, byte4 = 1;
        byte[] bytes = ArrayUtil.unWrap(byte1, byte2, byte3, byte4);
        String s3 = ArrayUtil.toString(bytes);
        assertEquals("[3, 4, 55, 1]", s3);
        //Short
        Short short1 = 4, short2 = 33, short3 = 6;
        short[] shorts = ArrayUtil.unWrap(short1, short2, short3);
        String s4 = ArrayUtil.toString(shorts);
        assertEquals("[4, 33, 6]", s4);
        //Float
        Float float1 = 4F, float2 = 33F, float3 = 6F;
        float[] floats = ArrayUtil.unWrap(float1, float2, float3);
        String s5 = ArrayUtil.toString(floats);
        assertEquals("[4.0, 33.0, 6.0]", s5);
        //Double
        Double double1 = 4D, double2 = 33D, double3 = 6D;
        double[] doubles = ArrayUtil.unWrap(double1, double2, double3);
        String s6 = ArrayUtil.toString(doubles);
        assertEquals("[4.0, 33.0, 6.0]", s6);
        //Boolean
        boolean[] booleans = ArrayUtil.unWrap(false, true, false);
        String s7 = ArrayUtil.toString(booleans);
        assertEquals("[false, true, false]", s7);
    }

    /**
     * 包装数组对象
     * Method: wrap(Object obj)
     */
    @Test
    public void testWrapObj() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        User[] users = new User[]{user1, user2, user3};
        Object[] wrap = ArrayUtil.wrap(users);
        String s = ArrayUtil.toString(wrap);
        assertEquals("[User(name=null, age=null, studentId=null), User(name=null, age=null, studentId=null), User(name=null, age=null, studentId=null)]", s);


        expectedEx.expect(UtilException.class);
        expectedEx.expectMessage("[class com.uneed.common.core.entity.User] is not Array!");
        Object[] wrap1 = ArrayUtil.wrap(user1);

    }

    /**
     * 判断对象数组是否存在某个对象
     * Method: contains(Object[] array, Object obj)
     */
    @Test
    public void testContains() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        User[] users = new User[]{user1, user2,};
        boolean contains = ArrayUtil.contains(users, user1);
        boolean contains1 = ArrayUtil.contains(users, user3);
        Validate.isTrue(contains);
        assertTrue(contains1);

    }

    /**
     * 返回对象在数组对象对应的数组下标
     * Method: indexOf(Object[] array, Object obj)
     */
    @Test
    public void testIndexOfForArrayObj() {
        User user1 = new User();
        User user2 = new User();

        User[] users = new User[]{user1, user2,};
        int i = ArrayUtil.indexOf(users, user2);
        assertEquals(0, i);

    }

    /**
     * Method: indexOf(Object[] array, Object obj, int start)
     */
    @Test
    public void testIndexOfForArrayObjStart() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();

        User[] users = new User[]{user1, user2, user3, user4};
        int i = ArrayUtil.indexOf(users, user4, 0);
        assertEquals(0, i);
    }

    /**
     * Method: add(T[] array, T element)
     */
    @Test
    public void testAdd() {
        Integer[] integers = new Integer[]{4, 2, 3};
        Integer[] add = ArrayUtil.add(integers, 5);
        String s = ArrayUtil.toString(add);
        assertEquals("[4, 2, 3, 5]", s);
    }

}
