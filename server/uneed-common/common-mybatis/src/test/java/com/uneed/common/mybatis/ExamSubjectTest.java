package com.uneed.common.mybatis;

import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.mybatis.entity.ExamSubject;
import com.uneed.common.mybatis.service.ExamSubjectService;
import com.uneed.common.mybatis.service.impl.TestData;
import com.uneed.common.mybatis.vo.ExamSubjectVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/25
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:mysql/spring-mysql.xml"})
@Slf4j
public class ExamSubjectTest {

    @Autowired
    private ExamSubjectService examSubjectService;

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
     * 测试新增数据
     */
    @Test
    @Order(1)
    public void testInsert() {
        TimeMeter meter = new TimeMeter();
        ExamSubject subject = new ExamSubject();
        subject.setSubjectCode("A001");
        subject.setSubjectName("语文");
        subject.setTenantId(1L);
        int result = examSubjectService.insert(subject);
        log.warn(" >>>>>>>> test insert 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"), subject);

        ExamSubject subject11 = new ExamSubject();
        subject11.setId("11");
        subject11.setSubjectCode("B001");
        subject11.setSubjectName("语文");
        subject11.setTenantId(1L);
        ExamSubject subject22 = new ExamSubject();
        subject22.setId("22");
        subject22.setSubjectCode("B002");
        subject22.setSubjectName("数学");
        subject22.setTenantId(1L);
        ExamSubject subject33 = new ExamSubject();
        subject33.setId("33");
        subject33.setSubjectCode("B003");
        subject33.setSubjectName("英语");
        subject33.setTenantId(1L);
        List<ExamSubject> list = Lists.newArrayList(subject11, subject22, subject33);
        result = examSubjectService.insertBatch(list);
        log.warn(" >>>>>>>> test insert 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"), list);
    }

    /**
     * 测试修改数据
     */
    @Test
    @Order(2)
    public void testUpdate() {
        TimeMeter meter = new TimeMeter();
        ExamSubject subject11 = new ExamSubject();
        subject11.setId("11");
        subject11.setSubjectName("二年级语文");
        int result = examSubjectService.update(subject11);
        log.warn(" >>>>>>>> test update 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"), subject11);

        ExamSubject subject22 = new ExamSubject();
        subject22.setId("22");
        subject22.setSubjectName("二年级数学");
        ExamSubject subject33 = new ExamSubject();
        subject33.setId("33");
        subject33.setSubjectName("二年级英语");
        List<ExamSubject> list = Lists.newArrayList(subject22, subject33);
        result = examSubjectService.updateBatch(list);
        log.warn(" >>>>>>>> test update 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"), list);
    }

    /**
     * 测试全量修改数据
     */
    @Test
    @Order(3)
    public void testUpdateFull() {
        TimeMeter meter = new TimeMeter();
        ExamSubject subject11 = new ExamSubject();
        subject11.setId("11");
        int result = examSubjectService.updateForAll(subject11);
        log.warn(" >>>>>>>> test update full 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                subject11);
        ExamSubject subject22 = new ExamSubject();
        subject22.setId("22");
        result = examSubjectService.updateForAll(subject22);
        log.warn(" >>>>>>>> test update full 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"),
                subject22);
        ExamSubject subject33 = new ExamSubject();
        subject33.setId("33");
        result = examSubjectService.updateForAll(subject33);
        log.warn(" >>>>>>>> test update full 3 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step2", "step3"),
                subject33);

        List<ExamSubject> list = Lists.newArrayList(subject11,subject22,subject33);
        result = examSubjectService.updateBatchForAll(list);
        log.warn(" >>>>>>>> test updateBath full 4 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step3", "step4"),
                JsonUtil.toJson(list));
    }

    /**
     * 测试新增或修改数据
     */
    @Test
    @Order(4)
    public void testInsertOrUpdate() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimeMeter meter = new TimeMeter();
        ExamSubject subject2 = new ExamSubject();
        subject2.setSubjectCode("A002");
        subject2.setSubjectName("数学");
        subject2.setTenantId(1L);
        int result = examSubjectService.insertOrUpdate(subject2);
        log.warn(" >>>>>>>> test insert or update 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                subject2);

        ExamSubject subject3 = new ExamSubject();
        subject3.setId("3");
        subject3.setSubjectCode("A003");
        subject3.setSubjectName("英语");
        subject3.setTenantId(1L);
        result = examSubjectService.insertOrUpdate(subject3);
        log.warn(" >>>>>>>> test insert or update 2 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step1", "step2"), subject3);

        ExamSubject subject11 = new ExamSubject();
        subject11.setId("11");
        subject11.setSubjectCode("B001");
        subject11.setSubjectName("二年级语文");
        subject11.setTenantId(1L);
        result = examSubjectService.insertOrUpdate(subject11);
        log.warn(" >>>>>>>> test insert or update 3 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step2", "step3"), subject11);


        ExamSubject subject22 = new ExamSubject();
        subject22.setId("22");
        subject22.setSubjectCode("B002");
        subject22.setSubjectName("二年级数学");
        subject22.setTenantId(1L);
        ExamSubject subject33 = new ExamSubject();
        subject33.setId("33");
        subject33.setSubjectCode("B003");
        subject33.setSubjectName("二年级英语");
        subject33.setTenantId(1L);
        ExamSubject subject44 = new ExamSubject();
        subject44.setId("44");
        subject44.setSubjectCode("C001");
        subject44.setSubjectName("三年级语文");
        subject44.setTenantId(1L);
        ExamSubject subject55 = new ExamSubject();
        subject55.setId("55");
        subject55.setSubjectCode("C002");
        subject55.setSubjectName("三年级数学");
        subject55.setTenantId(1L);
        ExamSubject subject66 = new ExamSubject();
        subject66.setId("66");
        subject66.setSubjectCode("C003");
        subject66.setSubjectName("三年级英语");
        subject66.setTenantId(1L);
        List<ExamSubject> list = Lists.newArrayList(subject22, subject33, subject44, subject55, subject66);
        result = examSubjectService.insertOrUpdateBatch(list);
        log.warn(" >>>>>>>> test insert or update 4 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step3", "step4"), list);
    }

    /**
     * 测试新增数据VO
     */
    @Test
    @Order(5)
    public void testInsertVO() {
        TimeMeter meter = new TimeMeter();
        ExamSubjectVO subject = new ExamSubjectVO();
        subject.setSubjectCode("A001-VO");
        subject.setSubjectName("语文-VO");
        subject.setDescription("语文-VO");
        subject.setVersion(1);
        int result = examSubjectService.insert(subject);
        log.warn(" >>>>>>>> test insert vo 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"), subject);

        ExamSubjectVO subject11 = new ExamSubjectVO();
        subject11.setId("111");
        subject11.setSubjectCode("B001-VO");
        subject11.setSubjectName("语文-VO");
        subject.setDescription("语文-VO");
        subject.setVersion(1);
        ExamSubjectVO subject22 = new ExamSubjectVO();
        subject22.setId("222");
        subject22.setSubjectCode("B002-VO");
        subject22.setSubjectName("数学-VO");
        subject.setDescription("数学-VO");
        subject.setVersion(1);
        ExamSubjectVO subject33 = new ExamSubjectVO();
        subject33.setId("333");
        subject33.setSubjectCode("B003-VO");
        subject33.setSubjectName("英语-VO");
        subject.setDescription("英语-VO");
        subject.setVersion(1);
        List<ExamSubjectVO> list = Lists.newArrayList(subject11, subject22, subject33);
        result = examSubjectService.insertBatch(list);
        log.warn(" >>>>>>>> test insert vo 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"),
                list);
    }

    /**
     * 测试修改数据VO
     */
    @Test
    @Order(6)
    public void testUpdateVO() {
        TimeMeter meter = new TimeMeter();
        ExamSubjectVO subject11 = new ExamSubjectVO();
        subject11.setId("111");
        subject11.setSubjectName("二年级语文-VO");
        subject11.setDescription("二年级语文-VO");
        subject11.setVersion(1);
        int result = examSubjectService.update(subject11);
        log.warn(" >>>>>>>> test update vo 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"), subject11);

        ExamSubjectVO subject22 = new ExamSubjectVO();
        subject22.setId("222");
        subject22.setSubjectName("二年级数学-VO");
        subject22.setDescription("二年级数学-VO");
        subject22.setVersion(1);
        ExamSubjectVO subject33 = new ExamSubjectVO();
        subject33.setId("333");
        subject33.setSubjectName("二年级英语-VO");
        subject33.setDescription("二年级英语-VO");
        subject33.setVersion(1);
        List<ExamSubjectVO> list = Lists.newArrayList(subject22, subject33);
        result = examSubjectService.updateBatch(list);
        log.warn(" >>>>>>>> test update vo 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"),
                list);
    }

    /**
     * 测试全量修改数据VO
     */
    @Test
    @Order(7)
    public void testUpdateFullVO() {
        TimeMeter meter = new TimeMeter();
        ExamSubjectVO subject11 = new ExamSubjectVO();
        subject11.setId("111");
        subject11.setVersion(1);
        int result = examSubjectService.updateForAll(subject11);
        log.warn(" >>>>>>>> test update full 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                subject11);
        ExamSubjectVO subject22 = new ExamSubjectVO();
        subject22.setId("222");
        subject22.setVersion(1);
        result = examSubjectService.updateForAll(subject22);
        log.warn(" >>>>>>>> test update full 2 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1", "step2"),
                subject22);
        ExamSubjectVO subject33 = new ExamSubjectVO();
        subject33.setId("333");
        subject33.setVersion(1);
        result = examSubjectService.updateForAll(subject33);
        log.warn(" >>>>>>>> test update full 3 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step2", "step3"),
                subject33);
    }

    /**
     * 测试新增或修改数据VO
     */
    @Test
    @Order(8)
    public void testInsertOrUpdateVO() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimeMeter meter = new TimeMeter();
        ExamSubjectVO subject2 = new ExamSubjectVO();
        subject2.setSubjectCode("A002-VO");
        subject2.setSubjectName("数学-VO");
        subject2.setDescription("数学-VO");
        subject2.setVersion(1);
        int result = examSubjectService.insertOrUpdate(subject2);
        log.warn(" >>>>>>>> test insert or update 1 finished. size=[{}], time=[{}]ms, data={}", result, meter.sign("step1"),
                subject2);

        ExamSubjectVO subject3 = new ExamSubjectVO();
        subject3.setId("4");
        subject3.setSubjectCode("A003-VO");
        subject3.setSubjectName("英语-VO");
        subject3.setDescription("英语-VO");
        subject3.setVersion(1);
        result = examSubjectService.insertOrUpdate(subject3);
        log.warn(" >>>>>>>> test insert or update 2 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step1", "step2"), subject3);

        ExamSubjectVO subject11 = new ExamSubjectVO();
        subject11.setId("111");
        subject11.setSubjectCode("B001-VO");
        subject11.setSubjectName("二年级语文-VO");
        subject11.setDescription("二年级语文-VO");
        subject11.setVersion(1);
        result = examSubjectService.insertOrUpdate(subject11);
        log.warn(" >>>>>>>> test insert or update 3 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step2", "step3"), subject11);


        ExamSubjectVO subject22 = new ExamSubjectVO();
        subject22.setId("222");
        subject22.setSubjectCode("B002-VO");
        subject22.setSubjectName("二年级数学-VO");
        subject22.setDescription("二年级数学-VO");
        subject22.setVersion(1);
        ExamSubjectVO subject33 = new ExamSubjectVO();
        subject33.setId("333");
        subject33.setSubjectCode("B003-VO");
        subject33.setSubjectName("二年级英语-VO");
        subject33.setDescription("二年级英语-VO");
        subject33.setVersion(1);
        ExamSubjectVO subject44 = new ExamSubjectVO();
        subject44.setId("444");
        subject44.setSubjectCode("C001-VO");
        subject44.setSubjectName("三年级语文-VO");
        subject44.setDescription("三年级语文-VO");
        subject44.setVersion(1);
        ExamSubjectVO subject55 = new ExamSubjectVO();
        subject55.setId("555");
        subject55.setSubjectCode("C002-VO");
        subject55.setSubjectName("三年级数学-VO");
        subject55.setDescription("三年级数学-VO");
        subject55.setVersion(1);
        ExamSubjectVO subject66 = new ExamSubjectVO();
        subject66.setId("666");
        subject66.setSubjectCode("C003-VO");
        subject66.setSubjectName("三年级英语-VO");
        subject66.setDescription("三年级英语-VO");
        subject66.setVersion(1);
        List<ExamSubjectVO> list = Lists.newArrayList(subject22, subject33, subject44, subject55, subject66);
        result = examSubjectService.insertOrUpdateBatch(list);
        log.warn(" >>>>>>>> test insert or update 4 finished. size=[{}], time=[{}]ms, data={}", result,
                meter.sign("step3", "step4"), list);
    }
}
