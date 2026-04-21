package com.uneed.common.core.lang;

import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.entity.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * ObjectUtil Tester.
 *
 * @author diablo
 * @date 07/22/2018
 */
public class ObjectUtilTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void testIsNull() {
        String str = null;
        Assert.assertTrue(ObjectUtil.isNull(str));
    }

    @Test
    public void testIsNotNull() {
        String str = "";
        Assert.assertTrue(ObjectUtil.isNotNull(str));
    }

    /**
     * Method: isEmpty(Object obj)
     */
    @Test
    public void testIsEmpty() {
        //---------------------------字符串测试------------------------------------
        Object obj = null;
        assertTrue(ObjectUtil.isEmpty(obj));
        //空字符串
        String str = "";
        assertTrue(ObjectUtil.isEmpty(str));
        //空格
        str = " ";
        assertTrue(ObjectUtil.isEmpty(str));
        //多个tab
        str = "         ";
        assertTrue(ObjectUtil.isEmpty(str));
        str = "abc";
        assertFalse(ObjectUtil.isEmpty(str));

        StringBuilder sb = new StringBuilder();
        assertTrue(ObjectUtil.isEmpty(sb));
        sb.append("  ");
        assertTrue(ObjectUtil.isEmpty(sb));
        sb.append("aa");
        assertFalse(ObjectUtil.isEmpty(sb));

        //---------------------------集合测试------------------------------------
        List<Integer> list = new ArrayList<>();
        assertTrue(ObjectUtil.isEmpty(list));
        list.add(1);
        assertFalse(ObjectUtil.isEmpty(list));

        //---------------------------map测试------------------------------------
        Map<String, String> map = new HashMap<>();
        assertTrue(ObjectUtil.isEmpty(map));
        map.put("key", "value");
        assertFalse(ObjectUtil.isEmpty(map));

        //---------------------------数组测试------------------------------------
        String[] arr = {};
        assertTrue(ObjectUtil.isEmpty(arr));
        arr = new String[1];
        assertFalse(ObjectUtil.isEmpty(arr));
        assertTrue(ObjectUtil.isEmpty(arr[0]));

        //---------------------------迭代器测试------------------------------------
        Set<String> set = new HashSet<>();
        Iterator<String> it = set.iterator();
        assertTrue(ObjectUtil.isEmpty(it));
        //增加元素
        set.add("aa");
        Iterator<String> it2 = set.iterator();
        assertFalse(ObjectUtil.isEmpty(it2));

        //---------------------------其他测试------------------------------------
        User user = new User();
        assertFalse(ObjectUtil.isEmpty(user));
        assertTrue(ObjectUtil.isEmpty(user.getName()));
    }

    /**
     * Method: isNotEmpty(Object obj)
     */
    @Test
    public void testIsNotEmpty() {
        //---------------------------字符串测试------------------------------------
        String str = null;
        assertFalse(ObjectUtil.isNotEmpty(str));
        //空字符串
        str = "";
        assertFalse(ObjectUtil.isNotEmpty(str));
        //空格
        str = " ";
        assertFalse(ObjectUtil.isNotEmpty(str));
        //多个tab
        str = "         ";
        assertFalse(ObjectUtil.isNotEmpty(str));
        StringBuilder sb = null;
        assertFalse(ObjectUtil.isNotEmpty(sb));
        sb = new StringBuilder();
        assertFalse(ObjectUtil.isNotEmpty(sb));

        //---------------------------集合测试------------------------------------
        List<Integer> list = null;
        assertFalse(ObjectUtil.isNotEmpty(list));
        list = new ArrayList<>();
        assertFalse(ObjectUtil.isNotEmpty(list));
        list.add(1);
        assertTrue(ObjectUtil.isNotEmpty(list));

        //---------------------------map测试------------------------------------
        Map<String, String> map = null;
        assertFalse(ObjectUtil.isNotEmpty(map));
        map = new HashMap<>();
        assertFalse(ObjectUtil.isNotEmpty(map));
        map.put("key", "value");
        assertTrue(ObjectUtil.isNotEmpty(map));


        //---------------------------数组测试------------------------------------
        String[] arr = {};
        assertFalse(ObjectUtil.isNotEmpty(arr));
        arr = new String[1];
        assertTrue(ObjectUtil.isNotEmpty(arr));
        assertFalse(ObjectUtil.isNotEmpty(arr[0]));

        //---------------------------迭代器测试------------------------------------
        Set<String> set = new HashSet<>();
        Iterator<String> it = set.iterator();
        assertFalse(ObjectUtil.isNotEmpty(it));
        //增加元素
        set.add("aa");
        Iterator<String> it2 = set.iterator();
        assertTrue(ObjectUtil.isNotEmpty(it2));

        //---------------------------其他测试------------------------------------
        User user = new User();
        assertTrue(ObjectUtil.isNotEmpty(user));
        assertFalse(ObjectUtil.isNotEmpty(user.getName()));
    }

    /**
     * Method: equal(Object orig, Object dest)
     */
    @Test
    public void testEqual() {
        int a = 1;
        Integer b = 1;
        Long c = 1L;
        long d = 1;
        assertTrue(ObjectUtil.equal(a, b));
        assertFalse(ObjectUtil.equal(a, c));
        assertFalse(ObjectUtil.equal(b, c));
        assertFalse(ObjectUtil.equal(a, d));

        String s1 = null;
        String s2 = "";
        String s3 = "   ";
        String s4 = "a";
        String s5 = "a";
        assertFalse(ObjectUtil.equal(s1, s3));
        assertFalse(ObjectUtil.equal(s2, s3));
        assertTrue(ObjectUtil.equal(s4, s5));

        User user1 = new User();
        user1.setName("张三");
        user1.setAge(18);
        User user2 = user1;
        assertTrue(ObjectUtil.equal(user1, user2));
        User user3 = new User();
        user3.setName("张三");
        user3.setAge(18);
        assertFalse(ObjectUtil.notEqual(user1, user3));
    }


    /**
     * Method: nullToDefault(T obj, @Nonnull T value)
     */
    @Test
    public void testNullToDefault() {
        String str = null;
        assertEquals("aa", ObjectUtil.nullToDefault(str, "aa"));
        str = "bb";
        assertEquals("bb", ObjectUtil.nullToDefault(str, "aa"));

        User user = null;
        assertNull(user);
        assertNotNull(ObjectUtil.nullToDefault(user, new User()));
    }

    @Test
    public void testConvert() {
        String obj = "12";
        long a = ObjectUtil.convert(obj, long.class);
        assertEquals(12, a);
        Integer b = 12;
        assertEquals("12", ObjectUtil.convert(b, String.class));


        //测试待默认值的转换
        assertEquals("12", ObjectUtil.convert(b, "5"));
        b = null;
        assertEquals("5", ObjectUtil.convert(b, "5"));
    }

    @Test
    public void testGtZero() {
        Integer a = 1;
        assertTrue(ObjectUtil.geZero(a));
        assertTrue(ObjectUtil.gtZero(a));
        Integer b = 0;
        assertTrue(ObjectUtil.geZero(b));
        assertFalse(ObjectUtil.gtZero(b));
        Integer c = -1;
        assertFalse(ObjectUtil.geZero(c));
        assertFalse(ObjectUtil.gtZero(c));
    }

    @Test
    public void testGroup() {
        List<User> users = getUsers(2000);
        List<List<User>> group = ObjectUtil.group(users);
        assertEquals(2, group.size());
        assertEquals(1000, group.get(0).size());
        assertEquals(1000, group.get(1).size());
        List<User> subList = ObjectUtil.dataPaging(users, 4, 600);
        assertEquals(200, subList.size());
        List<User> subList2 = ObjectUtil.dataPaging(users, 5, 600);
        assertEquals(0, subList2.size());
        List<User> subList3 = ObjectUtil.dataPaging(users, 2, 600);
        assertEquals(600, subList3.size());


        List<User> users2 = getUsers(2001);
        List<List<User>> group2 = ObjectUtil.group(users2);
        assertEquals(3, group2.size());
        assertEquals(1000, group2.get(0).size());
        assertEquals(1000, group2.get(1).size());
        assertEquals(1, group2.get(2).size());

        List<User> users3 = getUsers(1000);
        List<List<User>> group3 = ObjectUtil.group(users3);
        assertEquals(1, group3.size());
        assertEquals(1000, group3.get(0).size());

        List<User> users4 = getUsers(1999);
        List<List<User>> group4 = ObjectUtil.group(users4);
        assertEquals(2, group4.size());
        assertEquals(1000, group4.get(0).size());
        assertEquals(999, group4.get(1).size());
    }

    private List<User> getUsers(int size) {
        List<User> users = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            User user = new User();
            user.setStudentId(i + 1);
            user.setAge(user.getStudentId() % 2 == 0 ? 17 : 18);
            user.setName("student" + user.getStudentId());
            users.add(user);
        }
        return users;
    }

    /**
     * Method: compare(T orig, T dest)
     */
    @Test
    public void testCompare() {
        Integer a = 1;
        Integer b = 13;
        assertEquals(-1, ObjectUtil.compare(a, b));

        assertEquals(0, ObjectUtil.compare(null, null));

        String aa = "aa";
        String bb = aa;
        String cc = "a";

        assertEquals(0, ObjectUtil.compare(aa, bb));
        assertEquals(1, ObjectUtil.compare(aa, cc));
    }

    /**
     * Method: compare(T orig, T dest)
     */
    @Test
    public void testToMap() {

        List<User> list = Lists.newArrayList(
                new User("张三", 18, 1),
                new User("李四", 19, 2),
                new User("王五", 17, 3),
                new User("马六", 18, 4),
                new User("钱七", 16, 5),
                new User("赵一", null, 6),
                new User(null, null, 7),
                null
        );

        Map<Integer, User> idMap = ObjectUtil.toMap(list, "studentId");
        assertEquals(7, idMap.size());
        Map<Integer, User> idMap2 = ObjectUtil.toMap(list, User::getStudentId);
        assertEquals(7, idMap2.size());

        Map<String, User> nameMap = ObjectUtil.toMap(list, "name");
        assertEquals(6, nameMap.size());
        Map<String, User> nameMap2 = ObjectUtil.toMap(list, User::getName);
        assertEquals(6, nameMap2.size());

        Map<Integer, User> ageMap = ObjectUtil.toMap(list, "age");
        assertEquals(4, ageMap.size());
        Map<Integer, User> ageMap2 = ObjectUtil.toMap(list, User::getAge);
        assertEquals(4, ageMap2.size());

        Map<Integer, String> idNameMap = ObjectUtil.toMap(list, "studentId", "name");
        assertEquals(7, idNameMap.size());
        Map<Integer, String> idNameMap2 = ObjectUtil.toMap(list, User::getStudentId, User::getName);
        assertEquals(7, idNameMap2.size());

        Map<Integer, String> idNameMap3 = ObjectUtil.toMap(list, "studentId", "name", true);
        assertEquals(6, idNameMap3.size());
        Map<Integer, String> idNameMap4 = ObjectUtil.toMap(list, User::getStudentId, User::getName, true);
        assertEquals(6, idNameMap4.size());

        Map<Integer, Integer> idAgeMap = ObjectUtil.toMap(list, "studentId", "age");
        assertEquals(7, idAgeMap.size());
        Map<Integer, Integer> idAgeMap2 = ObjectUtil.toMap(list, User::getStudentId, User::getAge);
        assertEquals(7, idAgeMap2.size());

        Map<Integer, Integer> idAgeMap3 = ObjectUtil.toMap(list, "studentId", "age", true);
        assertEquals(5, idAgeMap3.size());
        Map<Integer, Integer> idAgeMap4 = ObjectUtil.toMap(list, User::getStudentId, User::getAge, true);
        assertEquals(5, idAgeMap4.size());
    }

    /**
     * Method: compare(T orig, T dest)
     */
    @Test
    public void testToMapList() {

        List<User> list = Lists.newArrayList(
                new User("张三", 18, 1),
                new User("李四", 17, 2),
                new User("王五", 17, 3),
                new User("马六", 18, 4),
                new User("钱七", 17, 5),
                new User("赵一", 18, 6),
                new User(null, 17, 7),
                null
        );

        Map<Integer, List<User>> idMap = ObjectUtil.toMapList(list, "studentId");
        System.out.println(idMap);
        assertEquals(7, idMap.size());
        Map<Integer, List<User>> idMap2 = ObjectUtil.toMapList(list, User::getStudentId);
        System.out.println(idMap2);
        assertEquals(7, idMap2.size());

        Map<Integer, List<User>> idMap3 = ObjectUtil.toMapList(list, "studentId", true);
        System.out.println(idMap3);
        assertEquals(7, idMap3.size());
        Map<Integer, List<User>> idMap4 = ObjectUtil.toMapList(list, User::getStudentId, true);
        System.out.println(idMap4);
        assertEquals(7, idMap4.size());

        Map<Integer, List<User>> ageMap = ObjectUtil.toMapList(list, "age");
        System.out.println(ageMap);
        assertEquals(2, ageMap.size());
        assertEquals(4, ageMap.get(17).size());
        assertEquals(3, ageMap.get(18).size());
        Map<Integer, List<User>> ageMap2 = ObjectUtil.toMapList(list, User::getAge);
        System.out.println(ageMap2);
        assertEquals(2, ageMap2.size());
        assertEquals(4, ageMap.get(17).size());
        assertEquals(3, ageMap.get(18).size());

        Map<Integer, List<String>> ageMap3 = ObjectUtil.toMapList(list, "age", "name");
        System.out.println(ageMap3);
        assertEquals(2, ageMap3.size());
        assertEquals(4, ageMap3.get(17).size());
        assertEquals(3, ageMap3.get(18).size());
        Map<Integer, List<String>> ageMap4 = ObjectUtil.toMapList(list, User::getAge, User::getName);
        System.out.println(ageMap4);
        assertEquals(2, ageMap4.size());
        assertEquals(4, ageMap4.get(17).size());
        assertEquals(3, ageMap4.get(18).size());

        Map<Integer, List<String>> ageMap5 = ObjectUtil.toMapList(list, "age", "name", true);
        System.out.println(ageMap5);
        assertEquals(2, ageMap5.size());
        assertEquals(3, ageMap5.get(17).size());
        assertEquals(3, ageMap5.get(18).size());
        Map<Integer, List<String>> ageMap6 = ObjectUtil.toMapList(list, User::getAge, User::getName, true);
        System.out.println(ageMap6);
        assertEquals(2, ageMap6.size());
        assertEquals(3, ageMap6.get(17).size());
        assertEquals(3, ageMap6.get(18).size());

        Map<Integer, List<Integer>> ageMap7 = ObjectUtil.toMapList(list, "age", "studentId", true);
        System.out.println(ageMap7);
        assertEquals(2, ageMap7.size());
        assertEquals(4, ageMap7.get(17).size());
        assertEquals(3, ageMap7.get(18).size());
        Map<Integer, List<Integer>> ageMap8 = ObjectUtil.toMapList(list, User::getAge, User::getStudentId, true);
        System.out.println(ageMap8);
        assertEquals(2, ageMap8.size());
        assertEquals(4, ageMap8.get(17).size());
        assertEquals(3, ageMap8.get(18).size());
    }
}
