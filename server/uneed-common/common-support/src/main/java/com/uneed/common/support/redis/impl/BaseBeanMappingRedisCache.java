package com.uneed.common.support.redis.impl;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.lang.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.isNull;
import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;
import static com.uneed.common.support.constant.KeyConstant.BEAN_MAPPING;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2020/1/16
 */
public abstract class BaseBeanMappingRedisCache<T, ID extends Serializable, V extends List<E>, E> extends BaseBeanRedisCache<T, ID, List<E>> {

    //************************************************* 私有属性 *******************************************************************************/

    /**
     * 集合元素值的获取方法，私有属性，用来缓存，使用必须通过getIdGetterMethod()方法获取
     */
    private Method elementGetterMethod;

    //************************************************* 公用方法 *******************************************************************************/

    /**
     * 重写父类批量添加bean至缓存中的方法，实现在批量添加时，去合并已存在的数据项
     *
     * @param beans bean对象集合
     */
    @Override
    public void add(Collection<T> beans) {
        Map<String, List<E>> map = buildBeanMap(beans);
        //如果缓存库中存在，则取取出来与添加的值做合并处理后再写入缓存
        for (String key : map.keySet()) {
            List<E> elements = concatElements(getHash().get(key), map.get(key));
            if (elements.isEmpty()) {
                continue;
            }
            map.put(key, elements);
        }
        if (map.isEmpty()) {
            return;
        }
        getHash().putAll(map);
    }

    /**
     * 重写父类根据bean删除缓存的方法，实现根据主键+映射值的方式删除
     *
     * @param bean bean对象
     */
    @Override
    public void delBean(T bean) {
        String key = getKey(getId(bean));
        if (StringUtil.isEmpty(key)) {
            return;
        }
        delMapping(key, Lists.newArrayList(getElement(bean)));
    }

    /**
     * 重写父类根据bean集合删除缓存的方法，实现根据主键+映射值集合的方式删除
     *
     * @param beans bean对象集合
     */
    @Override
    public void delBean(Collection<T> beans) {
        Map<String, List<E>> map = buildBeanMap(beans);
        for (String key : map.keySet()) {
            delMapping(key, map.get(key));
        }
    }

    /**
     * 重写父类初始化缓存的方法，先做清理数据，再重写添加全部数据，不用去做加载已存在数据处理
     *
     * @param beans bean对象集合
     */
    @Override
    public void init(Collection<T> beans) {
        super.clear();
        Map<String, List<E>> map = buildBeanMap(beans);
        if (map.isEmpty()) {
            return;
        }
        getHash().putAll(map);
    }

    //************************************************* 重写父类方法 *******************************************************************************/

    /**
     * 获取hash的key值，组成：BEAN_MAPPING + ":" + prefix()
     *
     * @return String hash key 值
     */
    @Override
    protected String getHashKey() {
        return BEAN_MAPPING + ":" + prefix();
    }

    /**
     * 重写父类获取缓存值的方法，从redis缓存中获取值列表，合并后再返回
     *
     * @param bean bean对象
     * @return List<E> 合并之后的值集
     */
    @Override
    protected List<E> getValue(T bean, String key) {
        List<E> elements = addElement(getHash().get(key), getElement(bean));
        return elements.isEmpty() ? null : elements;
    }

    /**
     * 重写父类构建beanMap对象时，处理映射关系的逻辑
     *
     * @param bean bean对象
     * @param key  map的key
     * @param map  构建的beanMap对象
     * @return 映射值集
     */
    @Override
    protected List<E> getValue(T bean, String key, Map<String, List<E>> map) {
        List<E> elements = addElement(map.get(key), getElement(bean));
        return elements.isEmpty() ? null : elements;
    }

    //************************************************* 获取 id *******************************************************************************/

    /**
     * 获取集合元素字段名称
     *
     * @return 集合元素字段名称
     */
    protected abstract String elementField();

    /**
     * 获取集合元素的反射方法
     *
     * @return Method 反射方法
     */
    protected Method getElementGetterMethod() {
        if (isNull(elementGetterMethod)) {
            elementGetterMethod = BeanUtil.getReadMethod(getBeanClass(), elementField());
        }
        return elementGetterMethod;
    }

    /**
     * 获取集合元素值的方法
     *
     * @param bean bean对象
     * @return 集合元素值
     */
    @SuppressWarnings("ALL")
    protected E getElement(T bean) {
        try {
            return (E) getElementGetterMethod().invoke(bean);
        } catch (Exception e) {
            log.error("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取集合元素element失败：" + e.getMessage(), e);
            throw new BusinessException("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取集合元素element失败：" + e.getMessage());
        }
    }

    //************************************************* 其他 *******************************************************************************/

    /**
     * 删除缓存数据处理，会只处理参数给出来的集合
     *
     * @param key      redis缓存键
     * @param elements 需要删除的集合元素
     */
    protected void delMapping(String key, List<E> elements) {
        List<E> values = diffElements(getHash().get(key), elements);
        if (values.isEmpty()) {
            getHash().delete(key);
            return;
        }
        getHash().put(key, values);
    }

    /**
     * 获取俩个数据集的差集
     *
     * @param values   已存在的数据集
     * @param elements 需要添加的数据集
     * @return List<E> 差集处理后的结果
     */
    private List<E> diffElements(List<E> values, List<E> elements) {
        List<E> list = nullToDefault(values, Lists.newArrayList());
        if (CollectionUtil.isNotEmpty(elements)) {
            list.removeAll(elements);
        }
        return list;
    }

    /**
     * 获取俩个数据集的并集
     *
     * @param values   缓存中数据集
     * @param elements 需要添加的数据
     * @return 获取并集并去重复之后的集合
     */
    protected List<E> concatElements(List<E> values, List<E> elements) {
        return CollectionUtil.contact(values, elements);
    }

    /**
     * 缓存数据集中添加元素
     *
     * @param values  缓存数据集
     * @param element 需要添加的元素
     * @return List<E> 添加后的数据集
     */
    private List<E> addElement(List<E> values, E element) {
        //不添加空元素
        if (isNull(element)) {
            return Lists.newArrayList();
        }

        //如果缓存数据集为空
        if (isNull(values)) {
            return Lists.newArrayList(element);
        }

        //集合中如果没有包含需要添加的元素
        if (!values.contains(element)) {
            values.add(element);
        }

        return values;
    }

}
