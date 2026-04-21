package com.uneed.common.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.enums.NestedType;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.DateUtil;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.condition.GeneralCondition;
import com.uneed.common.mybatis.condition.NestedCondition;
import com.uneed.common.mybatis.condition.StudentCondition;
import com.uneed.common.mybatis.condition.TeacherCondition;
import com.uneed.common.mybatis.entity.Classes;
import com.uneed.common.mybatis.entity.Student;
import com.uneed.common.mybatis.entity.Teacher;
import com.uneed.common.mybatis.mapper.StudentMapper;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.mybatis.page.Sort;
import com.uneed.common.mybatis.service.ClassesService;
import com.uneed.common.mybatis.service.StudentService;
import com.uneed.common.mybatis.service.TeacherService;
import com.uneed.common.mybatis.service.impl.TestData;
import com.uneed.common.mybatis.utils.Conditions;
import com.uneed.common.mybatis.utils.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.isEmpty;

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
public class ConditionTest {

    @Autowired
    private ClassesService classesService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentMapper studentMapper;

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
     * 测试修改值为空
     */
    @Test
    @Order(2)
    public void testUpdateCondition() {
        //获取id为1的学生
        Student student = studentService.getById("1");
        //普通方式，值不变
        student.setStuNamePy(null);
        studentService.update(student);
        student = studentService.getById("1");
        Assertions.assertFalse(isEmpty(student.getStuNamePy()));

        //方式一，调用：update(entity,false)
        student.setStuNamePy(null);
        studentService.updateForAll(student);
        student = studentService.getById("1");
        Assertions.assertTrue(isEmpty(student.getStuNamePy()));

        //方式二，使用mybatis-wrapper的方式
        UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", student.getId());
        wrapper.set("stu_sex", null);
        studentMapper.update(student, wrapper);
        student = studentService.getById("1");
        Assertions.assertTrue(isEmpty(student.getStuSex()));

        //方式三，使用mybatis-wrapper的方式
        wrapper = new UpdateWrapper<>();
        Conditions.setKeyCondition(wrapper, student);
        Conditions.setValue(wrapper, "enrolmentTime", null);
        studentMapper.update(student, wrapper);
        student = studentService.getById("1");
        Assertions.assertTrue(isEmpty(student.getEnrolmentTime()));

        //测试工具类判断是否超类
        Assertions.assertTrue(TableUtil.isSuperClass(SuperModel.class));
        Assertions.assertFalse(TableUtil.isSuperClass(Student.class));
    }

    /**
     * 测试wrapper链式操作
     */
    @Test
    @Order(3)
    public void testWrapperChain() {
        //获取一个数据，调用one方法时，存在多个结果，会抛TooManyResultsException异常
        Assertions.assertThrows(MyBatisSystemException.class,
                                () -> classesService.lambdaQuery().like(Classes::getClassCode, "A").one());
        //获取一个数据，使用service的get方法，存在多个结果，会取第一个
        Classes classes = classesService.get(new LambdaQueryWrapper<Classes>().like(Classes::getClassCode, "A"));
        Assertions.assertNotNull(classes);

        //链式查询
        List<Classes> list = classesService.query().like("class_code", "A").like("class_name", "一年级").list();
        Assertions.assertEquals(list.size(), 2);

        //链式查询，lambda方式
        list = classesService.lambdaQuery().like(Classes::getClassCode, "A").like(Classes::getClassName, "一年级").list();
        Assertions.assertEquals(list.size(), 2);

        //链式修改
        //链式修改的方式，不会调用超类的自动填充
        // UPDATE ud_classes SET class_name=? WHERE remove_flag=0 AND (class_name = ?)
        // 三年级，1班(String), 三年级1班(String)
        boolean result = classesService.update().set("class_name", "三年级，1班").eq("class_name", "三年级1班").update();
        Assertions.assertTrue(result);

        //链式修改，lambda方式
        //链式修改的方式，不会调用超类的自动填充
        // UPDATE ud_classes SET class_name=? WHERE remove_flag=0 AND (class_name = ?)
        // 三年级1班(String), 三年级，1班(String)
        result = classesService.lambdaUpdate().set(Classes::getClassName, "三年级1班").eq(Classes::getClassName, "三年级，1班").update();
        Assertions.assertTrue(result);
    }

    /**
     * 测试嵌套条件
     */
    @Test
    @Order(4)
    public void testNestedCondition() {
        NestedCondition condition = new NestedCondition("100");
        QueryWrapper<Student> wrapper = Conditions.queryWrapper(condition, Student.class);
        // ... WHERE remove_flag=0 AND ((stu_name LIKE ? OR stu_code LIKE ?))
        // %100%(String), %100%(String)
        List<Student> list = studentService.list(wrapper);
        log.warn(" >>>>>>>> test nested condition. target sql={}, result={}", wrapper.getTargetSql(), list.size());
        Assertions.assertEquals(wrapper.getTargetSql(), "((stu_name LIKE ? OR stu_code LIKE ?))");

        wrapper = Wrappers.query();
        Conditions.setNestedCondition(wrapper, Keyword.LIKE, Lists.newArrayList("stu_name", "stuCode"), "100", NestedType.OR);
        // ... WHERE remove_flag=0 AND ((stu_name LIKE ? OR stu_code LIKE ?))
        // %100%(String), %100%(String)
        list = studentService.list(wrapper);
        log.warn(" >>>>>>>> test nested condition. target sql={}, result={}", wrapper.getTargetSql(), list.size());
        Assertions.assertEquals(wrapper.getTargetSql(), "((stu_name LIKE ? OR stu_code LIKE ?))");
    }

    /**
     * 测试通用条件
     */
    @Test
    @Order(5)
    public void testGeneralCondition() {
        //设置通用条件
        GeneralCondition condition = new GeneralCondition("2", "2");
        //查询学生
        QueryWrapper<Student> studentWrapper = Conditions.queryWrapper(condition, Student.class);
        // ... WHERE remove_flag=0 AND (stu_code LIKE ? AND stu_name LIKE ?)
        // %2%(String), %2%(String)
        List<Student> studentList = studentService.list(studentWrapper);
        log.warn(" >>>>>>>> test general condition 1 finished. target sql={}, result={}", studentWrapper.getTargetSql(),
                 studentList.size());
        Assertions.assertEquals(studentWrapper.getTargetSql(), "(stu_code LIKE ? AND stu_name LIKE ?)");

        //查询班级
        QueryWrapper<Classes> classesWrapper = Conditions.queryWrapper(condition, Classes.class);
        // ... WHERE remove_flag=0 AND (class_code LIKE ?)
        // %2%(String)
        List<Classes> classesList = classesService.list(classesWrapper);
        log.warn(" >>>>>>>> test general condition 2 finished. target sql={}, result={}", classesWrapper.getTargetSql(),
                 classesList.size());
        Assertions.assertEquals(classesWrapper.getTargetSql(), "(class_code LIKE ?)");

        //查询老师
        QueryWrapper<Teacher> teacherWrapper = Conditions.queryWrapper(condition, Teacher.class);
        // ... WHERE remove_flag=0 AND (tea_code LIKE ? AND tea_name LIKE ?)
        // %2%(String), %2%(String)
        List<Teacher> teacherList = teacherService.list(teacherWrapper);
        log.warn(" >>>>>>>> test general condition 3 finished. target sql={}, result={}", teacherWrapper.getTargetSql(),
                 teacherList.size());
        Assertions.assertEquals(teacherWrapper.getTargetSql(), "(tea_code LIKE ? AND tea_name LIKE ?)");
    }

    /**
     * 测试多条件配置的情况
     */
    @Test
    @Order(6)
    public void testMultipleCondition() {
        StudentCondition condition = new StudentCondition();
        // stu_code like '%1%'
        condition.setCode("1");
        // stu_name like '%张%'
        condition.setStuName("张");
        // stu_sex = '男'
        condition.setStuSex("男");
        // stu_age > 18
        condition.setStuAge(18);
        // enrolment_time <= '2019-09-01 00:00:00.0'
        condition.setEnrolmentTime(DateUtil.toDate("2018-09-01"));
        // class_id in (2,4,6)
        condition.setClassesIds(Lists.newArrayList(2L, 4L, 6L));
        QueryWrapper<Student> wrapper = Conditions.queryWrapper(condition, Student.class);
        // ... WHERE remove_flag=0 AND (stu_code LIKE ? AND stu_name LIKE ? AND stu_age > ? AND stu_sex = ? AND enrolment_time <= ? AND classes_id IN (?,?,?))
        // %1%(String), %张%(String), 18(Integer), 男(String), 2019-09-01 00:00:00.0(Timestamp), 2(Long), 4(Long), 6(Long)
        List<Student> list = studentService.list(wrapper);
        log.warn(" >>>>>>>> test multiple condition finished. target sql=[{}], result={}", wrapper.getTargetSql(), list.size());
        Assertions.assertEquals(wrapper.getTargetSql(),
                                "(stu_code LIKE ? AND stu_name LIKE ? AND stu_age > ? AND stu_sex = ? AND enrolment_time <= ? AND classes_id IN (?,?,?))");
    }

    /**
     * 测试追加条件
     */
    @Test
    @Order(7)
    public void testAddCondition() {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        // tea_code is not null
        Conditions.setCondition(wrapper, Keyword.IS_NOT_NULL, "tea_code", null);
        // tea_name like '钱%'
        Conditions.setCondition(wrapper, Keyword.LIKE_RIGHT, "tea_name", "钱");
        // tea_sex = '女'
        Conditions.setCondition(wrapper, Keyword.EQ, "tea_sex", "女");
        // tea_age not in (3,4);
        Conditions.setCondition(wrapper, Keyword.NOT_IN, "tea_age", new double[]{3d, 4d});
        // ... WHERE remove_flag=0 AND (tea_code IS NOT NULL AND tea_name LIKE ? AND tea_sex = ? AND tea_age NOT IN (?,?))
        // 钱%(String), 女(String), 3.0(Double), 4.0(Double)
        List<Teacher> list = teacherService.list(wrapper);
        log.warn(" >>>>>>>> test add condition finished. target sql=[{}], result={}", wrapper.getTargetSql(), list.size());
        Assertions.assertEquals(wrapper.getTargetSql(),
                                "(tea_code IS NOT NULL AND tea_name LIKE ? AND tea_sex = ? AND tea_age NOT IN (?,?))");
    }

    /**
     * 测试构造分页条件
     */
    @Test
    @Order(8)
    public void testPageCondition() {
        //构建分页
        PageSearch<TeacherCondition> search = new PageSearch<TeacherCondition>().addSort("teaName", false);
        Page<Teacher> page = Conditions.page(search);
        // ... WHERE remove_flag = 0 ORDER BY tea_name DESC LIMIT ?,?
        // 0(Long), 10(Long)
        log.warn(" >>>>>>>> test page condition 1 finished. before data={}", JsonUtil.toJson(page));
        teacherService.page(page);
        log.warn(" >>>>>>>> test page condition 1 finished. after data={}", JsonUtil.toJson(page));
        log.warn(" >>>>>>>> test page condition 1 finished. condition={}", search.getCondition());
        Assertions.assertNull(search.getCondition());
        log.warn(" >>>>>>>> test page condition 1 finished. condition={}", search.getCondition(TeacherCondition.class));
        Assertions.assertNotNull(search.getCondition(TeacherCondition.class));
        log.warn(" >>>>>>>> test page condition 1 finished. target sql=[{}], result={}", "", page.getTotal());
        Assertions.assertEquals(page.getTotal(), 20);
        Page<Teacher> pageNew = teacherService.page(page);
        Assertions.assertEquals(page, pageNew);

        PageSearch<TeacherCondition> search2 = new PageSearch<TeacherCondition>().addAsc("teaName");
        Page<Teacher> page2 = Conditions.page(search2, Teacher.class);
        // ... WHERE remove_flag = 0 ORDER BY tea_name DESC LIMIT ?,?
        // 0(Long), 10(Long)
        log.warn(" >>>>>>>> test page condition 2 finished. before data={}", JsonUtil.toJson(page2));
        teacherService.page(page2);
        log.warn(" >>>>>>>> test page condition 2 finished. after data={}", JsonUtil.toJson(page2));
        log.warn(" >>>>>>>> test page condition 2 finished. condition={}", search2.getCondition());
        Assertions.assertNull(search2.getCondition());
        log.warn(" >>>>>>>> test page condition 2 finished. condition={}", search2.getCondition(TeacherCondition.class));
        Assertions.assertNotNull(search2.getCondition(TeacherCondition.class));
        log.warn(" >>>>>>>> test page condition 2 finished. target sql=[{}], result={}", "", page2.getTotal());
        Assertions.assertEquals(page2.getTotal(), 20);
        pageNew = teacherService.page(page2);
        Assertions.assertEquals(page2, pageNew);

        PageSearch<TeacherCondition> search3 = new PageSearch<TeacherCondition>().addDesc("teaName");
        //构建条件、分页
        TeacherCondition condition = new TeacherCondition("1", "李", "男");
        search3.setCondition(condition);
        search3.setCurrent(1);
        search3.setSize(5);
        QueryWrapper<Teacher> wrapper = Conditions.queryWrapper(condition, Teacher.class);
        Page<Teacher> page3 = Conditions.page(search3);
        // ... WHERE remove_flag = 0 AND (tea_code LIKE ? AND tea_name LIKE ? AND tea_sex = ?) ORDER BY tea_name DESC LIMIT ?,?
        // %1%(String), %李%(String), 男(String), 0(Long), 5(Long)
        log.warn(" >>>>>>>> test page condition 3 finished. before data={}", JsonUtil.toJson(page3));
        teacherService.page(page3, wrapper);
        log.warn(" >>>>>>>> test page condition 3 finished. after data={}", JsonUtil.toJson(page3));
        log.warn(" >>>>>>>> test page condition 3 finished. condition={}", search3.getCondition());
        Assertions.assertNotNull(search3.getCondition());
        log.warn(" >>>>>>>> test page condition 3 finished. condition={}", search3.getCondition(TeacherCondition.class));
        Assertions.assertNotNull(search3.getCondition(TeacherCondition.class));
        log.warn(" >>>>>>>> test page condition 3 finished. target sql=[{}], result={}", wrapper.getTargetSql(),
                 page3.getTotal());
        Assertions.assertEquals(wrapper.getTargetSql(), "(tea_code LIKE ? AND tea_name LIKE ? AND tea_sex = ?)");
        pageNew = teacherService.page(page3);
        Assertions.assertEquals(page3, pageNew);

        PageSearch<TeacherCondition> search4 = new PageSearch<TeacherCondition>().addDesc("teaName");
        //构建条件、分页
        TeacherCondition condition4 = new TeacherCondition("2", "刘", "女");
        search4.setCondition(condition4);
        Page<Teacher> page4 = Conditions.page(search4, Teacher.class);
        // ... WHERE remove_flag = 0 AND (tea_code LIKE ? AND tea_name LIKE ? AND tea_sex = ?) ORDER BY tea_name DESC LIMIT ?,?
        // %2%(String), %刘%(String), 女(String), 0(Long), 5(Long)
        log.warn(" >>>>>>>> test page condition 4 finished. before data={}", JsonUtil.toJson(page4));
        teacherService.page(page4, wrapper);
        log.warn(" >>>>>>>> test page condition 4 finished. after data={}", JsonUtil.toJson(page4));
        log.warn(" >>>>>>>> test page condition 4 finished. condition={}", search4.getCondition());
        Assertions.assertNotNull(search4.getCondition());
        log.warn(" >>>>>>>> test page condition 4 finished. condition={}", search4.getCondition(TeacherCondition.class));
        Assertions.assertNotNull(search4.getCondition(TeacherCondition.class));
        log.warn(" >>>>>>>> test page condition 4 finished. target sql=[{}], result={}", wrapper.getTargetSql(),
                 page4.getTotal());
        Assertions.assertEquals(wrapper.getTargetSql(), "(tea_code LIKE ? AND tea_name LIKE ? AND tea_sex = ?)");
        pageNew = teacherService.page(page4);
        Assertions.assertEquals(page4, pageNew);
    }

    /**
     * 测试追加排序
     */
    @Test
    @Order(9)
    public void testAddSort() {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        Conditions.setCondition(wrapper, Keyword.IS_NOT_NULL, "teaCode", null);
        Conditions.setCondition(wrapper, Keyword.EQ, "teaSex", "女");
        //设置排序
        Conditions.setSorting(wrapper, "teaName", false);
        List<Teacher> list = teacherService.list(wrapper);
        log.warn(" >>>>>>>> test add sort 1 finished. target sql=[{}], result={}", wrapper.getTargetSql(), list.size());
        Assertions.assertEquals(wrapper.getTargetSql(), "(tea_code IS NOT NULL AND tea_sex = ?) ORDER BY tea_name DESC");

        QueryWrapper<Teacher> wrapper2 = new QueryWrapper<>();
        //设置多排序，数组形式
        Conditions.setSorting(wrapper2, new Sort("tea_code", true), new Sort("tea_name", false));
        List<Teacher> list2 = teacherService.list(wrapper2);
        log.warn(" >>>>>>>> test add sort 2 finished. target sql=[{}], result={}", wrapper2.getTargetSql(), list2.size());
        Assertions.assertEquals(wrapper2.getTargetSql(), " ORDER BY tea_code ASC,tea_name DESC");

        QueryWrapper<Teacher> wrapper3 = new QueryWrapper<>();
        Conditions.setCondition(wrapper3, Keyword.EQ, "tea_sex", "男");
        //设置多排序，集合形式
        Conditions.setSorting(wrapper3, Lists.newArrayList(new Sort("tea_code", false), new Sort("tea_name", true)));
        List<Teacher> list3 = teacherService.list(wrapper3);
        log.warn(" >>>>>>>> test add sort 3 finished. target sql=[{}], result={}", wrapper3.getTargetSql(), list3.size());
        Assertions.assertEquals(wrapper3.getTargetSql(), "(tea_sex = ?) ORDER BY tea_code DESC,tea_name ASC");
    }
}
