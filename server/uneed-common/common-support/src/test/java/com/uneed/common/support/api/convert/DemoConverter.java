package com.uneed.common.support.api.convert;

import com.uneed.common.support.api.entity.Demo;
import com.uneed.common.support.api.pojo.DemoDTO;
import com.uneed.common.support.api.pojo.DemoVO;
import com.uneed.common.support.convert.AbstractModelConverter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/27
 */
public class DemoConverter extends AbstractModelConverter<Demo, DemoVO> {

    @Override
    protected Demo voToEntity(@NonNull DemoVO vo) {
        Demo demo = super.voToEntity(vo);
        demo.setRemark("重写 voToEntity");
        return demo;
    }

    @Override
    protected List<Demo> voToEntity(List<DemoVO> voList) {
        return nullToDefault(voList, new ArrayList<DemoVO>()).stream().map(this::voToEntity).collect(Collectors.toList());
    }

}
