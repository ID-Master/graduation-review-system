package com.uneed.common.core.bean;

import com.uneed.common.core.Editor;
import com.uneed.common.core.annotation.TestAnnotation;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.entity.Demo;
import com.uneed.common.core.entity.IntList;
import com.uneed.common.core.entity.User;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.Converter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * BeanUtil Tester.
 *
 * @author huangad@coracle.com
 * @date 10/15/2019
 */
@Slf4j
public class BeanUtilTest {

    @Before
    public void before() {
        //TODO: Test before goes here...
    }

    @After
    public void after() {
        //TODO: Test after goes here...
    }

    /**
     * 实体类数据拷贝
     * Method: copy(Object orig, Object dest)
     */
    @Test
    public void testCopyForOrigDest() {
        User user1 = new User();
        user1.setAge(54);
        user1.setName("忘忧草涛");
        User user2 = new User();
        BeanUtil.copy(user1, user2);

        Integer integer = 54;

        assertEquals(integer, user2.getAge());
        assertEquals("忘忧草涛", user2.getName());

    }

    /**
     * 实体类数据拷贝
     * Method: copy(Object orig, Object dest)
     */
    @Test
    public void testCopyForNewOrigDest() {
        User user1 = new User();
        user1.setAge(54);
        user1.setName("忘忧草涛");
        User user2 = BeanUtil.copyNew(user1, User.class);
        Integer integer = 54;

        assertEquals(integer, user2.getAge());
        assertEquals("忘忧草涛", user2.getName());

        List<User> users = BeanUtil.copyNew(Lists.newArrayList(user1, user2), User.class);
        assertEquals(integer, users.get(0).getAge());
        assertEquals(integer, users.get(1).getAge());
        assertEquals("忘忧草涛", users.get(0).getName());
        assertEquals("忘忧草涛", users.get(1).getName());
    }

    /**
     * 实体类数据拷贝，该方法可以实现自定义规则转换
     * Method: copy(Object orig, Object dest, Converter converter)
     */
    @Test
    public void testCopyForOrigDestConverter() {
        User user1 = new User();
        user1.setAge(54);
        user1.setName("忘忧草涛");
        User user2 = new User();

        Converter converter = (o, aClass, o1) -> {
            if (String.class == aClass) {
                o1 = o + "--";
                return o1;
            }
            return o;
        };
        BeanUtil.copy(user1, user2, converter);

        Integer integer = 54;
        assertEquals(integer, user2.getAge());
        assertEquals("忘忧草涛--", user2.getName());


    }

    /**
     * 序列化拷贝对象，先将源对象序列化成json对象，再将json转换为目标类型对象
     * <p>
     * Method: serializedCopy(Object orig, Class<T> destClass, String... ignores)
     */
    @Test
    public void testSerializedCopy() {
        User user = new User();
        user.setStudentId(1);
        user.setName("张三");
        user.setAge(18);
        Demo demo = BeanUtil.serializedCopy(user, Demo.class);
        assertEquals(demo.getStudentId(), user.getStudentId());
        assertEquals(demo.getName(), user.getName());
        assertEquals(demo.getAge(), user.getAge());
        log.warn("===============>> user=" + user);
        log.warn("===============>> demo=" + demo);
        Demo demo2 = BeanUtil.serializedCopy(user, Demo.class, "name", "age");
        assertEquals(demo2.getStudentId(), user.getStudentId());
        assertNull(demo2.getName());
        assertNull(demo2.getAge());
        log.warn("=========================================");
        log.warn("===============>> user=" + user);
        log.warn("===============>> demo2=" + demo2);
    }

    /**
     * 批量序列化拷贝对象，先将源对象集合序列化成json对象，再将json转换为目标类型对象集合
     * <p>
     * Method: serializedCopy(Object orig, Class<T> destClass, String... ignores)
     */
    @Test
    public void testSerializedCopyList() {
        List<User> users = buildUser(5);
        List<Demo> demos = BeanUtil.serializedCopy(users, Demo.class);
        assertEquals(demos.get(3).getStudentId(), users.get(3).getStudentId());
        assertEquals(demos.get(3).getName(), users.get(3).getName());
        assertEquals(demos.get(3).getAge(), users.get(3).getAge());
        log.warn("===============>> users=" + users);
        log.warn("===============>> demos=" + demos);

        List<Demo> demos2 = BeanUtil.serializedCopy(users, Demo.class, "name", "age");
        assertEquals(demos2.get(3).getStudentId(), users.get(3).getStudentId());
        assertNull(demos2.get(3).getName());
        assertNull(demos2.get(3).getAge());
        log.warn("=========================================");
        log.warn("===============>> users=" + users);
        log.warn("===============>> demos2=" + demos2);
    }

    /**
     * 测试批量拷贝效率
     */
    @Test
    public void testCopyListEfficiency() {
        long time = System.currentTimeMillis();
        int size = 1000000;
        List<User> users = buildUser(size);
        long time2 = System.currentTimeMillis();
        log.warn("===============>> build, size=" + users.size() + ", time=" + (time2 - time));
        List<Demo> demos = BeanUtil.copyNew(users, Demo.class);
        long time3 = System.currentTimeMillis();
        log.warn("===============>> copy, size=" + demos.size() + ", time=" + (time3 - time2));
    }

    /**
     * 测试批量拷贝效率
     */
    @Test
    public void testSerializedCopyListEfficiency() {
        long time = System.currentTimeMillis();
        int size = 1000000;
        List<User> users = buildUser(size);
        long time2 = System.currentTimeMillis();
        log.warn("===============>> build, size=" + users.size() + ", time=" + (time2 - time));
        List<Demo> demos = BeanUtil.serializedCopy(users, Demo.class);
        long time3 = System.currentTimeMillis();
        log.warn("===============>> serialized copy, size=" + demos.size() + ", time=" + (time3 - time2));
    }

    private List<User> buildUser(int size) {
        List<User> list = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            User user = new User();
            user.setStudentId(i + 1);
            user.setName("学生" + user.getStudentId());
            user.setAge(18);
            list.add(user);
        }
        return list;
    }

    /**
     * 测试去空格
     * Method: trimProperty(Class<?> clazz)
     */
    @Test
    public void testTrimProperty() {
        Demo demo = new Demo();
        demo.setStudentId(1);
        demo.setCode(" a, b ");
        demo.setName(" 张三");
        demo.setAge(18);
        BeanUtil.trimProperty(demo);
        log.warn("test trim property 1, user={}", demo);
        assertEquals("a, b", demo.getCode());
        assertEquals("张三", demo.getName());

        demo.setCode(" a, b ");
        demo.setName(" 张三");
        BeanUtil.trimProperty(demo, "code");
        log.warn("test trim property 2, user={}", demo);
        assertEquals(" a, b ", demo.getCode());
        assertEquals("张三", demo.getName());

        demo.setCode(" a, b ");
        demo.setName(" 张三");
        BeanUtil.trimProperty(demo, "code", "name");
        log.warn("test trim property 3, user={}", demo);
        assertEquals(" a, b ", demo.getCode());
        assertEquals(" 张三", demo.getName());

    }

    /**
     * 判断是否为Bean
     * Method: isBean(Class<?> clazz)
     */
    @Test
    public void testIsBean() {
        assertTrue(BeanUtil.isBean(User.class));
        assertFalse(BeanUtil.isBean(String.class));

    }

    /**
     * 判断是否有Setter方法
     * Method: hasSetter(Class<?> clazz)
     */
    @Test
    public void testHasSetter() {
        assertTrue(BeanUtil.isBean(User.class));
        assertFalse(BeanUtil.isBean(String.class));
    }

    /**
     * 判断是否为Bean对象<br>
     * 判定方法是是否存在只有一个参数的getXXX方法
     * Method: hasGetter(Class<?> clazz)
     */
    @Test
    public void testHasGetter() {
        assertTrue(BeanUtil.hasGetter(User.class));
        assertFalse(BeanUtil.hasGetter(Number.class));
    }

    /**
     * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
     * Method: beanToMap(Object bean)
     */
    @Test
    public void testBeanToMapBean() {
        User bean = new User();
        Integer integer = 18;
        Integer id = 13231;

        bean.setAge(integer);
        bean.setStudentId(id);

        Map<String, Object> map = BeanUtil.toMap(bean);
        assertEquals(integer, map.get("age"));
        assertTrue(map.containsKey("name"));
        assertEquals(id, map.get("studentId"));
    }

    /**
     * 对象转Map
     * Method: beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue)
     */
    @Test
    public void testBeanToMapForBeanIsToUnderlineCaseIgnoreNullValue() {
        User bean = new User();
        Integer integer = 18;
        Integer id = 13231;

        bean.setAge(integer);
        bean.setStudentId(id);

        Map<String, Object> map = BeanUtil.toMap(bean, true, true);
        assertEquals(integer, map.get("age"));
        assertFalse(map.containsKey("name"));
        assertEquals(id, map.get("student_id"));
    }

    /**
     * 对象转Map
     * Method: beanToMap(Object bean, Map<String, Object> targetMap, final boolean isToUnderlineCase, boolean ignoreNullValue)
     */
    @Test
    public void testBeanToMapForBeanTargetMapIsToUnderlineCaseIgnoreNullValue() {
        User bean = new User();
        Integer integer = 18;
        Integer id = 13231;

        bean.setAge(integer);
        bean.setStudentId(id);
        Map<String, Object> targetMap = new HashMap<>();

        Map<String, Object> map = BeanUtil.toMap(bean, targetMap, true, true);
        assertEquals(integer, map.get("age"));
        assertFalse(map.containsKey("name"));
        assertEquals(id, map.get("student_id"));

    }

    /**
     * 对象转Map
     * Method: beanToMap(Object bean, Map<String, Object> targetMap, boolean ignoreNullValue, Editor<String> keyEditor)
     */
    @Test
    public void testBeanToMapForBeanTargetMapIgnoreNullValueKeyEditor() {
        User bean = new User();
        Integer integer = 18;
        Integer id = 13231;

        bean.setAge(integer);
        bean.setStudentId(id);

        Map<String, Object> targetMap = new HashMap<>();
        Editor<String> keyEditor = s -> {
            if ("studentId".equals(s)) {
                return null;
            } else {
                return s + "--";
            }
        };

        Map<String, Object> map = BeanUtil.toMap(bean, targetMap, true, keyEditor);
        assertEquals(integer, map.get("age--"));
        assertFalse(map.containsKey("name"));
        assertNull(map.get("studentId"));
    }

    /**
     * 获取Bean描述信息
     * Method: getBeanDesc(Class<?> clazz)
     */
    @Test
    public void testGetBeanDesc() {

        BeanDesc beanDesc = BeanUtil.getBeanDesc(User.class);
        assertEquals("com.uneed.common.core.entity.User", beanDesc.getName());
        assertEquals("User", beanDesc.getSimpleName());
    }

    /**
     * 获取Bean类的 getxxxx列表
     * Method: getReadMethod(Object bean)
     */
    @Test
    public void testGetReadMethodBean() {
        User bean = new User();

        List<Method> readMethod = BeanUtil.getReadMethod(bean);

        assertEquals("getStudentId", readMethod.get(0).getName());
        assertEquals("getName", readMethod.get(1).getName());
        assertEquals("getAge", readMethod.get(2).getName());
    }

    /**
     * 获取Bean类的 setxxx方法列表
     * Method: getWriteMethod(Object bean)
     */
    @Test
    public void testGetWriteMethodBean() {
        User bean = new User();

        List<Method> writeMethod = BeanUtil.getWriteMethod(bean);

        assertEquals("setStudentId", writeMethod.get(0).getName());
        assertEquals("setName", writeMethod.get(1).getName());
        assertEquals("setAge", writeMethod.get(2).getName());
    }

    /**
     * 获取Bean类的 getxxxx列表
     * Method: getReadMethod(Class<?> clazz)
     */
    @Test
    public void testGetReadMethodClazz() {
        List<Method> readMethod = BeanUtil.getReadMethod(User.class);

        assertEquals("getStudentId", readMethod.get(0).getName());
        assertEquals("getName", readMethod.get(1).getName());
        assertEquals("getAge", readMethod.get(2).getName());
    }

    /**
     * 获取Bean类的 setxxxx列表
     * Method: getWriteMethod(Class<?> clazz)
     */
    @Test
    public void testGetWriteMethodClazz() {
        List<Method> writeMethod = BeanUtil.getWriteMethod(User.class);

        assertEquals("setStudentId", writeMethod.get(0).getName());
        assertEquals("setName", writeMethod.get(1).getName());
        assertEquals("setAge", writeMethod.get(2).getName());
    }

    /**
     * Method: getReadAndWriteMethod(Class<?> clazz)
     */
    @Test
    public void testGetReadAndWriteMethodClazz() {
        List<Method> readAndWriteMethod = BeanUtil.getReadAndWriteMethod(User.class);

        assertEquals("getStudentId", readAndWriteMethod.get(0).getName());
        assertEquals("setStudentId", readAndWriteMethod.get(1).getName());
        assertEquals("getName", readAndWriteMethod.get(2).getName());
        assertEquals("setName", readAndWriteMethod.get(3).getName());
        assertEquals("getAge", readAndWriteMethod.get(4).getName());
        assertEquals("setAge", readAndWriteMethod.get(5).getName());

    }

    /**
     * Method: getReadAndWriteMethod(Object bean)
     */
    @Test
    public void testGetReadAndWriteMethodBean() {
        User user = new User();
        List<Method> readAndWriteMethod = BeanUtil.getReadAndWriteMethod(user);

        assertEquals("getStudentId", readAndWriteMethod.get(0).getName());
        assertEquals("setStudentId", readAndWriteMethod.get(1).getName());
        assertEquals("getName", readAndWriteMethod.get(2).getName());
        assertEquals("setName", readAndWriteMethod.get(3).getName());
        assertEquals("getAge", readAndWriteMethod.get(4).getName());
        assertEquals("setAge", readAndWriteMethod.get(5).getName());
    }

    /**
     * 获取类属性对象
     * Method: getField(Object bean, String fieldName)
     */
    @Test
    public void testGetFieldForBeanFieldName() {
        User user = new User();
        String fieldName = "name";

        assertEquals(fieldName, BeanUtil.getField(user, fieldName).getName());
    }

    /**
     * 获取类属性对象
     * Method: getField(Class<?> clazz, String fieldName)
     */
    @Test
    public void testGetFieldForClazzFieldName() {
        String fieldName = "name";
        assertEquals(fieldName, BeanUtil.getField(User.class, fieldName).getName());
    }

    /**
     * 获取类属性对象列表
     * Method: getFields(Object bean)
     */
    @Test
    public void testGetFieldsBean() {
        User user = new User();
        List<Field> fields = BeanUtil.getFields(user);
        assertEquals("name", fields.get(0).getName());
        assertEquals("age", fields.get(1).getName());
        assertEquals("studentId", fields.get(2).getName());
    }

    /**
     * 获取类属性对象列表 是否忽略父类的属性对象
     * Method: getFields(Object bean, boolean superclass)
     */
    @Test
    public void testGetFieldsForBeanSuperclass() {
        ArrayList arrayList = new ArrayList();
        List<Field> fields = BeanUtil.getFields(arrayList, true);
        //忽略父类的属性对象
        assertEquals("serialVersionUID", fields.get(0).getName());
        assertEquals("DEFAULT_CAPACITY", fields.get(1).getName());
        assertEquals("MAX_ARRAY_SIZE", fields.get(6).getName());

        //不忽略父类的属性对象
        List<Field> fieldsFalse = BeanUtil.getFields(ArrayList.class, false);

        assertEquals("serialVersionUID", fieldsFalse.get(0).getName());
        assertEquals("DEFAULT_CAPACITY", fieldsFalse.get(1).getName());
        assertEquals("MAX_ARRAY_SIZE", fieldsFalse.get(6).getName());

        assertEquals("modCount", fieldsFalse.get(7).getName());
    }

    /**
     * 获取类属性对象列表
     * Method: getFields(Class<?> clazz)
     */
    @Test
    public void testGetFieldsClazz() {
        List<Field> fields = BeanUtil.getFields(User.class);
        assertEquals("name", fields.get(0).getName());
        assertEquals("age", fields.get(1).getName());
        assertEquals("studentId", fields.get(2).getName());
    }

    /**
     * 获取类属性对象列表 是否忽略父类属性
     * Method: getFields(Class<?> clazz, boolean superclass)
     */
    @Test
    public void testGetFieldsForClazzSuperclass() {
        List<Field> fields = BeanUtil.getFields(ArrayList.class, true);
        //忽略父类的属性对象
        assertEquals("serialVersionUID", fields.get(0).getName());
        assertEquals("DEFAULT_CAPACITY", fields.get(1).getName());
        assertEquals("MAX_ARRAY_SIZE", fields.get(6).getName());

        //不忽略父类的属性对象
        List<Field> fieldsFalse = BeanUtil.getFields(ArrayList.class, false);

        assertEquals("serialVersionUID", fieldsFalse.get(0).getName());
        assertEquals("DEFAULT_CAPACITY", fieldsFalse.get(1).getName());
        assertEquals("MAX_ARRAY_SIZE", fieldsFalse.get(6).getName());

        assertEquals("modCount", fieldsFalse.get(7).getName());
    }

    /**
     * 往bean对象的属性注入对应的值
     * Method: setProperty(Object bean, String property, Object value)
     */
    @Test
    public void testSetProperty() {
        User user = new User();
        BeanUtil.setProperty(user, "name", "忘忧草涛");
        assertEquals("忘忧草涛", user.getName());
    }

    /**
     * Method: getSuperClassActualType(Class clazz, int index)
     */
    @Test
    public void testGetSuperClassActualType() {
        Class clazz1 = ArrayList.class;
        Class clazz2 = String[].class;
        Class clazz3 = String.class;
        Class clazz4 = Object.class;
        Class clazz5 = IntList.class;
        System.out.println("TEST-----------------WARN-----------------------------------------");
        assertNull(BeanUtil.getSuperClassActualType(clazz1, 0));
        //assertEquals("java.lang.Object", BeanUtil.getSuperClassActualType(clazz2, 0).getName());
        //assertEquals("java.lang.Object", BeanUtil.getSuperClassActualType(clazz3, 0).getName());
        //assertEquals("java.lang.Object", BeanUtil.getSuperClassActualType(clazz4, 0).getName());
        //assertEquals("java.lang.Object", BeanUtil.getSuperClassActualType(clazz5, 1).getName());

        assertEquals("java.lang.Integer", BeanUtil.getSuperClassActualType(clazz5, 0).getName());


    }

    /**
     * Method: getProperty(Object bean, String property)
     */
    @Test
    public void testGetProperty() {
        User user = new User();
        BeanUtil.setProperty(user, "name", "忘忧草涛");
        assertEquals("忘忧草涛", BeanUtil.getProperty(user, "name"));
    }

    @Test
    public void testGetPropertyAnnotation() {
        TestAnnotation annotation = BeanUtil.getPropertyAnnotation(Demo.class, "code", TestAnnotation.class);
        assert annotation != null;
        assertEquals("张", annotation.name());

        TestAnnotation annotation2 = BeanUtil.getPropertyAnnotation(Demo.class, "name", TestAnnotation.class);
        assert annotation2 != null;
        assertEquals("李", annotation2.name());

        TestAnnotation annotation3 = BeanUtil.getPropertyAnnotation(Demo.class, "age", TestAnnotation.class);
        assert annotation3 != null;
        assertEquals("王-parent", annotation3.name());

        TestAnnotation annotation4 = BeanUtil.getPropertyAnnotation(Demo.class, "studentId", TestAnnotation.class);
        assertNull(annotation4);
    }

}
