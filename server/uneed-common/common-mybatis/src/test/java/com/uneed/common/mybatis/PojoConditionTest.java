package com.uneed.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.mybatis.condition.GeneralCondition;
import com.uneed.common.mybatis.entity.Student;
import com.uneed.common.mybatis.service.ClassesService;
import com.uneed.common.mybatis.service.StudentService;
import com.uneed.common.mybatis.service.TeacherService;
import com.uneed.common.mybatis.service.impl.TestData;
import com.uneed.common.mybatis.utils.Conditions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * need to describe something here.
 *
 * @author diablo
 * @date 2020/4/23
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:mysql/spring-mysql.xml"})
@Slf4j
public class PojoConditionTest {

    @Autowired
    private ClassesService classesService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

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
     * 初始化数据
     */
    @Test
    @Order(1)
    public void init() {
        //清理所有数据
        testData.clearAll();

        TimeMeter meter = new TimeMeter();
        //构建班级班级
        int rc = testData.initClasses();
        Assertions.assertEquals(rc, 6);
        meter.sign("c");
        //构建学生数据
        int rs = testData.initStudent();
        Assertions.assertEquals(rs, 300);
        meter.sign("s");
        //构建老师数据
        int rt = testData.initTeacher();
        Assertions.assertEquals(rt, 20);
        meter.sign("t");
        //构建老师班级数据
        int rtc = testData.initTeacherClass();
        Assertions.assertEquals(rtc, 50);
        meter.sign("tc");

        log.warn(" >>>>>>>> test init classes finished. time=[{}]ms, result={}", meter.sign("c"), rc);
        log.warn(" >>>>>>>> test init student finished. time=[{}]ms, result={}", meter.sign("c", "s"), rs);
        log.warn(" >>>>>>>> test init teacher finished. time=[{}]ms, result={}", meter.sign("s", "t"), rt);
        log.warn(" >>>>>>>> test init teacher classes finished. time=[{}]ms, result={}", meter.sign("t", "tc"), rtc);
    }

    /**
     * 测试count
     */
    @Test
    @Order(2)
    public void testCountCondition() {
        GeneralCondition condition = new GeneralCondition("1", "王");
        long count = studentService.countByCondition(condition);
        log.warn(" >>>>>>>> test count condition finished. result={}", count);
        long count2 = studentService.count(Conditions.queryWrapper(condition, Student.class));
        log.warn(" >>>>>>>> test count condition finished. result={}", count2);
        Assertions.assertEquals(count, count);
    }

    /**
     * 测试get
     */
    @Test
    @Order(3)
    public void testGetCondition() {
        GeneralCondition condition = new GeneralCondition("1", "王");
        Student student = studentService.getByCondition(condition);
        log.warn(" >>>>>>>> test get condition finished. result={}", student);
        Assertions.assertTrue(studentService.getOptByCondition(condition).isPresent());
        Student student2 = studentService.get(Conditions.queryWrapper(condition, Student.class));
        log.warn(" >>>>>>>> test get condition finished. result={}", student2);
        Assertions.assertEquals(student.getId(), student2.getId());
    }

    /**
     * 测试list
     */
    @Test
    @Order(4)
    public void testListCondition() {
        GeneralCondition condition = new GeneralCondition("1", "王");
        List<Student> students = studentService.listByCondition(condition);
        log.warn(" >>>>>>>> test list condition finished. result={}", students);
        List<Student> students2 = studentService.list(Conditions.queryWrapper(condition, Student.class));
        log.warn(" >>>>>>>> test list condition finished. result={}", students2);
        Assertions.assertEquals(students.size(), students2.size());
    }

    /**
     * 测试list
     */
    @Test
    @Order(5)
    public void testPageCondition() {
        GeneralCondition condition = new GeneralCondition("1", "王");
        Page<Student> page = studentService.pageByCondition(new Page<>(0, 1000), condition);
        log.warn(" >>>>>>>> test page condition finished. result={}", page);
        Page<Student> page2 = studentService.page(new Page<>(0, 1000), Conditions.queryWrapper(condition, Student.class));
        log.warn(" >>>>>>>> test page condition finished. result={}", page2);
        Assertions.assertEquals(page.getRecords().size(), page2.getRecords().size());
    }
}
