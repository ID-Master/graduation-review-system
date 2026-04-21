package com.uneed.common.core.collection;

import com.uneed.common.core.entity.User;
import com.uneed.common.core.lang.Validate;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.collections4.Predicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

/**
 * CollectionUtil Tester.
 *
 * @author huangad@coracle.com
 * @date 10/23/2019
 */
public class CollectionUtilTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * 集合是否为空
     * Method: isEmpty(Collection<T> collection)
     */
    @Test
    public void testIsEmptyCollection() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        assertFalse(CollectionUtil.isEmpty(list));
    }

    /**
     * Iterable是否为空
     * Method: isEmpty(Iterable<?> iterable)
     */
    @Test
    public void testIsEmptyIterable() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        assertFalse(CollectionUtil.isEmpty((Iterable<Integer>) list));
    }

    /**
     * Iterator是否为空
     * Method: isEmpty(Iterator<?> iterator)
     */
    @Test
    public void testIsEmptyIterator() {
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> iterator = list.iterator();
        assertTrue(CollectionUtil.isEmpty(iterator));
    }

    /**
     * Enumeration是否为空
     * Method: isEmpty(Enumeration<?> enumeration)
     */
    @Test
    public void testIsEmptyEnumeration() {
        Enumeration enumeration = new IteratorEnumeration();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        Iterator iterator = list.iterator();
        ((IteratorEnumeration) enumeration).setIterator(iterator);
        assertFalse(CollectionUtil.isEmpty(enumeration));
    }

    /**
     * 集合是否不为空
     * Method: isNotEmpty(Collection<T> collection)
     */
    @Test
    public void testIsNotEmptyCollection() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        assertTrue(CollectionUtil.isNotEmpty(list));
    }

    /**
     * Iterable是否不为空
     * Method: isNotEmpty(Iterable<?> iterable)
     */
    @Test
    public void testIsNotEmptyIterable() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        assertTrue(CollectionUtil.isNotEmpty((Iterable<Integer>) list));
    }

    /**
     * Iterator是否不为空
     * Method: isNotEmpty(Iterator<?> iterator)
     */
    @Test
    public void testIsNotEmptyIterator() {
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> iterator = list.iterator();
        assertFalse(CollectionUtil.isNotEmpty(iterator));
    }

    /**
     * Enumeration是否不为空
     * Method: isNotEmpty(Enumeration<?> enumeration)
     */
    @Test
    public void testIsNotEmptyEnumeration() {
        Enumeration enumeration = new IteratorEnumeration();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        Iterator iterator = list.iterator();
        ((IteratorEnumeration) enumeration).setIterator(iterator);
        assertTrue(CollectionUtil.isNotEmpty(enumeration));
    }

    /**
     * 创建新的集合对象
     * Method: create(Class<?> collectionType)
     */
    @Test
    public void testCreate() {
        //抽象集合
        Collection<Object> objects = CollectionUtil.create(AbstractCollection.class);
        Validate.isInstanceOf(ArrayList.class, objects);
        //Set
        Collection<Object> objects1 = CollectionUtil.create(HashSet.class);
        Validate.isInstanceOf(HashSet.class, objects1);

        Collection<Object> objects2 = CollectionUtil.create(LinkedHashSet.class);
        Validate.isInstanceOf(LinkedHashSet.class, objects2);

        Collection<Object> objects3 = CollectionUtil.create(TreeSet.class);
        Validate.isInstanceOf(TreeSet.class, objects3);

        // Collection<Object> objects4 = CollectionUtil.create(EnumSet.class);
        //Validate.isInstanceOf(EnumSet.class, objects4);报空指针异常

        //List
        Collection<Object> objects5 = CollectionUtil.create(ArrayList.class);
        Validate.isInstanceOf(ArrayList.class, objects5);

        Collection<Object> objects6 = CollectionUtil.create(LinkedList.class);
        Validate.isInstanceOf(LinkedList.class, objects6);


    }

    /**
     * 将指定对象全部加入到集合中
     * Method: addAll(Collection<T> collection, Object value)
     */
    @Test
    public void testAddAllForCollectionValue() {

        List<Integer> list = new ArrayList<>();
        Integer integer = 11;
        Collection<Integer> collection = CollectionUtil.addAll(list, integer);
        Validate.isTrue(collection.contains(integer));

    }

    /**
     * 将指定对象全部加入到集合中
     * Method: addAll(Collection<T> collection, Object value, Type elementType)
     */
    @Test
    public void testAddAllForCollectionValueElementType() {
        List<Integer> list = new ArrayList<>();
        Integer integer = 11;
        Type type = integer.getClass();
        Collection<Integer> collection = CollectionUtil.addAll(list, integer, type);
        Validate.isTrue(collection.contains(integer));
    }

    /**
     * 加入全部到集合
     * Method: addAll(Collection<T> collection, Iterator<T> iterator)
     */
    @Test
    public void testAddAllForCollectionIterator() {
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        list.add(11);
        list1.add(22);
        Iterator<Integer> iterator = list1.iterator();
        Collection<Integer> collection = CollectionUtil.addAll(list, iterator);
        Validate.isTrue(collection.contains(11) && collection.contains(22));
    }

    /**
     * 加入全部到集合
     * Method: addAll(Collection<T> collection, Iterable<T> iterable)
     */
    @Test
    public void testAddAllForCollectionIterable() {
        List<Integer> list = new ArrayList<>();
        Integer integer = 11;
        list.add(11);

        Set<Integer> set = new HashSet<>();
        Integer integer1 = 122;
        set.add(integer1);

        Collection<Integer> collection = CollectionUtil.addAll(list, set);
        Validate.isTrue(collection.contains(integer) && collection.contains(integer1));

    }

    /**
     * 加入全部到集合
     * Method: addAll(Collection<T> collection, Enumeration<T> enumeration)
     */
    @Test
    public void testAddAllForCollectionEnumeration() {
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        list.add(99);
        list1.add(1);
        Iterator<Integer> iterator = list1.iterator();
        Enumeration<Integer> enumeration = new IteratorEnumeration();
        ((IteratorEnumeration) enumeration).setIterator(iterator);
        Collection<Integer> collection = CollectionUtil.addAll(list, enumeration);
        Validate.isTrue(collection.contains(99) && collection.contains(1));


    }

    /**
     * 加入全部数组元素到集合
     * Method: addAll(Collection<T> collection, T[] values)
     */
    @Test
    public void testAddAllForCollectionValues() {
        List<Integer> list = new ArrayList<>();
        Integer[] integers = new Integer[]{5, 3, 22, 8};
        Collection<Integer> collection = CollectionUtil.addAll(list, integers);
        Validate.isTrue(collection.contains(5) && collection.contains(22));
    }

    /**
     * 新建一个空List
     * Method: list(boolean isLinked)
     */
    @Test
    public void testListIsLinked() {
        List<Integer> list = CollectionUtil.list(false);
        assertTrue(CollectionUtil.isEmpty(list));
    }

    /**
     * 新建一个List
     * Method: list(boolean isLinked, T... values)
     */
    @Test
    public void testListForIsLinkedValues() {
        Integer integer = 5;
        Integer integer1 = 222;
        List<Integer> list = CollectionUtil.list(false, integer, integer1);

        Validate.isTrue(list.contains(5) && list.contains(222));
    }

    /**
     * 新建一个List
     * Method: list(boolean isLinked, Collection<T> collection)
     */
    @Test
    public void testListForIsLinkedCollection() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        List<Integer> list1 = CollectionUtil.list(false, set);
        Validate.isTrue(!CollectionUtil.isEmpty(list1));
    }

    /**
     * Method: list(boolean isLinked, Iterable<T> iterable)
     */
    @Test
    public void testListForIsLinkedIterable() {
        List<Integer> list = CollectionUtil.list(false);
        List<Integer> list1 = CollectionUtil.list(false, (Iterable<Integer>) list);
        Validate.isTrue(CollectionUtil.isEmpty(list1));

        Iterable<Integer> iterable = null;
        assertEquals("[]", CollectionUtil.list(false, iterable).toString());
    }

    /**
     * Method: list(boolean isLinked, Iterator<T> iter)
     */
    @Test
    public void testListForIsLinkedIter() {
        List<Integer> list = new ArrayList<>();
        list.add(11);
        Iterator<Integer> iterator = list.iterator();
        List list1 = CollectionUtil.list(true, iterator);
        Validate.isTrue(list1.contains(11));

        iterator = null;
        assertEquals("[]", CollectionUtil.list(true, iterator).toString());
    }

    /**
     * 新建一个List
     * Method: list(boolean isLinked, Enumeration<T> enumration)
     */
    @Test
    public void testListForIsLinkedEnumration() {
        List<Integer> list = new ArrayList<>();
        list.add(500);
        Iterator<Integer> iterator = list.iterator();
        Enumeration<Integer> enumeration = new IteratorEnumeration();
        ((IteratorEnumeration) enumeration).setIterator(iterator);

        List list1 = CollectionUtil.list(false, enumeration);
        Validate.isTrue(list1.contains(500));

        enumeration = null;
        assertEquals("[]", CollectionUtil.list(false, enumeration).toString());
    }

    /**
     * 新建一个ArrayList
     * Method: newArrayList(T... values)
     */
    @Test
    public void testNewArrayListValues() {
        Integer[] integers = new Integer[]{5, 6};
        ArrayList<Integer> arrayList = CollectionUtil.newArrayList(integers);
        Validate.isTrue(arrayList.contains(5) && arrayList.contains(6));

    }

    /**
     * 数组转为ArrayList
     * Method: toList(T... values)
     */
    @Test
    public void testToList() {
        Integer[] integers = new Integer[]{250, 111};
        ArrayList<Integer> arrayList = CollectionUtil.toList(integers);
        Validate.isTrue(arrayList.contains(250) && arrayList.contains(111));
    }

    /**
     * 新建一个ArrayList
     * Method: newArrayList(Collection<T> collection)
     */
    @Test
    public void testNewArrayListCollection() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        ArrayList<Integer> arrayList = CollectionUtil.newArrayList(set);
        Validate.isTrue(!CollectionUtil.isEmpty(arrayList));
    }

    /**
     * 新建一个ArrayList
     * Method: newArrayList(Iterable<T> iterable)
     */
    @Test
    public void testNewArrayListIterable() {
        Iterable<Integer> iterable = null;
        assertEquals("[]", CollectionUtil.newArrayList(iterable).toString());

        List<Integer> list = CollectionUtil.list(false);
        list.add(11);
        List<Integer> list1 = CollectionUtil.newArrayList((Iterable<Integer>) list);
        Validate.isTrue(!CollectionUtil.isEmpty(list1));
    }

    /**
     * 新建一个ArrayList
     * Method: newArrayList(Iterator<T> iter)
     */
    @Test
    public void testNewArrayListIter() {
        List<Integer> list = new ArrayList<>();
        list.add(11);
        Iterator<Integer> iterator = list.iterator();
        List list1 = CollectionUtil.newArrayList(iterator);
        Validate.isTrue(list1.contains(11));

        iterator = null;
        assertEquals("[]", CollectionUtil.newArrayList(iterator).toString());
    }

    /**
     * 新建一个ArrayList
     * Method: newArrayList(Enumeration<T> enumration)
     */
    @Test
    public void testNewArrayListEnumration() {
        List<Integer> list = new ArrayList<>();
        list.add(500);
        Iterator<Integer> iterator = list.iterator();
        Enumeration<Integer> enumeration = new IteratorEnumeration();
        ((IteratorEnumeration) enumeration).setIterator(iterator);

        List list1 = CollectionUtil.newArrayList(enumeration);
        Validate.isTrue(list1.contains(500));

        enumeration = null;
        assertEquals("[]", CollectionUtil.newArrayList(enumeration).toString());
    }

    /**
     * 新建LinkedList
     * Method: newLinkedList(T... values)
     */
    @Test
    public void testNewLinkedList() {
        Integer[] integers = new Integer[]{250, 111};
        LinkedList<Integer> linkedList = CollectionUtil.newLinkedList(integers);
        Validate.isTrue(linkedList.contains(250) && linkedList.contains(111));

    }

    /**
     * 新建一个CopyOnWriteArrayList
     * Method: newCopyOnWriteArrayList(Collection<T> collection)
     */
    @Test
    public void testNewCopyOnWriteArrayList() {
        List<Long> list = new LinkedList<>();
        list.add(5L);
        CopyOnWriteArrayList<Long> longs = CollectionUtil.newCopyOnWriteArrayList(list);
        Validate.isTrue(longs.contains(5L));

    }

    /**
     * 新建 BlockingQueue
     * Method: newBlockingQueue(int capacity, boolean isLinked)
     */
    @Test
    public void testNewBlockingQueue() {
        int capacity = 10;
        BlockingQueue<Object> blockingQueue = CollectionUtil.newBlockingQueue(capacity, false);
        Object object = new Object();
        blockingQueue.add(object);
        Validate.isTrue(blockingQueue.contains(object));

    }

    /**
     * Iterable}转为{@link Collection
     * Method: toCollection(Iterable<E> iterable)
     */
    @Test
    public void testToCollection() {
        List<Integer> list = new LinkedList<>();
        list.add(11);
        Collection<Integer> collection = CollectionUtil.toCollection(list);
        Validate.isTrue(collection.contains(11));

    }

    /**
     * 新建一个HashSet
     * Method: newHashSet(T... ts)
     */
    @Test
    public void testNewHashSetTs() {
        Float[] floats = new Float[]{5f, 44f};
        HashSet<Float> floats1 = CollectionUtil.newHashSet(floats);
        Validate.isTrue(floats1.contains(5f) && floats1.contains(44f));
    }

    /**
     * 新建一个HashSet
     * Method: newLinkedHashSet(T... ts)
     */
    @Test
    public void testNewLinkedHashSet() {
        Double[] doubles = new Double[]{4D, 5D};
        LinkedHashSet<Double> doubles1 = CollectionUtil.newLinkedHashSet(doubles);
        Validate.isTrue(doubles1.contains(4D) && doubles1.contains(5D));
    }

    /**
     * 新建一个HashSet
     * Method: newHashSet(boolean isSorted, T... ts)
     */
    @Test
    public void testNewHashSetForIsSortedTs() {
        Integer[] integers = new Integer[]{4, 1};
        HashSet<Integer> hashSet = CollectionUtil.newHashSet(true, integers);

        Validate.isTrue(!CollectionUtil.isEmpty(hashSet));

    }

    /**
     * 新建一个HashSet
     * Method: newHashSet(Collection<T> collection)
     */
    @Test
    public void testNewHashSetCollection() {
        List<Integer> list = new LinkedList<>();
        HashSet<Integer> integers = CollectionUtil.newHashSet(list);
        Validate.isTrue(CollectionUtil.isEmpty(integers));
    }

    /**
     * 新建一个HashSet
     * Method: newHashSet(boolean isSorted, Collection<T> collection)
     */
    @Test
    public void testNewHashSetForIsSortedCollection() {
        List<Integer> list = new LinkedList<>();
        HashSet<Integer> integers = CollectionUtil.newHashSet(true, list);
        Validate.isTrue(CollectionUtil.isEmpty(integers));
    }

    /**
     * 新建一个HashSet
     * Method: newHashSet(boolean isSorted, Iterator<T> iter)
     */
    @Test
    public void testNewHashSetForIsSortedIter() {
        List<Integer> list = new LinkedList<>();
        list.add(12);
        Iterator<Integer> iterator = list.iterator();
        HashSet<Integer> integers = CollectionUtil.newHashSet(false, iterator);
        Validate.isTrue(integers.contains(12));
    }

    /**
     * 新建一个HashSet
     * Method: newHashSet(boolean isSorted, Enumeration<T> enumration)
     */
    @Test
    public void testNewHashSetForIsSortedEnumration() {
        List<Integer> list = new LinkedList<>();
        list.add(555);
        Iterator<Integer> iterator = list.iterator();
        Enumeration<Integer> enumeration = new IteratorEnumeration();
        ((IteratorEnumeration) enumeration).setIterator(iterator);

        HashSet<Integer> integers = CollectionUtil.newHashSet(false, enumeration);
        Validate.isTrue(integers.contains(555));


    }

    /**
     * 循环为迭代对象的每个元素的指定属性赋值
     * Method: forEach(final Iterable<E> iterable, String property, Object value)
     */
    @Test
    public void testForEach() {
        List<User> list = new LinkedList<>();
        User user1 = new User();
        User user2 = new User();
        list.add(user1);
        list.add(user2);
        String property = "name";
        String value = "hello";

        CollectionUtil.forEach(list, property, value);
        assertEquals("hello", user1.getName());
        assertEquals("hello", user2.getName());

    }

    /**
     * 从集合中获取第一个指定属性与指定属性值所对应的对象的索引位置
     * Method: indexOf(List<T> list, String property, V value)
     */
    @Test
    public void testIndexOf() {
        List<User> list = new LinkedList<>();
        User user1 = new User();
        User user2 = new User();
        user1.setName("hello");
        user2.setName("world");

        list.add(user1);
        list.add(user2);
        String property = "name";
        String value = "hello";
        String value1 = "world";

        assertEquals(0, CollectionUtil.indexOf(list, property, value));
        assertEquals(1, CollectionUtil.indexOf(list, property, value1));

    }

    /**
     * Method: removeAll(Collection<T> collection, Collection<T> elements)
     */
    @Test
    public void testRemoveAll() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> list1 = new LinkedList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list2 = CollectionUtil.removeAll(list, list1);
        assertEquals("[3]", list2.toString());
    }

    /**
     * 从指定迭代对象中，获取指定属性的值集合，返回为List
     * Method: getPropertyList(Iterable<E> iterable, String property)
     */
    @Test
    public void testGetPropertyList() {
        List<User> list = new LinkedList<>();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setName("hello");
        user2.setName("world");
        list.add(user1);
        list.add(user2);
        list.add(user3);

        String property = "name";
        List<Object> propertyList = CollectionUtil.getPropertyList(list, property);
        assertEquals("[hello, world, null]", propertyList.toString());

        List<String> names = CollectionUtil.getPropertyList(list, User::getName);
        assertEquals("[hello, world, null]", names.toString());

        List<Object> propertyList2 = CollectionUtil.getPropertyList(list, property, true);
        assertEquals("[hello, world]", propertyList2.toString());

        List<String> names2 = CollectionUtil.getPropertyList(list, User::getName, true);
        assertEquals("[hello, world]", names2.toString());
    }

    /**
     * 从指定迭代对象中，获取指定属性的值集合，返回为Set
     * Method: getPropertySet(Iterable<E> iterable, String property)
     */
    @Test
    public void testGetPropertySet() {
        List<User> list = new LinkedList<>();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setName("hello");
        user2.setName("world");
        list.add(user1);
        list.add(user2);
        list.add(user3);

        String property = "name";
        Set<Object> propertySet = CollectionUtil.getPropertySet(list, property);
        assertEquals("[hello, world, null]", propertySet.toString());

        Set<String> names = CollectionUtil.getPropertySet(list, User::getName);
        assertEquals("[hello, world, null]", names.toString());

        Set<Object> propertySet2 = CollectionUtil.getPropertySet(list, property, true);
        assertEquals("[hello, world]", propertySet2.toString());

        Set<String> names2 = CollectionUtil.getPropertySet(list, User::getName, true);
        assertEquals("[hello, world]", names2.toString());
    }

    /**
     * 循环指定迭代对象，以迭代对象中的keyProperty属性值当key，迭代对象中的valueProperty属性值当value，封装成Map对象返回
     * Method: getPropertyMap(Iterable<E> iterable, String keyProperty, String valueProperty)
     */
    @Test
    public void testGetPropertyMap() {
        List<User> list = new LinkedList<>();
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");
        user2.setName("李四");
        user1.setAge(18);
        user2.setAge(22);
        list.add(user1);
        list.add(user2);

        String keyProperty = "name";
        String valueProperty = "age";

        Map<Object, Object> propertyMap = CollectionUtil.getPropertyMap(list, keyProperty, valueProperty);
        assertEquals("{李三=18, 李四=22}", propertyMap.toString());

        Map<String, Integer> propertyMap2 = CollectionUtil.getPropertyMap(list, User::getName, User::getAge);
        assertEquals("{李三=18, 李四=22}", propertyMap2.toString());
    }

    /**
     * 循环指定集合中的对象，得到指定属性的值后，填充到返回集合中
     * Method: get(Iterable<E> iterable, String property, V value)
     */
    @Test
    public void testGetForIterablePropertyValue() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        String property = "name";
        String value = "李三";
        User user = CollectionUtil.get(list, property, value);
        assertEquals(user1, user);

        User user3 = CollectionUtil.get(list, User::getName, value);
        assertEquals(user1, user3);

        User user4 = CollectionUtil.get(list, User::getName, "value");
        assertNotEquals(user1, user4);
    }

    /**
     * 从指定迭代对象中,获取第一个与匹配规则对象对应的元素
     * Method: get(Iterable<E> iterable, Predicate<E> predicate)
     */
    @Test
    public void testGetForIterablePredicate() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        Predicate<User> predicate = user -> user.getName() != null;
        User user3 = CollectionUtil.get(list, predicate);

        //User user4 = CollectionUtil.get(list, user -> user.getName() != null);
        assertEquals(user3, user1);

    }

    /**
     * 循环迭代对象,根据指定属性名，获取迭代对象中的元素,判断值是否在指定的数组值集中，如果在,将该对象存入list中并返回
     * Method: find(Iterable<E> iterable, String property, V... values)
     */
    @Test
    public void testFindForIterablePropertyValues() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");
        user2.setName("李四");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        String property = "name";
        String[] strings = new String[]{"李三", "李四", "卡特"};

        List<User> users = CollectionUtil.find(list, property, strings);
        assertEquals("[User(name=李三, age=null, studentId=null), User(name=李四, age=null, studentId=null)]", users.toString());

        List<User> users2 = CollectionUtil.find(list, User::getName, strings);
        assertEquals("[User(name=李三, age=null, studentId=null), User(name=李四, age=null, studentId=null)]", users2.toString());
    }

    /**
     * 循环迭代对象,根据指定属性名，获取迭代对象中的元素,判断值是否在指定的列表值集中，如果在,将该对象存入list中并返回
     * Method: find(Iterable<E> iterable, String property, Collection<V> collection)
     */
    @Test
    public void testFindForIterablePropertyCollection() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");
        user2.setName("李四");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        String property = "name";

        Set<String> strings = new HashSet<>();
        strings.add("李三");
        strings.add("李四");
        strings.add("李五");
        List<User> users = CollectionUtil.find(list, property, strings);
        assertEquals("[User(name=李三, age=null, studentId=null), User(name=李四, age=null, studentId=null)]", users.toString());

        List<User> users2 = CollectionUtil.find(list, User::getName, strings);
        assertEquals("[User(name=李三, age=null, studentId=null), User(name=李四, age=null, studentId=null)]", users2.toString());

    }

    /**
     * 循环迭代对象,获取与匹配规则相符的数据对象，存入list中并返回
     * Method: find(Iterable<E> iterable, Predicate<E> predicate)
     */
    @Test
    public void testFindForIterablePredicate() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        Predicate<User> predicate = user -> user.getName() != null;

        List<User> users = CollectionUtil.find(list, predicate);
        assertEquals("[User(name=李三, age=null, studentId=null)]", users.toString());
    }

    /**
     * 循环迭代对象,根据指定属性名，获取迭代对象中的元素,判断值是否在指定的数组值集中，如果不在,将该对象存入list中并返回
     * Method: findRejected(Iterable<E> iterable, String property, V... values)
     */
    @Test
    public void testFindRejectedForIterablePropertyValues() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");
        user2.setName("卡特");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        String property = "name";
        String[] strings = new String[]{"李三", "李四",};

        List<User> users = CollectionUtil.findRejected(list, property, strings);
        assertEquals("[User(name=卡特, age=null, studentId=null)]", users.toString());

        List<User> users2 = CollectionUtil.findRejected(list, User::getName, strings);
        assertEquals("[User(name=卡特, age=null, studentId=null)]", users2.toString());
    }

    /**
     * 循环迭代对象,根据指定属性名，获取迭代对象中的元素,判断值是否在指定的列表值集中，如果不在,将该对象存入list中并返回
     * Method: findRejected(Iterable<E> iterable, String property, Collection<V> collection)
     */
    @Test
    public void testFindRejectedForIterablePropertyCollection() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");
        user2.setName("李四");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        String property = "name";

        Set<String> strings = new HashSet<>();
        strings.add("李三");
        strings.add("李五");
        List<User> users = CollectionUtil.findRejected(list, property, strings);
        assertEquals("[User(name=李四, age=null, studentId=null)]", users.toString());

        List<User> users2 = CollectionUtil.findRejected(list, User::getName, strings);
        assertEquals("[User(name=李四, age=null, studentId=null)]", users2.toString());
    }

    /**
     * 循环迭代对象,获取与匹配规则不相符的数据对象，存入list中并返回
     * Method: findRejected(Iterable<E> iterable, Predicate<E> predicate)
     */
    @Test
    public void testFindRejectedForIterablePredicate() {
        User user1 = new User();
        User user2 = new User();
        user1.setName("李三");

        List<User> list = new LinkedList<>();
        list.add(user1);
        list.add(user2);

        Predicate<User> predicate = user -> user.getName() == null;

        List<User> users = CollectionUtil.findRejected(list, predicate);
        assertEquals("[User(name=李三, age=null, studentId=null)]", users.toString());
    }

    /**
     * Method: contact(Collection<E> c1, Collection<E> c2)
     */
    @Test
    public void testContact() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        Set<Integer> set = new LinkedHashSet<>();
        set.add(3);
        set.add(4);
        Collection<Integer> contact = CollectionUtil.contact(list, set);
        assertEquals("[1, 2, 3, 4]", contact.toString());
    }

}
