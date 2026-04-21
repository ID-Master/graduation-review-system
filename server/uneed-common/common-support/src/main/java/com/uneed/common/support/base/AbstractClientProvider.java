package com.uneed.common.support.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.mybatis.base.SuperService;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.mybatis.utils.Conditions;
import com.uneed.common.support.convert.AbstractModelConverter;
import com.uneed.common.support.convert.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * client的提供者超类
 *
 * @param <S>   泛型参数，用来约定service
 * @param <T>   泛型参数，用来约定实体类类型
 * @param <C>   泛型参数，用来约定数据对象转换的转换器
 * @param <DTO> 泛型参数，用来约定dto数据对象类型
 * @author diablo
 * @date 2020/5/1
 * @since 1.1.0
 */
public abstract class AbstractClientProvider<S extends SuperService<T>, T extends SuperModel, C extends AbstractModelConverter, DTO extends Serializable>
        implements SuperClient<DTO> {

    //********************************************* 通用属性 *********************************************/

    /**
     * service逻辑处理器，由子类自动注入
     */
    @Autowired
    protected S service;

    /**
     * 实体类的class类型，用来做字段缓存，不需要每次都取
     */
    protected final Class<T> entityClass = getGenericType(1);

    /**
     * converter类的class类型，用来做字段缓存，不需要每次都取
     */
    protected final Class<C> converterClass = getGenericType(2);

    /**
     * DTO类的class类型，用来做字段缓存，不需要每次都取
     */
    protected final Class<DTO> dtoClass = getGenericType(3);

    /**
     * 数据对象转换器
     */
    protected final C converter = Converters.get(converterClass);

    //********************************************* SuperClient 的接口方法实现 *********************************************/

    /**
     * 新增单个数据
     */
    @PostMapping("/insert")
    @Override
    public int insert(@RequestBody DTO dto) {
        return service.insert(toEntity(dto));
    }

    /**
     * 批量新增数据
     */
    @PostMapping("/insert-batch")
    @Override
    public int insertBatch(@RequestBody List<DTO> list) {
        return service.insertBatch(toEntity(list));
    }

    /**
     * 修改单个数据
     */
    @PostMapping("/update")
    @Override
    public int update(@RequestBody DTO dto) {
        return service.update(toEntity(dto));
    }

    /**
     * 全量修改单个数据
     */
    @PostMapping("/update-full")
    @Override
    public int updateFull(@RequestBody DTO dto) {
        return service.updateForAll(toEntity(dto));
    }

    /**
     * 批量修改数据
     */
    @PostMapping("/update-batch")
    @Override
    public int updateBatch(@RequestBody List<DTO> list) {
        return service.updateBatch(toEntity(list));
    }

    /**
     * 单个数据新增或修改
     */
    @PostMapping("/insert-or-update")
    @Override
    public int insertOrUpdate(@RequestBody DTO dto) {
        return service.insertOrUpdate(toEntity(dto));
    }

    /**
     * 批量新增或修改
     */
    @PostMapping("/insert-or-update-batch")
    @Override
    public int insertOrUpdateBatch(@RequestBody List<DTO> list) {
        return service.insertOrUpdateBatch(toEntity(list));
    }

    /**
     * 根据id删除单个数据
     */
    @GetMapping("/remove-by-id")
    @Override
    public int removeById(@RequestParam("id") String id) {
        return service.removeById(id);
    }

    /**
     * 根据id集合批量删除数据
     */
    @PostMapping("/remove-by-ids")
    @Override
    public int removeByIds(@RequestBody List<String> ids) {
        return service.removeByIds(ids);
    }

    /**
     * 获取所有数据总数
     */
    @GetMapping("/count")
    @Override
    public long count() {
        return service.count();
    }

    /**
     * 根据条件对象获取数据总数
     */
    @PostMapping("/remove-by-condition")
    @Override
    public <POJO extends Serializable> long countByCondition(@RequestBody POJO condition) {
        return service.countByCondition(condition);
    }

    //********************************************* 获取泛型相关方法 *********************************************/

    /**
     * 根据索引获取当前类的泛型参数类型，获取不到，会抛异常
     *
     * @param index 泛型参数索引
     * @param <G>   泛型类型
     * @return Class<G> 泛型参数类型
     */
    protected <G> Class<G> getGenericType(int index) {
        Class<G> clazz = getGenericType(getClass(), index);
        if (isNull(clazz)) {
            throw new BusinessException("未能获取到当前继承类上的泛型参数！当前类类型[" + getClass() + "]，泛型参数索引[" + index + "]");
        }
        return clazz;
    }

    /**
     * 根据指定类型、索引获取执行类的泛型参数类型
     *
     * @param clazz 指定类型
     * @param index 泛型参数索引
     * @param <G>   泛型类型
     * @return Class<G> 泛型参数类型
     */
    @SuppressWarnings("unchecked")
    protected <G> Class<G> getGenericType(Class<?> clazz, int index) {
        return (Class<G>) BeanUtil.getSuperClassActualType(clazz, index);
    }

    //********************************************* DTO与实体类型数据转换方法 *********************************************/

    /**
     * dto对象数据转实体对象数据
     */
    @SuppressWarnings("unchecked")
    protected T toEntity(DTO dto) {
        return (T) converter.toEntity(dto);
    }

    /**
     * dto对象数据集合转实体对象数据集合
     */
    @SuppressWarnings("unchecked")
    protected List<T> toEntity(List<DTO> list) {
        return converter.toEntity(list);
    }
}
