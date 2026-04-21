package com.uneed.common.core.lang;

import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.date.pattern.DatePattern;
import com.uneed.common.core.entity.Demo;
import com.uneed.common.core.text.JsonUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * EnumUtil Tester.
 *
 * @author diablo
 * @date 07/23/2018
 */
public class EnumUtilTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void before() {
        //TODO: Test before goes here... 
    }

    @After
    public void after() {
        //TODO: Test after goes here...
    }

    /**
     * Method: getEnum(@Nonnull Class<E> clazz, String name)
     */
    @Test
    public void testGetEnumForClazzName() {
        Enum<DatePattern> e1 = EnumUtil.getEnum(DatePattern.class, "DAY");
        assertEquals(DatePattern.DAY, e1);
        //忽略大小写测试
        Enum<DatePattern> e2 = EnumUtil.getEnum(DatePattern.class, "day");
        assertEquals(DatePattern.DAY, e2);
        Enum<DatePattern> e3 = EnumUtil.getEnum(DatePattern.class, "dAy");
        assertEquals(DatePattern.DAY, e3);
        //测试不存在的
        Enum<DatePattern> e4 = EnumUtil.getEnum(DatePattern.class, "day1");
        assertNull(e4);
    }

    /**
     * Method: getEnum(@Nonnull Class<E> clazz, int ordinal)
     */
    @Test
    public void testGetEnumForClazzOrdinal() {
        Enum<DatePattern> e1 = EnumUtil.getEnum(DatePattern.class, 0);
        assertEquals(DatePattern.YEAR, e1);
        Enum<DatePattern> e2 = EnumUtil.getEnum(DatePattern.class, 5);
        assertEquals(DatePattern.SECOND, e2);
        //测试不存在的
        Enum<DatePattern> e3 = EnumUtil.getEnum(DatePattern.class, 99);
        assertNull(e3);
    }

    /**
     * Method: getEnum(@Nonnull Class<E> clazz, @Nonnull String property, Object value)
     */
    @Test
    public void testGetEnumForClazzPropertyValue() {
        Enum<DatePattern> e1 = EnumUtil.getEnum(DatePattern.class, "pattern", "month");
        assertEquals(DatePattern.MONTH, e1);
        Enum<DatePattern> e2 = EnumUtil.getEnum(DatePattern.class, "description", "分");
        assertEquals(DatePattern.MINUTE, e2);
        //测试定制值不存在的
        Enum<DatePattern> e3 = EnumUtil.getEnum(DatePattern.class, "description", "test");
        assertNull(e3);
        //测试定制属性不存在的，先断言需要抛出的异常和异常信息
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Unknown property 'test' on class 'class com.uneed.common.core.date.pattern.DatePattern'");
        //执行会出现异常的方法
        EnumUtil.getEnum(DatePattern.class, "test", "day");
    }

    /**
     * Method: getEnum(@Nonnull Class<E> clazz, int ordinal)
     */
    @Test
    public void testCollection() {
        TimeMeter meter = new TimeMeter();
        List<Demo> list = buildList(100000);
        System.out.println("====== >>> 构建数据时长：" + meter.sign("build"));
        Demo demo = CollectionUtil.get(list, "code", "7000");
        System.out.println("------>> demo：" + JsonUtil.toJson(demo));
        Demo demo4 = CollectionUtil.get(list, Demo::getCode, "7000");
        System.out.println("------>> demo4：" + JsonUtil.toJson(demo4));
        System.out.println("------>> CollectionUtil.get 时长：" + meter.sign("build","get"));
        Demo demo1 = list.stream().filter(it -> it.getCode().equals("7000")).findAny().orElse(null);
        System.out.println("------>> demo1：" + JsonUtil.toJson(demo1));
        System.out.println("------>> stream 时长：" + meter.sign("get","stream"));
        Demo demo2 = list.parallelStream().filter(it -> it.getCode().equals("7000")).findAny().orElse(null);
        System.out.println("------>> demo2：" + JsonUtil.toJson(demo2));
        System.out.println("------>> parallelStream 时长：" + meter.sign("stream","parallelStream"));
    }

    /**
     * Method: getEnum(@Nonnull Class<E> clazz, int ordinal)
     */
    @Test
    public void testCollection2() {
        TimeMeter meter = new TimeMeter();
        List<Demo> list = buildList(10000);
        System.out.println("====== >>> 构建数据时长：" + meter.sign("build"));
        List<String> codes = CollectionUtil.getPropertyList(list, "code");
        System.out.println("------>> demoList：" + JsonUtil.toJson(codes));
        System.out.println("------>> CollectionUtil.getPropertyList 时长：" + meter.sign("build","get"));
        List<String>  codes1 = list.stream().map(Demo::getCode).collect(Collectors.toList());
        System.out.println("------>> demo1：" + JsonUtil.toJson(codes1));
        System.out.println("------>> stream 时长：" + meter.sign("get","stream"));
        List<String>  codes2 = list.parallelStream().map(Demo::getCode).collect(Collectors.toList());
        System.out.println("------>> demo2：" + JsonUtil.toJson(codes2));
        System.out.println("------>> parallelStream 时长：" + meter.sign("stream","parallelStream"));

    }



    /**
     * Method: getEnum(@Nonnull Class<E> clazz, int ordinal)
     */
    @Test
    public void testCollection3() {
        TimeMeter meter = new TimeMeter();
        List<Demo> list = buildList(10000);
        System.out.println("====== >>> 构建数据时长：" + meter.sign("build"));

        List<String> list1 = Lists.newArrayList();
        for(Demo demo : list){
            list1.add(demo.getCode());
        }
        System.out.println("------>> list1：" + JsonUtil.toJson(list1));
        System.out.println("------>> list1 size：" + list1.size());
        System.out.println("------>> get 时长：" + meter.sign("build","get"));

        List<String> list2 = Lists.newArrayList();
        list.forEach(it -> list2.add(it.getCode()));
        System.out.println("------>> list2：" + JsonUtil.toJson(list2));
        System.out.println("------>> list1 size：" + list2.size());
        System.out.println("------>> stream 时长：" + meter.sign("get","stream"));

        List<String> list3 = Collections.synchronizedList(new ArrayList<>());
        list.parallelStream().forEachOrdered(it -> list3.add(it.getCode()));
        System.out.println("------>> list3：" + JsonUtil.toJson(list3));
        System.out.println("------>> list1 size：" + list3.size());
        System.out.println("------>> parallelStream forEachOrdered 时长：" + meter.sign("stream","parallelStream"));

        List<String> list4 = list.parallelStream().map(Demo::getCode).collect(Collectors.toList());
        System.out.println("------>> list4：" + JsonUtil.toJson(list4));
        System.out.println("------>> list1 size：" + list4.size());
        System.out.println("------>> parallelStream map collect 时长：" + meter.sign("parallelStream","parallelStream2"));
    }

    private List<Demo> buildList(int size) {
        List<Demo> list = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            Demo demo = new Demo();
            demo.setStudentId(i + 1);
            demo.setAge(18);
            demo.setCode("demo" + (i + 1));
            demo.setCode((i + 1) + "");
            list.add(demo);
        }
        return list;
    }
}
