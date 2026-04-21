package com.uneed.common.support.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.core.text.StringFormatter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.isNotEmpty;

/**
 * excel导入监听器的超类，继承了excel工具类的监听器{@link AnalysisEventListener}
 *
 * @param <T> 用来承载解析数据的泛型参数
 * @author diablo
 * @date 2020/4/29
 */
@Slf4j
public abstract class AbstractImportListener<T> extends AnalysisEventListener<T> {

    /**
     * 用来封装解析excel后得到的数据集
     */
    private final List<T> list = Lists.newArrayList();

    /**
     * 导入完成数量
     */
    private int finishCount;

    /**
     * 用来封装逐行解析时的异常消息，子类可读取
     */
    protected Map<Integer, List<String>> messageMap = Maps.newHashMap();

    /**
     * 用来承载检查异常的数据集合，子类可读取
     */
    protected List<T> exceptionList = Lists.newArrayList();


    /**
     * 重写父类的解析单个数据方法，得到索引，再分派给doNext执行，最后将解析完成的数据添加到集合中
     *
     * @param data    解析后的数据 {@link AnalysisContext#readRowHolder()}
     * @param context excel上下文信息
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        //获取导入的记录行号
        int index = context.readRowHolder().getRowIndex();
        if (log.isDebugEnabled()) {
            log.debug("analysis finish one row, index={}, data={}", index, JsonUtil.toJson(data));
        }
        List<String> messages = Lists.newArrayList();
        doNext(data, index, messages);
        //记录导入校验一次信息
        if (isNotEmpty(messages)) {
            messageMap.put(index, messages);
            exceptionList.add(data);
        }
        list.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (log.isDebugEnabled()) {
            log.debug("analysis finish all rows, size={}, exceptions={}, data={}", list.size(), exceptionList.size(),
                    JsonUtil.toJson(list));
        }
        //判断是否有错误信息
        if (isNotEmpty(messageMap)) {
            String message = buildMessage();
            log.info("exists import error message. message={}", message);
            //不是强制执行的情况下，会抛业务异常
            if (isThrowMessage()) {
                throw new BusinessException(message);
            }
        }
        //是否过滤存在校验异常的数据
        if (isFilterError()) {
            list.removeIf(t -> exceptionList.contains(t));
        }
        doFinish(list);
        finishCount = list.size();
        log.info("------------- >> import data finished. import size={}.", finishCount);
    }

    /**
     * 最终导入完成数量，在执行完成{@link this#doFinish(List)}后，会将list的数量写入到importSize属性中
     *
     * @return int
     */
    public int importSize() {
        return finishCount;
    }

    /**
     * 解析完全部数据后，需要执行的逻辑处理，子类必须实现
     *
     * @param dataList 数据列表
     */
    protected abstract void doFinish(List<T> dataList);

    /**
     * 解析完单条数据后，需要执行的逻辑处理，子类必须实现
     *
     * @param data     解析的单条数据
     * @param index    当前对应的索引
     * @param messages 错误消息集合，用来记录逐个解析数据时存在的错误信息
     */
    protected abstract void doNext(T data, int index, List<String> messages);

    /**
     * 根据索引、错误信息集合，构建当前索引行的错误信息，子类也可以根据需求情况，重写该方法，实现自定义构建错误信息
     *
     * @return String
     */
    protected String buildMessage() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<String>> entry : messageMap.entrySet()) {
            sb.append(buildMessage(entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

    /**
     * 根据索引、错误信息集合，构建当前索引行的错误信息，子类也可以根据需求情况，重写该方法，实现自定义构建错误信息
     *
     * @param index    导入行索引
     * @param messages 错误信息集合
     * @return String
     */
    protected String buildMessage(int index, List<String> messages) {
        return StringFormatter.format("第{}行：{}；", index, String.join("，", messages));
    }

    /**
     * 用来标记是否需要抛出异常消息，如果值为false，会忽略异常消息，继续执行{@link this#doFinish(List)}方法
     *
     * @return boolean
     */
    protected boolean isThrowMessage() {
        return true;
    }

    /**
     * 是否过滤解析错误的数据
     *
     * @return boolean
     */
    protected boolean isFilterError() {
        return true;
    }
}
