package com.uneed.common.dict.impl;

import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.AreaRepertory;
import com.uneed.common.dict.entity.Area;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

import static com.uneed.common.core.collection.CollectionUtil.contact;
import static com.uneed.common.core.collection.CollectionUtil.getPropertyList;
import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.dict.constant.AreaConstant.*;

/**
 * 行政区域redis缓存操作实现类，行政区域在redis中的存储方式，主要由2部分组成：
 * 1. 会用一个
 *
 * @author diablo
 * @date 2019/12/19
 */
@Slf4j
public class RedisAreaRepertory implements AreaRepertory {

    /**
     * redisTemplate对象，需要子类注入
     */
    private final RedisTemplate<String, Area> redisTemplate;


    /**
     * 带redisTemplate参数的构造函数
     *
     * @param redisTemplate redisTemplate对象
     */
    public RedisAreaRepertory(RedisTemplate<String, Area> redisTemplate) {
        Validate.notNull(redisTemplate, "redisTemplate can't be null!");
        this.redisTemplate = redisTemplate;
    }

    //////////////////////////////////////// 接口方法实现 /////////////////////////////////////////////////////////////////////

    /**
     * 根据行政区域编号获取行政区域数据对象
     */
    @Override
    public Area get(String code) {
        return isNotEmpty(code) ? getHash().get(code) : null;
    }

    /**
     * 根据行政区域编号获取其父级行政区域数据对象
     */
    @Override
    public Area parent(String code) {
        return get(getProperty(code, FIELD_PARENT));
    }

    /**
     * 根据行政区域编号获取行政区域名称
     */
    @Override
    public String getName(String code) {
        return getProperty(code, FIELD_NAME);
    }

    /**
     * 根据行政区域编号集合获取行政区域对象集合
     */
    @Override
    public List<Area> list(Collection<String> codes) {
        return isNotEmpty(codes) ? CollectionUtil.filterNull(getHash().multiGet(codes)) : Lists.newArrayList();
    }

    @Override
    public List<Area> subList(String code, boolean isFull) {
        List<Area> list = isFull ? fullSubList(code) : list(getProperty(code, FIELD_SUBSET));
        Collections.sort(list);
        return list;
    }

    /**
     * 新增行政区域数据
     */
    @Override
    public void add(Area area) {
        if (isNull(area) || isEmpty(area.getCode())) {
            return;
        }
        //1. 设置默认值
        buildDefault(area);
        //2. 设置行政区域子集，参数行政区域的子集+缓存中行政区域子集的组合
        area.setSubset(contact(area.getSubset(), getProperty(area.getCode(), FIELD_SUBSET)));
        //3. 保存行政区域
        getHash().put(area.getCode(), area);
        //4. 获取父级行政区域，并添加至其子集中
        Area parent = get(area.getParent());
        if (isNotNull(parent)) {
            parent.addSubset(area.getCode());
            getHash().put(parent.getCode(), parent);
        }
    }

    /**
     * 新增行政区域数据
     */
    @Override
    public void add(Collection<Area> areas) {
        if (isNotEmpty(areas)) {
            areas.forEach(this::add);
        }
    }

    /**
     * 根据行政区域编号，从缓存中删除行政区域数据，会删除其对应的所有子集
     */
    @Override
    public void del(String code) {
        if (isNotEmpty(code)) {
            //先删除所有子集
            Object[] array = getPropertyList(subList(code, true), FIELD_CODE).toArray();
            if (array.length > 0) {
                getHash().delete(array);
            }
            //删除编号对应的行政区域
            getHash().delete(code);
        }
    }

    /**
     * 根据行政区域编号集合，从缓存中删除行政区域信息，会删除其对应的所有子集
     */
    @Override
    public void del(Collection<String> codes) {
        if (isNotEmpty(codes)) {
            codes.forEach(this::del);
        }
    }

    /**
     * 清空所有行政区域数据
     */
    @Override
    public void clear() {
        TimeMeter meter = new TimeMeter();
        //执行删除操作
        Boolean result = redisTemplate.delete(AREA_REDIS_KEY);
        if (log.isDebugEnabled()) {
            log.debug("----------->> <<<清空>>>所有【行政区域】数据完成！结果：{}，用时：{}ms", result, meter.sign());
        }
    }

    /**
     * 初始化缓存中的行政区域数据
     */
    @Override
    public void initialization(Collection<Area> areas) {
        TimeMeter meter = new TimeMeter();
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【行政区域】数据【开始】，redis库中数量：{}，初始化数量：{} ============", getHash().size(), isNotNull(areas) ? areas.size() : 0);
        }
        //1. 先清空缓存数据
        clear();
        if (isNotEmpty(areas)) {
            meter.sign("clear");
            //2. 删除编号为空的数据
            areas.removeIf(a -> isEmpty(a.getCode()));
            if (log.isDebugEnabled()) {
                log.debug("---------- 过来编号为空的行政数据数据，过滤后的数量：{}，用时：{}ms ----------", areas.size(), meter.sign("clear", "filter"));
            }
            //3. 设置默认父级编号、排序
            areas.forEach(this::buildDefault);
            if (log.isDebugEnabled()) {
                log.debug("---------- 设置默认数据，用时：{}ms ----------", meter.sign("filter", "default"));
            }
            //4. 构建根级行政区域
            areas.add(buildRoot());
            if (log.isDebugEnabled()) {
                log.debug("---------- 构建根级行政区域，用时：{}ms ----------", meter.sign("default", "root"));
            }
            //5. 为行政区域子集赋值
            buildSubset(areas);
            if (log.isDebugEnabled()) {
                log.debug("---------- 行政区域子集赋值，用时：{}ms ----------", meter.sign("root", "subset"));
            }
            //6. 批次执行添加数据到缓存
            group(new ArrayList<>(areas)).forEach(a -> getHash().putAll(toMap(a, "code")));
            if (log.isDebugEnabled()) {
                log.debug("---------- 批次执行添加数据到缓存，数量：{}，用时：{}ms ----------", areas.size(), meter.sign("subset", "batch"));
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【行政区域】数据<<<完成>>>，行政区域数量：{}，总耗时{}ms ============", getHash().size(), meter.sign());
        }
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为行政区域数据集合构建一个hash对象
     */
    @Override
    public BoundHashOperations<String, String, Area> getHash() {
        return this.redisTemplate.boundHashOps(AREA_REDIS_KEY);
    }

    //////////////////////////////////////// 私有方法 /////////////////////////////////////////////////////////////////////

    /**
     * 构建跟节点，在初始化缓存信息时使用
     *
     * @return Area 跟节点的行政区域
     */
    private Area buildRoot() {
        Area area = new Area();
        area.setCode(ROOT_KEY);
        area.setName(ROOT_NAME);
        area.setFullName(area.getName());
        area.setSort(0D);
        return area;
    }

    /**
     * 构建子集行政区域编号，初始化缓存信息时使用
     *
     * @param areas 行政区域集合
     */
    private void buildSubset(Collection<Area> areas) {
        Map<String, List<String>> map = buildSubsetMap(areas);
        areas.forEach(a -> a.setSubset(nullToDefault(map.get(a.getCode()), Lists.newArrayList())));
    }

    /**
     * 构建行政区域与其子集映射的map集合，key为行政区域编码，value为子集编码
     *
     * @param areas 行政区域集合
     */
    private Map<String, List<String>> buildSubsetMap(Collection<Area> areas) {
        Map<String, List<String>> map = Maps.newHashMap();
        areas.stream().filter(a -> isNotEmpty(a.getParent())).forEach(a -> addSubset(a, map));
        return map;
    }

    /**
     * 添加行政区域编码到子集集合中
     *
     * @param area 行政区域对象
     * @param map  行政区域子集映射的map集合
     */
    private void addSubset(Area area, Map<String, List<String>> map) {
        List<String> list = getSubset(area.getParent(), map);
        if (!list.contains(area.getCode())) {
            list.add(area.getCode());
        }
    }

    /**
     * 根据行政区域编号，从子集映射map集合中获取子集编号集合
     *
     * @param code 行政区域编号
     * @param map  子集映射map集合
     * @return List<String> 行政区域编号对应的子集编号集合
     */
    private List<String> getSubset(String code, Map<String, List<String>> map) {
        List<String> list = map.get(code);
        if (isNull(list)) {
            list = Lists.newArrayList();
            map.put(code, list);
        }
        return list;
    }

    /**
     * 构建行政区域默认值，这里主要是设置默认的父级编号、排序字段
     *
     * @param area 行政区域对象
     */
    private void buildDefault(Area area) {
        if (isEmpty(area.getParent())) {
            area.setParent(ROOT_KEY);
        }
        if (isNull(area.getSort())) {
            area.setSort(convert(area.getCode(), 1D));
        }
    }

    /**
     * 根据行政区域编号、行政区域对象属性名称，获取映射的属性值。
     * <p>
     * 改方法会先根据行政区域编号，从缓存中获取行政区域对象，再根据属性名称，得到属性值
     *
     * @param code         行政区域名称
     * @param propertyName 行政区域对象属性名称
     * @param <V>          值泛型
     * @return V 属性映射的值
     */
    @SuppressWarnings("unchecked")
    private <V> V getProperty(String code, String propertyName) {
        Area area = get(code);
        if (isNull(area)) {
            return null;
        }
        switch (propertyName) {
            case FIELD_NAME:
                return (V) area.getName();
            case FIELD_PARENT:
                return (V) area.getParent();
            case FIELD_SUBSET:
                return (V) area.getSubset();
            default:
                throw new BusinessException("Unsupported attribute name[" + propertyName + "]");
        }
    }

    /**
     * 根据行政区域编号，获取所有子集行政区域，包含子集的子集
     *
     * @param code 行政区域编号
     * @return List<Area> 全量子集信息
     */
    private List<Area> fullSubList(String code) {
        List<Area> list = Lists.newArrayList();
        buildSubList(code, list);
        return list;
    }

    /**
     * 递归构建行政区域全量子集，添加到行政区域集合中
     *
     * @param code 行政区域编号
     * @param list 行政区域集合
     */
    private void buildSubList(String code, List<Area> list) {
        List<Area> subList = subList(code);
        if (isNotEmpty(subList)) {
            list.addAll(subList);
            for (Area sub : subList) {
                buildSubList(sub.getCode(), list);
            }
        }
    }
}
