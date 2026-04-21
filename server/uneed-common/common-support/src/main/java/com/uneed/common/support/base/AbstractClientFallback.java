package com.uneed.common.support.base;

import com.uneed.common.core.exception.unchecked.FeignClientException;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/5/2
 */
@Slf4j
public abstract class AbstractClientFallback<DTO extends Serializable> implements SuperClient<DTO> {

    @Override
    public int insert(DTO dto) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "insert", "dto=" + dto);
        throw new FeignClientException("execute " + getClass() + "#insert(Object) error!");
    }

    @Override
    public int insertBatch(List<DTO> list) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "insertBatch", "list=" + list);
        throw new FeignClientException("execute " + getClass() + "#insertBatch(List<Object>) error!");
    }

    @Override
    public int update(DTO dto) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "update", "dto=" + dto);
        throw new FeignClientException("execute " + getClass() + "#update(Object) error!");
    }

    @Override
    public int updateFull(DTO dto) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "updateFull", "dto=" + dto);
        throw new FeignClientException("execute " + getClass() + "#updateFull(List<Object>) error!");
    }

    @Override
    public int updateBatch(List<DTO> list) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "updateBatch", "list=" + list);
        throw new FeignClientException("execute " + getClass() + "#updateBatch(List<Object>) error!");
    }

    @Override
    public int insertOrUpdate(DTO dto) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "insertOrUpdate", "dto=" + dto);
        throw new FeignClientException("execute " + getClass() + "#insertOrUpdate(Object) error!");
    }

    @Override
    public int insertOrUpdateBatch(List<DTO> list) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "insertOrUpdateBatch", "list=" + list);
        throw new FeignClientException("execute " + getClass() + "#insertOrUpdateBatch(List<Object>) error!");
    }

    @Override
    public int removeById(String id) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "removeById", "id=" + id);
        throw new FeignClientException("execute " + getClass() + "#removeById(Long) error!");
    }

    @Override
    public int removeByIds(List<String> ids) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "removeByIds", "ids=" + ids);
        throw new FeignClientException("execute " + getClass() + "#removeByIds(List<Long>) error!");
    }

    @Override
    public long count() {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "count", "无");
        throw new FeignClientException("execute " + getClass() + "#count() error!");
    }

    @Override
    public <POJO extends Serializable> long countByCondition(POJO condition) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "countByCondition", "condition=" + condition);
        throw new FeignClientException("execute " + getClass() + "#countByCondition(Object) error!");
    }

    @Override
    public DTO getById(String id) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "getById", "id=" + id);
        throw new FeignClientException("execute " + getClass() + "#getById(Long) error!");
    }

    @Override
    public <POJO extends Serializable> DTO getByCondition(POJO condition) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "getByCondition", "condition=" + condition);
        throw new FeignClientException("execute " + getClass() + "#getByCondition(Object) error!");
    }

    @Override
    public List<DTO> list() {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "list", "无");
        throw new FeignClientException("execute " + getClass() + "#list() error!");
    }

    @Override
    public List<DTO> listByIds(List<Long> ids) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "listByIds", "ids=" + ids);
        throw new FeignClientException("execute " + getClass() + "#listByIds(List<Long>) error!");
    }

    @Override
    public <POJO extends Serializable> List<DTO> listByCondition(POJO condition) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "listByCondition", "condition=" + condition);
        throw new FeignClientException("execute " + getClass() + "#listByCondition(Object) error!");
    }

    @Override
    public <POJO extends Serializable> PageData<DTO> pageBySearch(PageSearch<POJO> search) {
        log.error("调用client[{}]的{}方法异常，执行熔断！参数{}}", getClass(), "pageBySearch", "search=" + search);
        throw new FeignClientException("execute " + getClass() + "#pageBySearch(Object) error!");
    }
}
