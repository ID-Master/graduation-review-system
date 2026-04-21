package com.uneed.common.support.excel;

import com.uneed.common.core.lang.Validate;
import com.uneed.common.mybatis.base.SuperService;
import com.uneed.common.support.convert.AbstractModelConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * excel导入监听器@{@link AbstractImportListener}的默认实现
 * <p>
 * 实例化类的时候，接收3个参数，且是必须的参数：
 * <p>
 * 1. service，{@link SuperService}的现实类类，用来处理数据保存的service类
 * <p>
 * 2. converter，{@link AbstractModelConverter}的实现类，用来处理数据转换，将导入解析的类型，转换为需要保存的实体类型
 * <p>
 *
 * @author diablo
 * @date 2020/4/29
 */
@Slf4j
public class DefaultImportListener extends AbstractImportListener<Object> {

    @SuppressWarnings("rawtypes")
    private final SuperService service;

    @SuppressWarnings("rawtypes")
    private final AbstractModelConverter converter;

    /**
     * 构造函数，必须指定service、converter
     */
    @SuppressWarnings("rawtypes")
    public DefaultImportListener(SuperService service, AbstractModelConverter converter) {
        this.service = service;
        Validate.notNull(service, "service can't be null!");
        this.converter = converter;
        Validate.notNull(converter, "converter can't be null!");
    }

    /**
     * 重写导入解析完成的方法，将解析后的数据做入库处理
     *
     * @param dataList 数据列表
     */
    @Override
    protected void doFinish(List<Object> dataList) {
        @SuppressWarnings("unchecked")
        int result = service.insertOrUpdateBatch(converter.toEntity(dataList));
        log.info("default listener import data finished. result={}, service={}, converter={}", result, service.getClass(),
                converter.getClass());
    }

    @Override
    protected void doNext(Object data, int index, List<String> messages) {

    }

    @Override
    protected String buildMessage(int index, List<String> messages) {
        return super.buildMessage(index, messages);
    }

    @Override
    protected boolean isThrowMessage() {
        return super.isThrowMessage();
    }

    @Override
    protected boolean isFilterError() {
        return super.isFilterError();
    }
}
