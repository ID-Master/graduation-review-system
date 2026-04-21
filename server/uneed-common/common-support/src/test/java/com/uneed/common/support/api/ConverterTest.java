package com.uneed.common.support.api;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.support.api.convert.DemoConverter;
import com.uneed.common.support.api.entity.Demo;
import com.uneed.common.support.api.init.ConvertData;
import com.uneed.common.support.api.pojo.DemoDTO;
import com.uneed.common.support.api.pojo.DemoVO;
import com.uneed.common.support.convert.Converters;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/27
 */
@Slf4j
public class ConverterTest {

    @Test
    @Order(1)
    public void testToPojo() {
        ConvertData data = new ConvertData();
        Demo demo = data.buildData();
        log.warn(" >>>>>>>> test converter pojo. demo = {}", demo);

        DemoVO vo = Converters.get(DemoConverter.class).toVO(demo);
        Assertions.assertEquals(demo.getCode(), vo.getCode());
        log.warn(" >>>>>>>> test converter vo. vo = {}", vo);
    }

    @Test
    @Order(2)
    public void testToPojoList() {
        ConvertData data = new ConvertData();
        List<Demo> demos = data.buildData(10);
        log.warn(" >>>>>>>> test converter pojo list. demos = {}", demos);

        List<DemoVO> voList = Converters.get(DemoConverter.class).toVO(demos);
        Assertions.assertEquals(10, voList.size());
        Assertions.assertEquals(demos.get(5).getCode(), voList.get(5).getCode());
        log.warn(" >>>>>>>> test converter vo list. vo = {}", voList);
    }

    @Test
    @Order(3)
    public void testToPageData() {
        ConvertData data = new ConvertData();
        List<Demo> demos = data.buildData(10);
        IPage<Demo> page = new Page<>(1, 10, 20);
        page.setRecords(demos);
        log.warn(" >>>>>>>> test converter vo page. page = {}", demos);

        PageData<DemoVO> voPage = Converters.get(DemoConverter.class).toVOPage(page);
        Assertions.assertEquals(10, voPage.getSize());
        Assertions.assertEquals(page.getTotal(), voPage.getTotal());
        Assertions.assertEquals(page.getCurrent(), voPage.getCurrent());
        Assertions.assertEquals(page.getRecords().get(4).getCode(), voPage.getRecords().get(4).getCode());
        log.warn(" >>>>>>>> test converter vo page. voPage = {}", voPage);
    }

    @Test
    @Order(4)
    public void testToEntity() {
        ConvertData data = new ConvertData();

        DemoVO vo = data.buildVOData();
        log.warn(" >>>>>>>> test vo converter entity. vo = {}", vo);
        Demo demo = Converters.get(DemoConverter.class).toEntity(vo);
        Assertions.assertEquals(demo.getCode(), vo.getCode());
        Assertions.assertNotEquals(demo.getRemark(), vo.getRemark());
        log.warn(" >>>>>>>> test vo converter entity. demo = {}", demo);

        DemoDTO dto = data.buildDTOData();
        log.warn(" >>>>>>>> test dto converter entity. dto = {}", dto);
        demo = Converters.get(DemoConverter.class).toEntity(dto);
        Assertions.assertEquals(demo.getName(), dto.getName());
        Assertions.assertNotEquals(demo.getRemark(), dto.getRemark());
        log.warn(" >>>>>>>> test dto converter entity. demo = {}", demo);

        Demo other = data.buildData();
        log.warn(" >>>>>>>> test other converter entity. other = {}", other);
        demo = Converters.get(DemoConverter.class).toEntity(other);
        Assertions.assertEquals(demo.getName(), other.getName());
        Assertions.assertEquals(demo.getRemark(), other.getRemark());
        log.warn(" >>>>>>>> test other converter entity. demo = {}", demo);
    }

    @Test
    @Order(5)
    public void testToEntityList() {
        ConvertData data = new ConvertData();

        List<DemoVO> voList = data.buildVOData(5);
        log.warn(" >>>>>>>> test voList converter entityList. voList = {}", voList);
        List<Demo> demos = Converters.get(DemoConverter.class).toEntity(voList);
        Assertions.assertEquals(5, demos.size());
        Assertions.assertEquals(demos.get(3).getCode(), voList.get(3).getCode());
        Assertions.assertNotEquals(demos.get(3).getRemark(), voList.get(3).getRemark());
        log.warn(" >>>>>>>> test voList converter entityList. demos = {}", demos);

        List<DemoDTO> dtoList = data.buildDTOData(5);
        log.warn(" >>>>>>>> test dtoList converter entityList. dtoList = {}", dtoList);
        demos = Converters.get(DemoConverter.class).toEntity(dtoList);
        Assertions.assertEquals(5, demos.size());
        Assertions.assertEquals(demos.get(3).getCode(), dtoList.get(3).getCode());
        Assertions.assertNotEquals(demos.get(3).getRemark(), dtoList.get(3).getRemark());
        log.warn(" >>>>>>>> test dtoList converter entityList. demos = {}", demos);

        List<Demo> others = data.buildData(5);
        log.warn(" >>>>>>>> test others converter entityList. others = {}", others);
        demos = Converters.get(DemoConverter.class).toEntity(others);
        Assertions.assertEquals(5, demos.size());
        Assertions.assertEquals(demos.get(3).getCode(), others.get(3).getCode());
        Assertions.assertEquals(demos.get(3).getRemark(), others.get(3).getRemark());
        log.warn(" >>>>>>>> test others converter entityList. demos = {}", demos);
    }

    @Test
    @Order(6)
    public void testToConvert() {
        long time = System.currentTimeMillis();
        ConvertData data = new ConvertData();
        int size = 1000;
        List<Demo> list = data.buildData(size);
        long time2 = System.currentTimeMillis();
        log.warn("======= build, size=" + size+", time="+(time2-time));
        List<DemoDTO> dtoList = Lists.newArrayList();
        for (Demo demo : list){
            dtoList.add(BeanUtil.copyNew(demo,DemoDTO.class));
        }
        long time3 = System.currentTimeMillis();
        log.warn("======= copy, size=" + dtoList.size()+", time="+(time3-time2));

        List<DemoDTO> dtoList2 = Lists.newArrayList();
        for (Demo demo : list){
            dtoList2.add(JSON.parseObject(JSON.toJSONString(demo),DemoDTO.class));
        }
        long time4 = System.currentTimeMillis();
        log.warn("======= serialize, size=" + dtoList2.size()+", time="+(time4-time3));

        List<DemoDTO> dtoList3 = JSON.parseArray(JSON.toJSONString(list),DemoDTO.class);

        long time5 = System.currentTimeMillis();
        log.warn("======= serialize, size=" + dtoList3.size()+", time="+(time5-time4));
    }

}
