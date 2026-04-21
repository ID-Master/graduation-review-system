package com.uneed.common.support.api;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.support.api.entity.Demo;
import com.uneed.common.support.api.init.ConvertData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/26
 */
@Slf4j
public class BeanCopyTest {

    /**
     * 测试拷贝bean对象数据
     */
    @Test
    public void testCopyTest() {
        ConvertData data = new ConvertData();
        TimeMeter meter = new TimeMeter();
        int size = 100000;
        List<Demo> dataList = data.buildData(size);
        Assert.assertEquals(size, dataList.size());
        meter.sign("build");
        List<Demo> beans = copy(dataList, false);
        Assert.assertEquals(size, beans.size());
        meter.sign("bean");
        List<Demo> springs = copy(dataList, true);
        Assert.assertEquals(size, springs.size());
        meter.sign("spring");

        log.warn(" >>>>>>>> test copy build finished. size=[{}], time=[{}]ms", dataList.size(), meter.sign("build"));
        log.warn(" >>>>>>>> test copy bean finished. size=[{}], time=[{}]ms", beans.size(), meter.sign("build", "bean"));
        log.warn(" >>>>>>>> test copy spring finished. size=[{}], time=[{}]ms", springs.size(), meter.sign("bean", "spring"));
    }

    /**
     * 测试数组连接
     */
    @Test
    public void testArrayJoin() {
        ConvertData data = new ConvertData();
        TimeMeter meter = new TimeMeter();
        int size = 5;
        List<Demo> dataList = data.buildData(size);
        String[] codes = dataList.stream().map(Demo::getCode).toArray(String[]::new);
        log.warn(" >>>>>>>> test array join finished. size=[{}], time=[{}]ms, data={}", dataList.size(), meter.sign("build"),
                 dataList);
        log.warn(" >>>>>>>> test array join finished. data={}", ArrayUtil.join(codes," | "));
        Assert.assertEquals(size, dataList.size());
        Assert.assertEquals("code-1", codes[0]);
    }

    /**
     * 执行copy方法
     */
    private List<Demo> copy(List<Demo> demos, boolean isSpring) {
        List<Demo> list = Lists.newArrayList();
        demos.forEach(data -> {
            Demo demo = new Demo();
            if (isSpring) {
                BeanUtils.copyProperties(data, demo);
            } else {
                BeanUtil.copy(data, demo);
            }
            list.add(demo);
        });
        return list;
    }
}
