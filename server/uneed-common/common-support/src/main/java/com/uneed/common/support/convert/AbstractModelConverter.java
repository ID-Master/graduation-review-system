package com.uneed.common.support.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.page.PageData;
import com.uneed.common.support.util.DictUtil;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

import static com.uneed.common.core.bean.BeanUtil.serializedCopy;
import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * 数据对象转换器.
 *
 * @param <T>   实体对象泛型参数
 * @param <VO>  VO对象泛型参数
 * @author diablo
 * @date 2020/4/26
 */
public abstract class AbstractModelConverter<T extends SuperModel, VO extends Serializable> {

    /**
     * 实体类的class类型，用来做字段缓存，不需要每次都取
     */
    private Class<T> entityClass;

    /**
     * VO类的class类型，用来做字段缓存，不需要每次都取
     */
    private Class<VO> voClass;

    /**
     * 实体类转VO，提供供外部调用，会做三件事情：
     * <p>
     * 1. 创建VO的新实例
     * <p>
     * 2. 将实体对象的值copy给VO实例
     * <p>
     * 3. 填充VO对象的数据字典名称
     *
     * @param entity 实体类对象
     * @return VO vo对象
     */
    public VO toVO(@NonNull T entity) {
        return isFillDict() ? fillDictionary(serializedCopy(entity, getVoClass())) : serializedCopy(entity, getVoClass());
    }

    /**
     * 实体类集合转VO集合，提供给外部调用，copy完集合对象的属性值后，也会填充数据VO对象的数据字典名称
     *
     * @param entityList 实体对象集合
     * @return List<VO> vo对象集合
     */
    public List<VO> toVO(List<T> entityList) {
        return isFillDict() ? fillDictionary(serializedCopy(entityList, getVoClass())) : serializedCopy(entityList, getVoClass());
    }

    /**
     * 实体类Page对象{@link IPage}转PageData对象{@link PageData}
     * <p>
     * 将Page对象的实体对象集合转VO对象集合后，填充到PageData对象中，并设置查询总数、当前查询页、查询页大小
     *
     * @param page page对象
     * @return PageData<VO> PageData对象
     */
    public PageData<VO> toVOPage(IPage<T> page) {
        return new PageData<>(toVO(page.getRecords()), page);
    }

    /**
     * 任意POJO对象转实体类，提供给外部调用，执行逻辑如下：
     * <p>
     * 1. 如果pojo参数的类型与当前类上泛型约定的VO类型相等，执行{@link this#voToEntity(Serializable)}方法
     * <p>
     * 3. 不符合1，2条件的情况下，执行{@link this#otherToEntity(Serializable)}方法
     * <p>
     * 4. 当前方法参数没有做泛型约定，若子类需要重写时，可以根据实际业务场景，重写voToEntity方法或dotToEntity方法，也可以针对其他转实体类型，重写otherToEntity方法
     *
     * @param pojo   pojo对象
     * @param <POJO> 泛型参数
     * @return T 实体对象
     */
    @SuppressWarnings("unchecked")
    public <POJO extends Serializable> T toEntity(@NonNull POJO pojo) {
        if (equal(pojo.getClass(), getVoClass())) {
            return voToEntity((VO) pojo);
        }
        return otherToEntity(pojo);
    }

    /**
     * 任意POJO对象集合转实体类集合，提供给外部调用，执行逻辑同{@link this#toEntity(Serializable)}
     *
     * @param pojoList pojo对象集合
     * @param <POJO>   泛型参数
     * @return List<T> 实体对象集合
     */
    @SuppressWarnings("unchecked")
    public <POJO extends Serializable> List<T> toEntity(List<POJO> pojoList) {
        if (isEmpty(pojoList)) {
            return Lists.newArrayList();
        }
        Class<?> clazz = pojoList.get(0).getClass();
        if (equal(clazz, getVoClass())) {
            return voToEntity((List<VO>) pojoList);
        }
        return otherToEntity(pojoList);
    }

    /**
     * 用来标记是否需要自动填充数据字典，默认为false
     *
     * @return boolean
     */
    protected boolean isFillDict() {
        return false;
    }

    /**
     * 填充pojo对象数据字典的名称
     *
     * @param pojo   pojo对象
     * @param <POJO> 泛型参数
     * @return POJO 填充之后的pojo对象
     */
    protected <POJO extends Serializable> POJO fillDictionary(POJO pojo) {
        DictUtil.set(pojo);
        return pojo;
    }

    /**
     * 填充pojo对象集合数据字典的名称
     *
     * @param pojoList pojo对象集合
     * @param <POJO>   泛型参数
     * @return List<POJO> 填充之后的pojo对象集合
     */
    protected <POJO extends Serializable> List<POJO> fillDictionary(List<POJO> pojoList) {
        DictUtil.set(pojoList);
        return pojoList;
    }

    /**
     * VO对象转实体类对象，默认实现为实例化一个新的实体对象，执行拷贝数据后返回
     * <p>
     * 当前方法不对外提供，子类可以重写其实现进行复杂数据处理
     *
     * @param vo vo对象
     * @return T 实体对象
     */
    protected T voToEntity(@NonNull VO vo) {
        return serializedCopy(vo, getEntityClass());
    }

    /**
     * VO对象集合转实体类对象集合，执行逻辑同{@link this#voToEntity(Serializable)}
     * <p>
     * 当前方法不对外提供，子类可以重写其实现进行复杂数据处理
     *
     * @param voList vo对象集合
     * @return List<T> 实体对象集合
     */
    protected List<T> voToEntity(List<VO> voList) {
        return serializedCopy(voList, getEntityClass());
    }

    /**
     * 其他pojo类型对象转实体类对象，默认实现为实例化一个新的实体对象，执行拷贝数据后返回
     * <p>
     * 当前方法不对外提供，子类可以重写其实现进行复杂数据处理
     *
     * @param pojo 其他类型对象
     * @return T 实体对象
     */
    protected <POJO extends Serializable> T otherToEntity(POJO pojo) {
        return serializedCopy(pojo, getEntityClass());
    }

    /**
     * 其他pojo类型对象集合转实体类对象集合，执行逻辑同{@link this#otherToEntity(Serializable)}
     * <p>
     * 当前方法不对外提供，子类可以重写其实现进行复杂数据处理
     *
     * @param pojoList 其他pojo类型对象集合
     * @return List<T> 实体对象集合
     */
    protected <POJO extends Serializable> List<T> otherToEntity(List<POJO> pojoList) {
        return serializedCopy(pojoList, getEntityClass());
    }

    /**
     * 实体类类型，取当前实例化对象类的第一个泛型参数类型
     *
     * @return Class<T> 实体类类型
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        if (isNull(entityClass)) {
            entityClass = (Class<T>) BeanUtil.getSuperClassActualType(getClass(), 0);
        }
        return entityClass;
    }

    /**
     * VO类类型，取当前实例化对象类的第二个泛型参数类型
     *
     * @return Class<VO> VO类类型
     */
    @SuppressWarnings("unchecked")
    protected Class<VO> getVoClass() {
        if (isNull(voClass)) {
            voClass = (Class<VO>) BeanUtil.getSuperClassActualType(getClass(), 1);
        }
        return voClass;
    }
}
