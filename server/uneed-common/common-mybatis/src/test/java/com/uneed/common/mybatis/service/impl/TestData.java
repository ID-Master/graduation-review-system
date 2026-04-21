package com.uneed.common.mybatis.service.impl;

import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.DateUtil;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.text.PinyinUtil;
import com.uneed.common.mybatis.entity.Classes;
import com.uneed.common.mybatis.entity.Student;
import com.uneed.common.mybatis.entity.Teacher;
import com.uneed.common.mybatis.entity.TeacherClasses;
import com.uneed.common.mybatis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/24
 */
@Component
@Slf4j
public class TestData {

    @Autowired
    private ClassesService classesService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherClassesService teacherClassesService;
    @Autowired
    private ExamSubjectService examSubjectService;

    private final Random rand = new Random();

    public void clearAll() {
        int result = classesService.deleteAll();
        log.warn("result = {}", result);
        result = studentService.deleteAll();
        log.warn("result = {}", result);
        result = teacherService.deleteAll();
        log.warn("result = {}", result);
        result = teacherClassesService.deleteAll();
        log.warn("result = {}", result);
        result = examSubjectService.deleteAll();
        log.warn("result = {}", result);
    }

    public List<Student> buildDate(int size, boolean idNull, String type) {
        List<Student> list = Lists.newArrayList();
        Date date = new Date();
        IntStream.rangeClosed(1, size).forEach(i -> {
            Student student = new Student();
            if (!idNull) {
                if ("au".equals(type)) {
                    student.setId(String.valueOf(size / 2 + i));
                } else {
                    student.setId(String.valueOf(size + i));
                }
            }
            student.setStuCode((idNull ? "" : "id-") + size + "-" + i + "-" + type);
            student.setStuName((idNull ? "" : "id-") + "stu-" + size + "-" + i + "-" + type);
            student.setClassesId(21L);
            student.setStuAge(18);
            student.setStuSex(i % 2 == 0 ? "男" : "女");
            student.setEnrolmentTime(date);
            student.setStuNamePy(student.getStuName());
            list.add(student);
        });
        return list;
    }

    public List<Student> buildFilterDate() {
        List<Student> filterList = Lists.newArrayList();
        Student student1 = new Student();
        student1.setId("1");
        student1.setStuCode("a1");
        filterList.add(student1);
        Student student11 = new Student();
        student11.setId("11");
        student11.setStuCode("a11");
        filterList.add(student11);
        Student student2 = new Student();
        student2.setId("2");
        student2.setStuCode("b2");
        filterList.add(student2);
        Student student22 = new Student();
        student22.setId("22");
        student22.setStuCode("b22");
        filterList.add(student22);
        Student student222 = new Student();
        student222.setId("222");
        student222.setStuCode("b222");
        filterList.add(student222);
        return filterList;
    }

    public List<String> buildIdList(String start, String end) {
        List<String> list = Lists.newArrayList();
//        IntStream.range(start, end).mapToObj(i -> (String) (i + 1)).forEach(list::add);
        return list;
    }

    public int initClasses() {
        Classes classes1 = new Classes();
        classes1.setId("1");
        classes1.setClassCode("A001");
        classes1.setClassName("一年级1班");
        Classes classes2 = new Classes();
        classes2.setId("2");
        classes2.setClassCode("A002");
        classes2.setClassName("一年级2班");
        Classes classes3 = new Classes();
        classes3.setId("3");
        classes3.setClassCode("B001");
        classes3.setClassName("二年级1班");
        Classes classes4 = new Classes();
        classes4.setId("4");
        classes4.setClassCode("B002");
        classes4.setClassName("二年级2班");
        Classes classes5 = new Classes();
        classes5.setId("5");
        classes5.setClassCode("C001");
        classes5.setClassName("三年级1班");
        Classes classes6 = new Classes();
        classes6.setId("6");
        classes6.setClassCode("C002");
        classes6.setClassName("三年级2班");
        return classesService.insertBatch(Lists.newArrayList(classes1, classes2, classes3, classes4, classes5, classes6));
    }

    public int initStudent() {
        Long[] arrayClasses = {1L, 2L, 3L, 4L, 5L, 6L};
        String[] arrayName = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈"};
        Integer[] arrayAge = {15, 16, 17, 18, 19};
        String[] arraySex = {"男", "女"};
        Date[] arrayDate = {DateUtil.toDate("2017-09-01"), DateUtil.toDate("2018-09-01"), DateUtil.toDate("2019-09-01")};

        List<Student> list = Lists.newArrayList();
        //构造300个学生
        for (int i = 1; i <= 300; i++) {
            Student student = new Student();
            student.setId(String.valueOf(i));
            student.setClassesId(getDate(arrayClasses));
            student.setStuCode("s-" + getCode(student.getClassesId(), i));
            student.setStuName(getDate(arrayName) + i);
            student.setStuNamePy(PinyinUtil.toPinyin(student.getStuName()));
            student.setStuAge(getDate(arrayAge));
            student.setStuSex(getDate(arraySex));
            student.setEnrolmentTime(getDate(arrayDate));
            list.add(student);
        }
        return studentService.insertBatch(list);
    }

    public int initTeacher() {
        String[] arrayName = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈"};
        Double[] arrayAge = {1.5d, 3d, 9.5d, 11d, 26.5d, 4d};
        String[] arraySex = {"男", "女"};

        List<Teacher> list = Lists.newArrayList();
        //构造20个老师
        for (int i = 1; i <= 20; i++) {
            Teacher teacher = new Teacher();
            teacher.setId(String.valueOf(i));
            teacher.setTeaCode("t" + getCode(null, i));
            teacher.setTeaName(getDate(arrayName) + "老师");
            teacher.setTeaAge(getDate(arrayAge));
            teacher.setTeaSex(getDate(arraySex));
            list.add(teacher);
        }
        return teacherService.insertBatch(list);
    }

    public int initTeacherClass() {
        List<TeacherClasses> list = Lists.newArrayList();
        List<String> codes = Lists.newArrayList();
        while (list.size() < 50) {
            TeacherClasses teacherClasses = new TeacherClasses();
            teacherClasses.setClassesId((long) (rand.nextInt(6) + 1));
            teacherClasses.setTeacherId((long) (rand.nextInt(20) + 1));
            //过滤重复
            String code = teacherClasses.getClassesId() + "-" + teacherClasses.getTeacherId();
            if (codes.contains(code)) {
                continue;
            }
            codes.add(code);
            list.add(teacherClasses);
        }
        return teacherClassesService.insertBatch(list);
    }

    public <T> T getDate(T[] array) {
        return array[rand.nextInt(array.length)];
    }

    public String getCode(Long classesId, int index) {
        if (index < 10) {
            return ObjectUtil.nullToDefault(classesId, "") + "-00" + index;
        }
        if (index < 100) {
            return ObjectUtil.nullToDefault(classesId, "") + "-0" + index;
        }
        return ObjectUtil.nullToDefault(classesId, "") + "-" + index;
    }
}
