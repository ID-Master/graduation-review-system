package com.uneed.common.support.api.init;

import com.uneed.common.core.collection.Lists;
import com.uneed.common.support.api.entity.Demo;
import com.uneed.common.support.api.pojo.DemoDTO;
import com.uneed.common.support.api.pojo.DemoVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/27
 */
public class ConvertData {

    /**
     * 构造单条数据
     *
     * @return Demo
     */
    public Demo buildData() {
        return buildData(1).get(0);
    }

    /**
     * 构造批量数据
     *
     * @param size 数量
     * @return List<Demo>
     */
    public List<Demo> buildData(int size) {
        List<Demo> list = Lists.newArrayList();
        for (int i = 1; i <= size; i++) {
            Demo demo = new Demo();
            demo.setId(String.valueOf(i));
            demo.setCreatedBy("1");
            demo.setUpdatedBy("1");
            demo.setCreatedDate(LocalDateTime.now());
            demo.setUpdatedDate(LocalDateTime.now());
            demo.setRemoveFlag(0);
            demo.setCode("code-" + i);
            demo.setName("name-" + i);
            demo.setScore(i % 2 == 0 ? new BigDecimal("90.25") : new BigDecimal("70.55"));
            demo.setEnable(i % 2 == 0);
            demo.setRemark("remark:" + demo.getCode() + ", " + demo.getName());
            list.add(demo);
        }
        return list;
    }

    /**
     * 构造单条VO数据
     *
     * @return DemoVO
     */
    public DemoVO buildVOData() {
        return buildVOData(1).get(0);
    }

    /**
     * 构造批量VO数据
     *
     * @param size 数量
     * @return List<DemoVO>
     */
    public List<DemoVO> buildVOData(int size) {
        List<DemoVO> list = Lists.newArrayList();
        for (int i = 1; i <= size; i++) {
            DemoVO demo = new DemoVO();
            demo.setId((long) i);
            demo.setCreateBy(1L);
            demo.setUpdateBy(1L);
            demo.setCreateTime(new Date());
            demo.setUpdateTime(new Date());
            demo.setRemoveFlag(0);
            demo.setCode("code-" + i);
            demo.setName("name-" + i);
            demo.setScore(i % 2 == 0 ? new BigDecimal("90.25") : new BigDecimal("70.55"));
            demo.setEnable(i % 2 == 0);
            demo.setRemark("remark:" + demo.getCode() + ", " + demo.getName());
            list.add(demo);
        }
        return list;
    }

    /**
     * 构造单条DTO数据
     *
     * @return DemoDTO
     */
    public DemoDTO buildDTOData() {
        return buildDTOData(1).get(0);
    }

    /**
     * 构造批量DTO数据
     *
     * @param size 数量
     * @return List<DemoDTO>
     */
    public List<DemoDTO> buildDTOData(int size) {
        List<DemoDTO> list = Lists.newArrayList();
        for (int i = 1; i <= size; i++) {
            DemoDTO demo = new DemoDTO();
            demo.setId((long) i);
            demo.setCode("code-" + i);
            demo.setName("name-" + i);
            demo.setScore(i % 2 == 0 ? new BigDecimal("90.25") : new BigDecimal("70.55"));
            demo.setEnable(i % 2 == 0);
            demo.setRemark("remark:" + demo.getCode() + ", " + demo.getName());
            list.add(demo);
        }
        return list;
    }
}
