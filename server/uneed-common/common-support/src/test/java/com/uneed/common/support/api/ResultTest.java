package com.uneed.common.support.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.http.HttpCode;
import com.uneed.common.mybatis.page.PageData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/26
 */
@Slf4j
public class ResultTest {

    @Test
    @Order(1)
    public void ok() {
        //无 data返回
        System.out.println("------------------- 测试无data ----------------------------------------");
        Result<String> result = Result.ok();
        log.warn(" >>>>>>>> test not data ok(). data = {}", result);
        assertEquals(result.getMessage(), "操作成功");
        assertNull(result.getData());
        assertTrue(result.isSuccess());

        // simple data 返回
        System.out.println("------------------- 测试simple data ----------------------------------------");
        result = Result.ok("Test");
        log.warn(" >>>>>>>> test simple data ok(data). data = {}", result);
        assertEquals(result.getMessage(), "操作成功");
        assertEquals(result.getData(), "Test");

        result = Result.ok("Test", "success");
        log.warn(" >>>>>>>> test simple data ok(data,message). data = {}", result);
        assertEquals(result.getMessage(), "success");
        assertEquals(result.getData(), "Test");

        // map data 返回
        System.out.println("------------------- 测试map data ----------------------------------------");
        Map<String, Object> map = Maps.newHashMap();
        map.put("a", 1L);
        map.put("b", 2L);
        Result<Map<String, Object>> result1 = Result.ok(map);
        log.warn(" >>>>>>>> test map data ok(data). data = {}", result1);
        assertEquals(result1.getMessage(), "操作成功");
        assertEquals(result1.getData().get("b"), 2L);

        result1 = Result.ok(map, "success");
        log.warn(" >>>>>>>> test map data ok(data,message). data = {}", result1);
        assertEquals(result1.getMessage(), "success");
        assertEquals(result1.getData().get("b"), 2L);

        // list data 返回
        System.out.println("------------------- 测试list data ----------------------------------------");
        List<String> list = Lists.newArrayList("aa", "bb");
        Result<List<String>> result2 = Result.ok(list);
        log.warn(" >>>>>>>> test ok(data). data = {}", result2);
        assertEquals(result2.getMessage(), "操作成功");
        assertEquals(result2.getData().size(), 2);

        result2 = Result.ok(list, "success");
        log.warn(" >>>>>>>> test ok(data,message). data = {}", result2);
        assertEquals(result2.getMessage(), "success");
        assertEquals(result2.getData().size(), 2);

        // page data
        System.out.println("------------------- 测试page data ----------------------------------------");
        PageData<String> page = new PageData<>(Lists.newArrayList("aa", "bb"));
        Result<PageData<String>> result3 = Result.ok(page);
        log.warn(" >>>>>>>> test ok(data). data = {}", result3);
        assertEquals(result3.getMessage(), "操作成功");
        assertEquals(result3.getData().getTotal(), 2);

        result3 = Result.ok(page, "success");
        log.warn(" >>>>>>>> test ok(data,message). data = {}", result3);
        assertEquals(result3.getMessage(), "success");
        assertEquals(result3.getData().getTotal(), 2);
    }

    @Test
    @Order(2)
    public void fail() {
        // 无参返回
        System.out.println("------------------- 测试无参 ----------------------------------------");
        Result<String> result = Result.fail();
        log.warn(" >>>>>>>> test not parameter fail. data = {}", result);
        assertEquals(result.getCode(), 400);
        assertEquals(result.getMessage(), "请求失败");
        assertFalse(result.isSuccess());

        // 错误消息返回
        System.out.println("------------------- 测试带错误消息 ----------------------------------------");
        result = Result.fail("系统异常");
        log.warn(" >>>>>>>> test error message fail. data = {}", result);
        assertEquals(result.getCode(), 400);
        assertEquals(result.getMessage(), "系统异常");

        // 自定义编号、错误消息返回
        System.out.println("------------------- 测试带自定义编号、错误消息 ----------------------------------------");
        result = Result.fail(550, "业务异常");
        log.warn(" >>>>>>>> test code and error message fail. data = {}", result);
        assertEquals(result.getCode(), 550);
        assertEquals(result.getMessage(), "业务异常");

        // httpCode返回
        System.out.println("------------------- 测试httpCode参数 ----------------------------------------");
        result = Result.fail(HttpCode.UNAUTHORIZED);
        log.warn(" >>>>>>>> test http code fail. data = {}", result);
        assertEquals(result.getCode(), 401);
        assertEquals(result.getMessage(), "请求未授权");

        // httpCode返回
        System.out.println("------------------- 测试httpCode、错误消息 ----------------------------------------");
        result = Result.fail(HttpCode.REQUEST_TIMEOUT, "请求已超时");
        log.warn(" >>>>>>>> test http code fail. data = {}", result);
        assertEquals(result.getCode(), 408);
        assertEquals(result.getMessage(), "请求已超时");
    }

    @Test
    @Order(3)
    public void pageData() {
        List<Integer> list = Lists.newArrayList();
        PageData<Integer> page = new PageData<>(list);
        log.warn(" >>>>>>>> test page data. data = {}", page);
        assertEquals(page.getTotal(), 0);
        assertEquals(page.getCurrent(), 1);
        assertEquals(page.getSize(), 0);

        list = Lists.newArrayList(1, 2, 3);
        page = new PageData<>(list);
        log.warn(" >>>>>>>> test page data. data = {}", page);
        assertEquals(page.getTotal(), 3);
        assertEquals(page.getCurrent(), 1);
        assertEquals(page.getSize(), 3);

        page = new PageData<>(list, 10, 2, 5);
        log.warn(" >>>>>>>> test page data. data = {}", page);
        assertEquals(page.getRecords().size(), 3);
        assertEquals(page.getTotal(), 10);
        assertEquals(page.getCurrent(), 2);
        assertEquals(page.getSize(), 5);

        page = new PageData<>(list, new Page<>(1, 10));
        log.warn(" >>>>>>>> test page data. data = {}", page);
        assertEquals(page.getRecords().size(), 3);
        assertEquals(page.getTotal(), 3);
        assertEquals(page.getCurrent(), 1);
        assertEquals(page.getSize(), 10);

        page = new PageData<>(list, new Page<>(1, 10, 20));
        log.warn(" >>>>>>>> test page data. data = {}", page);
        assertEquals(page.getRecords().size(), 3);
        assertEquals(page.getTotal(), 20);
        assertEquals(page.getCurrent(), 1);
        assertEquals(page.getSize(), 10);
    }
}