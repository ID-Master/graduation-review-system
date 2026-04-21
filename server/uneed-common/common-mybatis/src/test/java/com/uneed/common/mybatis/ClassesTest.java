package com.uneed.common.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.mybatis.entity.Classes;
import com.uneed.common.mybatis.mapper.ClassesMapper;
import com.uneed.common.mybatis.service.ClassesService;
import com.uneed.common.mybatis.service.impl.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
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
public class ClassesTest {

    @Autowired
    private ClassesService classesService;

    @Autowired
    private ClassesMapper classesMapper;

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
     * 测试非自动填充的保存
     */
    @Test
    @Order(1)
    public void testInsertCustom() {
        TimeMeter meter = new TimeMeter();
        Classes classes11 = new Classes();
        classes11.setClassCode("A001");
        classes11.setClassName("一年级一班");
        //测试非自动填充
        classes11.setId("11");
        classes11.setCreatedBy("1");
        classes11.setUpdatedBy("1");
        classes11.setCreatedDate(LocalDateTime.now());
        classes11.setUpdatedDate(LocalDateTime.now());
        classes11.setRemoveFlag(0);
        int result = classesService.insert(classes11);
        log.warn(" >>>>>>>> test insert custom finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), classes11);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试自动填充的保存
     */
    @Test
    @Order(2)
    public void testInsert() {
        TimeMeter meter = new TimeMeter();
        Classes classes12 = new Classes();
        classes12.setClassCode("A002");
        classes12.setClassName("一年级二班");
        int result = classesService.insert(classes12);
        log.warn(" >>>>>>>> test insert finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), classes12);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试非自动填充的批量保存
     */
    @Test
    @Order(3)
    public void testInsertBatchCustom() {
        TimeMeter meter = new TimeMeter();
        Classes classes21 = new Classes();
        classes21.setClassCode("B001");
        classes21.setClassName("二年级一班");
        //测试非自动填充
        classes21.setId("21");
        classes21.setCreatedBy("1");
        classes21.setUpdatedBy("1");
        classes21.setCreatedDate(LocalDateTime.now());
        classes21.setUpdatedDate(LocalDateTime.now());
        classes21.setRemoveFlag(0);

        Classes classes22 = new Classes();
        classes22.setClassCode("B002");
        classes22.setClassName("二年级二班");
        //测试非自动填充
        classes22.setId("22");
        classes22.setCreatedBy("1");
        classes22.setUpdatedBy("1");
        classes22.setCreatedDate(LocalDateTime.now());
        classes22.setUpdatedDate(LocalDateTime.now());
        classes22.setRemoveFlag(0);

        List<Classes> list = Lists.newArrayList(classes21, classes22);
        int result = classesService.insertBatch(list);
        log.warn(" >>>>>>>> test insert batch custom finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), list);
        Assertions.assertEquals(result, 2);
    }

    /**
     * 测试批量保存
     */
    @Test
    @Order(4)
    public void testInsertBatch() {
        TimeMeter meter = new TimeMeter();
        Classes classes31 = new Classes();
        classes31.setClassCode("C001");
        classes31.setClassName("三年级一班");

        Classes classes32 = new Classes();
        classes32.setClassCode("C002");
        classes32.setClassName("三年级二班");

        Classes classes41 = new Classes();
        classes41.setClassCode("D001");
        classes41.setClassName("四年级一班");

        Classes classes42 = new Classes();
        classes42.setClassCode("D002");
        classes42.setClassName("四年级二班");

        Classes classes51 = new Classes();
        classes51.setClassCode("E001");
        classes51.setClassName("五年级一班");

        Classes classes52 = new Classes();
        classes52.setClassCode("E002");
        classes52.setClassName("五年级二班");

        Classes classes61 = new Classes();
        classes61.setClassCode("F001");
        classes61.setClassName("六年级一班");

        Classes classes62 = new Classes();
        classes62.setClassCode("F002");
        classes62.setClassName("六年级二班");

        List<Classes> list = Lists
                .newArrayList(classes31, classes32, classes41, classes42, classes51, classes52, classes61, classes62);
        int result = classesService.insertBatch(list);
        log.warn(" >>>>>>>> test insert batch finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), list);
        Assertions.assertEquals(result, 8);
    }

    /**
     * 测试非自动填充的修改
     */
    @Test
    @Order(5)
    public void testUpdate() {
        TimeMeter meter = new TimeMeter();
        Classes classes11 = new Classes();
        classes11.setId("11");
        classes11.setClassName("一年级1班");
        //测试非自动填充
        classes11.setUpdatedBy("11");
        classes11.setUpdatedDate(LocalDateTime.now());
        int result = classesService.update(classes11);
        log.warn(" >>>>>>>> test update finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), classes11);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试自动填充的批量修改
     */
    @Test
    @Order(6)
    public void testUpdateBatch() {
        TimeMeter meter = new TimeMeter();
        Classes classes21 = new Classes();
        classes21.setId("21");
        classes21.setClassName("二年级1班");

        Classes classes22 = new Classes();
        classes22.setId("22");
        classes22.setClassName("二年级2班");

        List<Classes> list = Lists.newArrayList(classes21, classes22);
        int result = classesService.updateBatch(list);
        log.warn(" >>>>>>>> test update batch finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), list);
        Assertions.assertEquals(result, 2);
    }

    /**
     * 测试逻辑删除
     */
    @Test
    @Order(7)
    public void testRemove() {
        TimeMeter meter = new TimeMeter();
        Long id = 11L;
        int result = classesService.removeById("11");
        log.warn(" >>>>>>>> test remove finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), id);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试批量逻辑删除
     */
    @Test
    @Order(8)
    public void testRemoveBatch() {
        TimeMeter meter = new TimeMeter();
        List<String> ids = Lists.newArrayList("21", "21");
        int result = classesService.removeByIds(ids);
        log.warn(" >>>>>>>> test remove batch finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), ids);
        Assertions.assertEquals(result, 2);
    }

    /**
     * 测试批量物理删除
     */
    @Test
    @Order(9)
    public void testDeleteBatch() {
        TimeMeter meter = new TimeMeter();
        List<Long> ids = Lists.newArrayList(11L, 21L, 22L);
        int result = classesService.delete(ids);
        log.warn(" >>>>>>>> test delete batch finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), ids);
        Assertions.assertEquals(result, 3);
    }

    /**
     * 测试恢复自定义保存数据
     */
    @Test
    @Order(10)
    public void testRecoverInsertCustom() {
        TimeMeter meter = new TimeMeter();
        testInsertCustom();
        testInsertBatchCustom();
        log.warn(" >>>>>>>> test recover insert custom finished. time=[{}]ms", meter.sign());
    }

    /**
     * 测试根据id获取单个对象
     */
    @Test
    @Order(11)
    public void testGetById() {
        TimeMeter meter = new TimeMeter();
        Classes classes = classesService.getById("11");
        log.warn(" >>>>>>>> test get by id finished. time=[{}]ms, data={}", meter.sign(), classes);
        Assertions.assertEquals(classes.getClassCode(), "A001");
        Assertions.assertTrue(classesService.getOptById("11").isPresent());
    }

    /**
     * 测试根据wrapper获取单个对象
     */
    @Test
    @Order(12)
    public void testGetByWrapper() {
        TimeMeter meter = new TimeMeter();
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.like("class_name", "二年级");
        wrapper.orderByAsc("class_code");
        Classes classes = classesService.get(wrapper);
        log.warn(" >>>>>>>> test get by wrapper finished. time=[{}]ms, data={}", meter.sign(), classes);
        Assertions.assertEquals(classes.getClassCode(), "B001");
        Assertions.assertTrue(classesService.getOpt(wrapper).isPresent());
    }

    /**
     * 测试根据lambda wrapper获取单个对象
     */
    @Test
    @Order(13)
    public void testGetByLambdaWrapper() {
        TimeMeter meter = new TimeMeter();
        LambdaQueryWrapper<Classes> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Classes::getClassName, "二年级");
        wrapper.orderByDesc(Classes::getClassCode);
        Classes classes = classesService.get(wrapper);
        log.warn(" >>>>>>>> test get by lambda wrapper finished. time=[{}]ms, data={}", meter.sign(), classes);
        Assertions.assertEquals(classes.getClassCode(), "B002");
    }

    /**
     * 测试获取所有数据
     */
    @Test
    @Order(14)
    public void testList() {
        TimeMeter meter = new TimeMeter();
        List<Classes> list = classesService.list();
        log.warn(" >>>>>>>> test list all finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(), list);
        Assertions.assertTrue(list.size() > 0);
    }

    /**
     * 测试根据id集合获取数据集合
     */
    @Test
    @Order(15)
    public void testListByIds() {
        TimeMeter meter = new TimeMeter();
        List<Classes> list = classesService.listByIds(Lists.newArrayList("11", "21", "22"));
        log.warn(" >>>>>>>> test list by ids finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(), list);
        Assertions.assertTrue(list.size() > 0);
    }

    /**
     * 测试根据wrapper获取数据集合
     */
    @Test
    @Order(16)
    public void testListByWrapper() {
        TimeMeter meter = new TimeMeter();
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.likeLeft("class_name", "二班");
        wrapper.orderByAsc("class_code");
        List<Classes> list = classesService.list(wrapper);
        log.warn(" >>>>>>>> test list by wrapper finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(), list);
        Assertions.assertEquals(list.size(), 6);
    }

    /**
     * 测试根据lambda wrapper获取数据集合
     */
    @Test
    @Order(17)
    public void testListByLambdaWrapper() {
        TimeMeter meter = new TimeMeter();
        LambdaQueryWrapper<Classes> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeLeft(Classes::getClassName, "二班");
        wrapper.orderByDesc(Classes::getClassCode);
        List<Classes> list = classesService.list(wrapper);
        log.warn(" >>>>>>>> test list by lambda wrapper finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(),
                list);
        Assertions.assertEquals(list.size(), 6);
    }

    /**
     * 测试根据分页对象获取分页数据
     */
    @Test
    @Order(18)
    public void testPage() {
        TimeMeter meter = new TimeMeter();
        //构建分页对象，并排序
        Page<Classes> page = new Page<>(1, 5);
        page.addOrder(OrderItem.asc("class_code"));
        //分页查询
        page = classesService.page(page);
        List<Classes> list = page.getRecords();
        log.warn(" >>>>>>>> test page finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(), list);
        Assertions.assertTrue(list.size() > 0);
    }

    /**
     * 测试根据wrapper获取数据集合
     */
    @Test
    @Order(19)
    public void testPageByWrapper() {
        TimeMeter meter = new TimeMeter();
        //构建分页对象，并排序
        Page<Classes> page = new Page<>(1, 5);
        page.addOrder(OrderItem.desc("class_code"));
        //构建查询条件，并排序
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.likeLeft("class_name", "二班");
        wrapper.orderByAsc("class_name");
        //分页查询
        page = classesService.page(page, wrapper);
        List<Classes> list = page.getRecords();
        log.warn(" >>>>>>>> test page by wrapper finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(), list);
        Assertions.assertTrue(list.size() > 0);
    }

    /**
     * 测试根据lambda wrapper获取数据集合
     */
    @Test
    @Order(20)
    public void testPageByLambdaWrapper() {
        TimeMeter meter = new TimeMeter();
        //构建分页对象
        Page<Classes> page = new Page<>(2, 5);
        //构建查询条件，并排序
        LambdaQueryWrapper<Classes> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeLeft(Classes::getClassName, "二班");
        wrapper.orderByAsc(Classes::getClassName);
        //分页查询
        page = classesService.page(page, wrapper);
        List<Classes> list = page.getRecords();
        log.warn(" >>>>>>>> test page by lambda wrapper finished. size=[{}], time=[{}]ms, data={}", list.size(), meter.sign(),
                list);
        Assertions.assertTrue(list.size() > 0);
    }

    /**
     * 测试Mapper的非自动填充保存
     */
    @Test
    @Order(21)
    public void testMapperInsertCustom() {
        TimeMeter meter = new TimeMeter();
        Classes classes71 = new Classes();
        classes71.setClassCode("G001");
        classes71.setClassName("初中一年级一班");
        //测试非自动填充
        classes71.setId("71");
        classes71.setCreatedBy("1");
        classes71.setUpdatedBy("1");
        classes71.setCreatedDate(LocalDateTime.now());
        classes71.setUpdatedDate(LocalDateTime.now());
        classes71.setRemoveFlag(0);
        int result = classesMapper.insert(classes71);
        log.warn(" >>>>>>>> test mapper insert custom finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(),
                classes71);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试Mapper的保存
     */
    @Test
    @Order(22)
    public void testMapperInsert() {
        TimeMeter meter = new TimeMeter();
        Classes classes72 = new Classes();
        classes72.setClassCode("G002");
        classes72.setClassName("初中一年级二班");
        int result = classesMapper.insert(classes72);
        log.warn(" >>>>>>>> test mapper insert finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), classes72);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试mapper的updateById方法
     * 接收一个实体类的参数，以数据的id为条件，修改数据
     */
    @Test
    @Order(23)
    public void testMapperUpdate() {
        TimeMeter meter = new TimeMeter();
        Classes classes = new Classes();
        classes.setId("71");
        classes.setClassName("初中一年级1班");
        int result = classesMapper.updateById(classes);
        log.warn(" >>>>>>>> test mapper update finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), classes);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试mapper的update方法，接收俩个参数，参数1：实体类对象，参数2：wrapper对象
     */
    @Test
    @Order(24)
    public void testMapperUpdateWrapper() {
        TimeMeter meter = new TimeMeter();
        // 第一种情况：实体参数为null的情况下，不会执行自动填充
        // sql: UPDATE ud_classes SET class_name=? WHERE remove_flag=0 AND (class_code = ?)
        // Params: 初中一年级2班(String), G002(String)
        UpdateWrapper<Classes> wrapper = new UpdateWrapper<>();
        wrapper.set("class_name", "初中一年级2班");
        wrapper.eq("class_code", "G002");
        int result = classesMapper.update(null, wrapper);
        log.warn(" >>>>>>>> test mapper update wrapper 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                result);
        Assertions.assertEquals(result, 1);

        // 第二种情况：实体参数为不为null的情况，执行自动填充
        // sql: UPDATE ud_classes SET update_by=?, update_time=?, class_name=? WHERE remove_flag=0 AND (class_code = ?)
        // Params: 0(Long), 2020-04-14 14:26:11.928(Timestamp), 初中一年级贰班(String), G002(String)
        wrapper = new UpdateWrapper<>();
        wrapper.set("class_name", "初中一年级贰班");
        wrapper.eq("class_code", "G002");
        result = classesMapper.update(new Classes(), wrapper);
        log.warn(" >>>>>>>> test mapper update wrapper 2 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step1", "step2"), result);
        Assertions.assertEquals(result, 1);

        // 第三种情况：实体参数的赋值字段与wrapper的赋值字段相同的情况下，都会执行赋值操作，最终结果会倾向wrapper的赋值
        // sql: UPDATE ud_classes SET class_name=?, update_by=?, update_time=?, class_name=? WHERE remove_flag=0 AND (class_code = ?)
        // Params: 初中一年级(贰)班(String), 0(Long), 2020-04-14 14:26:11.94(Timestamp), 初中一年级(2)班(String), G002(String)
        wrapper = new UpdateWrapper<>();
        wrapper.set("class_name", "初中一年级(贰)班");
        wrapper.eq("class_code", "G002");
        Classes classes = new Classes();
        classes.setClassName("初中一年级(2)班");
        result = classesMapper.update(classes, wrapper);
        log.warn(" >>>>>>>> test mapper update wrapper 3 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step2", "step3"), result);
        Assertions.assertEquals(result, 1);

        // 第四种情况：实体参数的赋值字段与wrapper的赋值字段不相同的情况下，并且实体参数有设置了id的情况（会忽略id的值）
        // sql: UPDATE ud_classes SET create_by=?, update_by=?, update_time=?, class_name=? WHERE remove_flag=0 AND (class_code = ?)
        // Params: 73(Long), 0(Long), 2020-04-14 14:26:11.954(Timestamp), 初中一年级(贰)班(String), G002(String)
        wrapper = new UpdateWrapper<>();
        wrapper.set("class_name", "初中一年级(壹)班");
        wrapper.eq("class_code", "G001");
        classes = new Classes();
        classes.setId("70");
        classes.setCreatedBy("70");
        result = classesMapper.update(classes, wrapper);
        log.warn(" >>>>>>>> test mapper update wrapper 4 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step3", "step4"), result);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试将值修改为null的方法
     */
    @Test
    @Order(25)
    public void testUpdateNull() {
        TimeMeter meter = new TimeMeter();
        UpdateWrapper<Classes> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", 71L);
        wrapper.set("class_name", null);
        wrapper.set("class_code", null);
        Classes classes = new Classes();
        classes.setClassCode("111");
        classes.setClassName("aaa");
        int result = classesMapper.update(classes, wrapper);
        log.warn(" >>>>>>>> test mapper update finished. size=[{}], time=[{}]ms, data={}", result, meter.sign(), result);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试全量修改
     */
    @Test
    @Order(26)
    public void testUpdateFull() {
        TimeMeter meter = new TimeMeter();
        Classes classes11 = new Classes();
        classes11.setId("11");
        classes11.setClassName("");
        int result = classesService.updateForAll(classes11);
        log.warn(" >>>>>>>> test mapper update finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"), result);
        Assertions.assertEquals(result, 1);

        classes11 = new Classes();
        classes11.setId("11");
        classes11.setCreatedBy("11");
        classes11.setClassName("一年级1班");
        result = classesService.updateForAll(classes11);
        log.warn(" >>>>>>>> test mapper update finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"),
                result);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试新增或修改
     */
    @Test
    @Order(27)
    public void testInsertOrUpdate() {
        TimeMeter meter = new TimeMeter();
        //测试新增或修改，无id的情况
        Classes classes = new Classes();
        classes.setClassCode("not id");
        classes.setClassName("测试不带id的情况");
        int result = classesService.insertOrUpdate(classes);
        log.warn(" >>>>>>>> test insert or update 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                result);
        Assertions.assertEquals(result, 1);

        //测试新增或修改，有id的情况
        Classes classes1 = new Classes();
        classes1.setId("1");
        classes1.setClassCode("exists id");
        classes1.setClassName("测试存在id的情况");
        result = classesService.insertOrUpdate(classes1);
        log.warn(" >>>>>>>> test insert or update 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step2"),
                result);
        Assertions.assertEquals(result, 1);

        //测试新增或修改，有id，并且存在数据的情况
        Classes classes11 = new Classes();
        classes11.setId("11");
        classes11.setClassCode("A001");
        classes11.setClassName("一年级一班");
        result = classesService.insertOrUpdate(classes11);
        log.warn(" >>>>>>>> test insert or update 3 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step3"),
                result);
        Assertions.assertEquals(result, 1);
    }

    /**
     * 测试批量新增或修改
     */
    @Test
    @Order(28)
    public void testInsertOrUpdateBatch() {
        TimeMeter meter = new TimeMeter();
        //构建批量数据
        List<Classes> list = Lists.newArrayList();
        for (int i = 1; i < 10; i++) {
            Classes classes = new Classes();
            classes.setId(String.valueOf( i + 20));
            classes.setClassCode("B00" + i);
            classes.setClassName("二年级" + i + "班");
            list.add(classes);
        }
        Classes classes71 = new Classes();
        classes71.setId("71");
        classes71.setClassCode("G01");
        classes71.setClassName("初中一年级(壹)班");
        list.add(classes71);
        //测试批量修改新增或修改的方法，10条数据，3条修改，7条新增
        int result = classesService.insertOrUpdateBatch(list);
        log.warn(" >>>>>>>> test insert or update batch 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                result);
        Assertions.assertEquals(result, 10);
    }

    /**
     * 测试获取所有count
     */
    @Test
    @Order(29)
    public void testCount() {
        TimeMeter meter = new TimeMeter();
        long size = classesService.count();
        log.warn(" >>>>>>>> test count all finished. size=[{}], time=[{}]ms, data={}", size, meter.sign("step1"),
                size);
        Assertions.assertEquals(size, 23);
    }

    /**
     * 测试根据条件获取count
     */
    @Test
    @Order(30)
    public void testCountWrapper() {
        TimeMeter meter = new TimeMeter();
        LambdaQueryWrapper<Classes> wrapper = Wrappers.lambdaQuery(Classes.class).like(Classes::getClassCode, "B0");
        long size = classesService.count(wrapper);
        log.warn(" >>>>>>>> test count all finished. size=[{}], time=[{}]ms, data={}", size, meter.sign("step1"),
                size);
        Assertions.assertEquals(size, 9);
    }

}
