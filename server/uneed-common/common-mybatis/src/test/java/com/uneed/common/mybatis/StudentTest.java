package com.uneed.common.mybatis;

import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.mybatis.entity.Student;
import com.uneed.common.mybatis.service.StudentService;
import com.uneed.common.mybatis.service.impl.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:mysql/spring-mysql.xml"})
@Slf4j
public class StudentTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TestData testData;

    /**
     * 清理所有数据数据
     */
    @Test
    @Order(0)
    public void clearAll() {
        TimeMeter meter = new TimeMeter();
        //清理所有数据
        testData.clearAll();
        log.warn(" >>>>>>>> test clear all finished. time=[{}]ms", meter.sign());
    }

    /**
     * 测试大数据的批量新增100、1000、10000、100000所用时间，数据准确性
     */
    @Test
    @Order(1)
    public void testInsertBatch() {
        TimeMeter meter = new TimeMeter();
        int size = 100;
        List<Student> students = testData.buildDate(size, true, "a");
        meter.sign("b-100");
        //批量新增100
        int r100 = studentService.insertBatch(students);
        meter.sign("e-100");
        Assertions.assertEquals(r100, size);

        size = 1000;
        students = testData.buildDate(size, true, "a");
        meter.sign("b-1000");
        //批量新增1000
        int r1000 = studentService.insertBatch(students);
        meter.sign("e-1000");
        Assertions.assertEquals(r1000, size);

        size = 10000;
        students = testData.buildDate(size, true, "a");
        meter.sign("b-10000");
        //批量新增10000
        int r10000 = studentService.insertBatch(students);
        meter.sign("e-10000");
        Assertions.assertEquals(r10000, size);

//        size = 100000;
//        students = testData.buildDate(size, true, "a");
//        meter.sign("b-100000");
//        //批量新增10000
//        int r100000 = studentService.insertBatch(students);
//        meter.sign("e-100000");
//        Assertions.assertEquals(r100000, size);
//        meter.sign("step1");

        size = 100;
        students = testData.buildDate(size, false, "a");
        meter.sign("bid-100");
        //批量新增100
        int rid100 = studentService.insertBatch(students);
        meter.sign("eid-100");
        Assertions.assertEquals(rid100, size);

        size = 1000;
        students = testData.buildDate(size, false, "a");
        meter.sign("bid-1000");
        //批量新增1000
        int rid1000 = studentService.insertBatch(students);
        meter.sign("eid-1000");
        Assertions.assertEquals(rid1000, size);

        size = 10000;
        students = testData.buildDate(size, false, "a");
        meter.sign("bid-10000");
        //批量新增10000
        int rid10000 = studentService.insertBatch(students);
        meter.sign("eid-10000");
        Assertions.assertEquals(rid10000, size);

//        size = 100000;
//        students = testData.buildDate(size, false, "a");
//        meter.sign("bid-100000");
//        //批量新增10000
//        int rid100000 = studentService.insertBatch(students);
//        meter.sign("eid-100000");
//        Assertions.assertEquals(rid100000, size);
//        meter.sign("step2");


        log.warn(" >>>>>>>> test insert batch for 100 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("", "b-100"), meter.sign("b-100", "e-100"), r100);
        log.warn(" >>>>>>>> test insert batch for 1000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("e-100", "b-1000"), meter.sign("b-1000", "e-1000"), r1000);
        log.warn(" >>>>>>>> test insert batch for 10000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("e-1000", "b-10000"), meter.sign("b-10000", "e-10000"), r10000);
//        log.warn(" >>>>>>>> test insert batch for 100000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
//                .sign("e-10000", "b-100000"), meter.sign("b-100000", "e-100000"), r100000);
        log.warn("total : " + meter.sign("step1") + "ms");
        System.out.println("====================================================================================");
        log.warn(" >>>>>>>> test insert batch for id 100 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("step1", "bid-100"), meter.sign("bid-100", "eid-100"), rid100);
        log.warn(" >>>>>>>> test insert batch for id 1000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("eid-100", "bid-1000"), meter.sign("bid-1000", "eid-1000"), rid1000);
        log.warn(" >>>>>>>> test insert batch for id 10000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("eid-1000", "bid-10000"), meter.sign("bid-10000", "eid-10000"), rid10000);
//        log.warn(" >>>>>>>> test insert batch for id 100000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
//                .sign("eid-10000", "bid-100000"), meter.sign("bid-100000", "eid-100000"), rid100000);
        log.warn("total : " + meter.sign("step1", "step2") + "ms");
    }

    /**
     * 测试大数据的批量修改100、1000、10000、100000所用时间，数据准确性
     */
    @Test
    @Order(2)
    public void testUpdateBatch() {
        TimeMeter meter = new TimeMeter();
        int size = 100;
        List<Student> students = testData.buildDate(size, false, "u");
        meter.sign("bid-100");
        //批量新增100
        int rid100 = studentService.updateBatch(students);
        meter.sign("eid-100");
        Assertions.assertEquals(rid100, size);

        size = 1000;
        students = testData.buildDate(size, false, "u");
        meter.sign("bid-1000");
        //批量新增1000
        int rid1000 = studentService.updateBatch(students);
        meter.sign("eid-1000");
        Assertions.assertEquals(rid1000, size);

        size = 10000;
        students = testData.buildDate(size, false, "u");
        meter.sign("bid-10000");
        //批量新增10000
        int rid10000 = studentService.updateBatch(students);
        meter.sign("eid-10000");
        Assertions.assertEquals(rid10000, size);

//        size = 100000;
//        students = testData.buildDate(size, false, "u");
//        meter.sign("bid-100000");
//        //批量新增10000
//        int rid100000 = studentService.update(students);
//        meter.sign("eid-100000");
//        Assertions.assertEquals(rid100000, size);
        meter.sign("step1");

        log.warn(" >>>>>>>> test update batch for id 100 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("", "bid-100"), meter.sign("bid-100", "eid-100"), rid100);
        log.warn(" >>>>>>>> test update batch for id 1000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("eid-100", "bid-1000"), meter.sign("bid-1000", "eid-1000"), rid1000);
        log.warn(" >>>>>>>> test update batch for id 10000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("eid-1000", "bid-10000"), meter.sign("bid-10000", "eid-10000"), rid10000);
//        log.warn(" >>>>>>>> test update batch for id 100000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
//                .sign("eid-10000", "bid-100000"), meter.sign("bid-100000", "eid-100000"), rid100000);
        log.warn("total : " + meter.sign("", "step1"));
    }

    /**
     * 测试大数据的批量新增或修改100、1000、10000、100000所用时间，数据准确性
     */
    @Test
    @Order(3)
    public void testInsertOrUpdateBatch() {
        TimeMeter meter = new TimeMeter();
        int size = 200;
        List<Student> students = testData.buildDate(size, false, "au");
        meter.sign("bid-200");
        //批量新增100
        int rid200 = studentService.insertOrUpdateBatch(students);
        meter.sign("eid-200");
        Assertions.assertEquals(rid200, size);

        size = 2000;
        students = testData.buildDate(size, false, "au");
        meter.sign("bid-2000");
        //批量新增1000
        int rid2000 = studentService.insertOrUpdateBatch(students);
        meter.sign("eid-2000");
        Assertions.assertEquals(rid2000, size);

        size = 20000;
        students = testData.buildDate(size, false, "au");
        meter.sign("bid-20000");
        //批量新增10000
        int rid20000 = studentService.insertOrUpdateBatch(students);
        meter.sign("eid-20000");
        Assertions.assertEquals(rid20000, size);

//        size = 200000;
//        students = testData.buildDate(size, false, "au");
//        meter.sign("bid-200000");
//        //批量新增10000
//        int rid200000 = studentService.insertOrUpdate(students);
//        meter.sign("eid-200000");
//        Assertions.assertEquals(rid200000, size);
        meter.sign("step1");

        log.warn(" >>>>>>>> test insert or update batch for id 200 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("", "bid-200"), meter.sign("bid-200", "eid-200"), rid200);
        log.warn(" >>>>>>>> test insert or update batch for id 2000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("eid-200", "bid-2000"), meter.sign("bid-2000", "eid-2000"), rid2000);
        log.warn(" >>>>>>>> test insert or update batch for id 20000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
                .sign("eid-2000", "bid-20000"), meter.sign("bid-20000", "eid-20000"), rid20000);
//        log.warn(" >>>>>>>> test insert or update batch for id 200000 finished. build=[{}]ms, execute=[{}]ms, result={}", meter
//                .sign("eid-20000", "bid-200000"), meter.sign("bid-200000", "eid-200000"), rid200000);
        log.warn("total : " + meter.sign("", "step1") + "ms");
    }

    /**
     * 测试集合过滤
     */
    @Test
    @Order(4)
    public void testListFilter() {
        List<Student> filterList = testData.buildFilterDate();
        List<Student> list = Lists.newArrayList();
        log.warn("list : " + list.size() + ", filterList : " + filterList.size());
        Assertions.assertEquals(list.size(), 0);
        Assertions.assertEquals(filterList.size(), 5);
        //过滤a的数据，并追加到list中
        filterList.stream().filter(s -> s.getStuCode().contains("a")).forEach(list::add);
        log.warn("list : " + list.size() + ", filterList : " + filterList.size());
        Assertions.assertEquals(list.size(), 2);
        Assertions.assertEquals(filterList.size(), 5);
        //删除a的数据
        filterList.removeIf(s -> s.getStuCode().contains("a"));
        log.warn("list : " + list.size() + ", filterList : " + filterList.size());
        Assertions.assertEquals(list.size(), 2);
        Assertions.assertEquals(filterList.size(), 3);
    }

    /**
     * 测试大数据in查询，大于1000的情况
     */
    @Test
    @Order(5)
    public void testQueryIn() {
        TimeMeter meter = new TimeMeter();
        int start = 100;
        int end = 110;
//        List<Long> ids = testData.buildIdList(start, end);
        //批量查询size=10
//        List<Student> l10 = studentService.listByIds(ids);
//        Assertions.assertEquals(l10.size(), 10);
        meter.sign("s-10");

        start = 100;
        end = 200;
//        ids = testData.buildIdList(start, end);
        //批量查询size=100
//        List<Student> l100 = studentService.listByIds(ids);
//        Assertions.assertEquals(l100.size(), 100);
        meter.sign("s-100");

        start = 1000;
        end = 1500;
//        ids = testData.buildIdList(start, end);
        //批量查询size=500
//        List<Student> l500 = studentService.listByIds(ids);
//        Assertions.assertEquals(l500.size(), 500);
        meter.sign("s-500");

        start = 1000;
        end = 2000;
//        ids = testData.buildIdList(start, end);
        //批量查询size=1000
//        List<Student> l1000 = studentService.listByIds(ids);
//        Assertions.assertEquals(l1000.size(), 1000);
        meter.sign("s-1000");

        start = 1000;
        end = 3000;
//        ids = testData.buildIdList(start, end);
        //批量查询size=2000
//        List<Student> l2000 = studentService.listByIds(ids);
//        Assertions.assertEquals(l2000.size(), 2000);
        meter.sign("s-2000");

        start = 10000;
        end = 20000;
//        ids = testData.buildIdList(start, end);
        //批量查询size=10000
//        List<Student> l10000 = studentService.listByIds(ids);
//        Assertions.assertEquals(l10000.size(), 10000);
        meter.sign("s-10000");

        start = 10000;
        end = 30000;
//        ids = testData.buildIdList(start, end);
        //批量查询size=20000
//        List<Student> l20000 = studentService.listByIds(ids);
//        Assertions.assertEquals(l20000.size(), 20000);
        meter.sign("s-20000");

//        log.warn(" >>>>>>>> test query in for size 10 finished. time=[{}]ms, result={}", meter.sign("s-10"), l10.size());
//        log.warn(" >>>>>>>> test query in for size 100 finished. time=[{}]ms, result={}", meter.sign("s-10", "s-100"), l100
//                .size());
//        log.warn(" >>>>>>>> test query in for size 500 finished. time=[{}]ms, result={}", meter.sign("s-100", "s-500"), l500
//                .size());
//        log.warn(" >>>>>>>> test query in for size 1000 finished. time=[{}]ms, result={}", meter.sign("s-500", "s-1000"), l1000
//                .size());
//        log.warn(" >>>>>>>> test query in for size 2000 finished. time=[{}]ms, result={}", meter.sign("s-1000", "s-2000"), l2000
//                .size());
//        log.warn(" >>>>>>>> test query in for size 10000 finished. time=[{}]ms, result={}", meter
//                .sign("s-2000", "s-10000"), l10000.size());
//        log.warn(" >>>>>>>> test query in for size 20000 finished. time=[{}]ms, result={}", meter
//                .sign("s-10000", "s-20000"), l20000.size());
//        log.warn("total : " + meter.sign("", "step1") + "ms");
    }

    /**
     * 测试大数据in查询，大于1000的情况
     */
    @Test
    @Order(6)
    public void testQueryInConditions() {
        TimeMeter meter = new TimeMeter();
        int start = 100;
        int end = 110;
//        List<Long> ids = testData.buildIdList(start, end);
//        //批量查询size=10
//        QueryWrapper<Student> wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l10 = studentService.list(wrapper);
//        Assertions.assertEquals(l10.size(), 10);
//        meter.sign("s-10");
//
//        start = 100;
//        end = 200;
//        ids = testData.buildIdList(start, end);
//        //批量查询size=100
//        wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l100 = studentService.list(wrapper);
//        Assertions.assertEquals(l100.size(), 100);
//        meter.sign("s-100");
//
//        start = 1000;
//        end = 1500;
//        ids = testData.buildIdList(start, end);
//        //批量查询size=500
//        wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l500 = studentService.list(wrapper);
//        Assertions.assertEquals(l500.size(), 500);
//        meter.sign("s-500");
//
//        start = 1000;
//        end = 2000;
//        ids = testData.buildIdList(start, end);
//        //批量查询size=1000
//        wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l1000 = studentService.list(wrapper);
//        Assertions.assertEquals(l1000.size(), 1000);
//        meter.sign("s-1000");
//
//        start = 1000;
//        end = 3000;
//        ids = testData.buildIdList(start, end);
//        //批量查询size=2000
//        wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l2000 = studentService.list(wrapper);
//        Assertions.assertEquals(l2000.size(), 2000);
//        meter.sign("s-2000");
//
//        start = 10000;
//        end = 20000;
//        ids = testData.buildIdList(start, end);
//        //批量查询size=10000
//        wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l10000 = studentService.list(wrapper);
//        Assertions.assertEquals(l10000.size(), 10000);
//        meter.sign("s-10000");
//
//        start = 10000;
//        end = 30000;
//        ids = testData.buildIdList(start, end);
//        //批量查询size=20000
//        wrapper = new QueryWrapper<>();
//        Conditions.setCondition(wrapper, Keyword.IN, "id", ids);
//        List<Student> l20000 = studentService.list(wrapper);
//        Assertions.assertEquals(l20000.size(), 20000);
//        meter.sign("s-20000");
//
//        log.warn(" >>>>>>>> test query in for size 10 finished. time=[{}]ms, result={}", meter.sign("s-10"), l10.size());
//        log.warn(" >>>>>>>> test query in for size 100 finished. time=[{}]ms, result={}", meter.sign("s-10", "s-100"), l100
//                .size());
//        log.warn(" >>>>>>>> test query in for size 500 finished. time=[{}]ms, result={}", meter.sign("s-100", "s-500"), l500
//                .size());
//        log.warn(" >>>>>>>> test query in for size 1000 finished. time=[{}]ms, result={}", meter.sign("s-500", "s-1000"), l1000
//                .size());
//        log.warn(" >>>>>>>> test query in for size 2000 finished. time=[{}]ms, result={}", meter.sign("s-1000", "s-2000"), l2000
//                .size());
//        log.warn(" >>>>>>>> test query in for size 10000 finished. time=[{}]ms, result={}", meter
//                .sign("s-2000", "s-10000"), l10000.size());
//        log.warn(" >>>>>>>> test query in for size 20000 finished. time=[{}]ms, result={}", meter
//                .sign("s-10000", "s-20000"), l20000.size());
//        log.warn("total : " + meter.sign("", "step1") + "ms");
    }
}
