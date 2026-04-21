package com.uneed.common.dict.impl;

import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.DictRepertory;
import com.uneed.common.dict.entity.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.dict.constant.DictConstant.DICT_REDIS_KEY;

/**
 * 字典缓存操作接口的默认实现
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/17
 */
@Slf4j
public class RedisDictRepertory implements DictRepertory {

    /**
     * redisTemplate对象，需要注入
     */
    private final RedisTemplate<String, Dict> redisTemplate;

    /**
     * 根级字典的key值
     */
    private static final String ROOT_KEY = "ROOT";

    /**
     * 字典数据在redis中的key值，可以注入
     */
    private String redisKey = DICT_REDIS_KEY;

    /**
     * 带redisTemplate参数的构造函数
     *
     * @param redisTemplate redisTemplate对象
     */
    public RedisDictRepertory(RedisTemplate<String, Dict> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 带redisTemplate参数的构造函数
     *
     * @param redisTemplate redisTemplate对象
     * @param redisKey      字典在redis中的key值
     */
    public RedisDictRepertory(RedisTemplate<String, Dict> redisTemplate, String redisKey) {
        this(redisTemplate);
        this.redisKey = redisKey;
    }

    //////////////////////////////////////// 接口方法实现 /////////////////////////////////////////////////////////////////////

    /**
     * 根据字典的key获取单条数据字典记录
     */
    @Override
    public Dict get(String key) {
        return isNotEmpty(key) ? getHash().get(key) : null;
    }

    /**
     * 根据字典key集合获取key对应的字典记录集合
     */
    @Override
    public List<Dict> list(Collection<String> keys) {
        return isNotEmpty(keys) ? CollectionUtil.filterNull(getHash().multiGet(keys)) : Lists.newArrayList();
    }

    /**
     * 根据字典key，获取其对应的子集
     */
    @Override
    public List<Dict> subset(String key) {
        Dict dict = get(key);
        return isNotNull(dict) ? sort(list(dict.getSubset())) : Lists.newArrayList();
    }

    /**
     * 根据字典key，获取其对应的子集，isEnabled参数用来标记是否只取有效的数据
     */
    @Override
    public List<Dict> subset(String key, boolean isEnabled) {
        return isEnabled ? enabledList(subset(key)) : subset(key);
    }

    /**
     * 新增数据字典
     */
    @Override
    public void add(Dict dict) {
        if (isNull(dict) || isEmpty(dict.getKey())) {
            return;
        }
        //1. 设置数据字典的默认值
        setDefaultValue(dict);
        //2. 设置数据字典子集，参数的子集+缓存中数据字典子集的组合
        setDictSubset(dict);
        //3. 将新增的字典保存到缓存中
        getHash().put(dict.getKey(), dict);
        //4. 获取父级数据字典，并添加至其子集中
        appendToParent(dict);
    }

    /**
     * 批量新增数据字典
     */
    @Override
    public void add(Collection<Dict> beans) {
        if (isNotEmpty(beans)) {
            beans.forEach(this::add);
        }
    }

    /**
     * 根据字典的key删除其对应的所有子集字典数据
     */
    @Override
    public void del(String key) {
        if (isNotEmpty(key)) {
            //先删除所有子集
            Object[] array = subset(key).stream().map(Dict::getKey).toArray();
            if (array.length > 0) {
                getHash().delete(array);
            }
            //删除key对应的数据字典
            getHash().delete(key);
        }
    }

    /**
     * 根据传入的集合删除其对应的字典数据，并删除其对应的子集
     */
    @Override
    public void del(Collection<String> beans) {
        if (CollectionUtil.isNotEmpty(beans)) {
            beans.forEach(this::del);
        }
    }

    /**
     * 清空所有字典数据
     */
    @Override
    public void clear() {
        TimeMeter meter = new TimeMeter();
        //执行删除操作
        Boolean result = redisTemplate.delete(redisKey);
        if (log.isDebugEnabled()) {
            log.debug("----------->> <<<清空>>>所有【数据字典】数据完成！结果：{}，用时：{}ms", result, meter.sign());
        }
    }

    @Override
    public void initialization(Collection<Dict> beans) {
        TimeMeter meter = new TimeMeter();
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【数据字典】数据【开始】，redis库中数量：{}，初始化数量：{} ============", getHash().size(), isNotNull(beans) ? beans.size() : 0);
        }
        //1. 先清空缓存数据
        clear();
        if (isNotEmpty(beans)) {
            meter.sign("clear");
            //2. 删除编号为空的数据
            beans.removeIf(dict -> isEmpty(dict.getKey()));
            if (log.isDebugEnabled()) {
                log.debug("---------- 过滤编号为空的数据字典数据，过滤后的数量：{}，用时：{}ms ----------", beans.size(), meter.sign("clear", "filter"));
            }
            //3. 设置默认父级编号、排序
            beans.forEach(this::setDefaultValue);
            if (log.isDebugEnabled()) {
                log.debug("---------- 设置数据字典的默认值，用时：{}ms ----------", meter.sign("filter", "default"));
            }
            //4. 构建根级数据字典对象
            beans.add(buildRoot());
            if (log.isDebugEnabled()) {
                log.debug("---------- 构建根级数据字典，用时：{}ms ----------", meter.sign("default", "root"));
            }
            //5. 为数据字典的子集赋值
            buildSubset(beans);
            if (log.isDebugEnabled()) {
                log.debug("---------- 构建数据字典的子集key集合，用时：{}ms ----------", meter.sign("root", "subset"));
            }
            //6. 批次执行添加数据字典到缓存
            group(new ArrayList<>(beans)).forEach(this::addDictList);
            if (log.isDebugEnabled()) {
                log.debug("---------- 批次执行添加数据字典到缓存，数量：{}，用时：{}ms ----------", beans.size(), meter.sign("subset", "batch"));
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【数据字典】数据<<<完成>>>，数据字典数量：{}，总耗时{}ms ============", getHash().size(), meter.sign());
        }
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为每一个字典集合构建一个hash对象
     */
    @Override
    public BoundHashOperations<String, String, Dict> getHash() {
        Validate.notNull(this.redisTemplate, "redisTemplate can't be null!");
        return this.redisTemplate.boundHashOps(redisKey);
    }

    //////////////////////////////////////// 私有方法 /////////////////////////////////////////////////////////////////////

    /**
     * 字典数据排序
     */
    private List<Dict> sort(List<Dict> list) {
        Collections.sort(list);
        return list;
    }

    /**
     * 获取字典集合中所有有效的数据
     */
    private List<Dict> enabledList(List<Dict> list) {
        return list.stream().filter(Dict::getEnable).collect(Collectors.toList());
    }

    /**
     * 添加数据字典至缓存中
     *
     * @param list 数据字典集合
     */
    private void addDictList(List<Dict> list) {
        Map<String, Dict> dictMap = list.stream().collect(Collectors.toMap(Dict::getKey, dict -> dict));
        getHash().putAll(dictMap);
    }

    /**
     * 构建数据字典的子集key集合，初始化缓存信息时使用
     *
     * @param beans 数据字典集合
     */
    private void buildSubset(Collection<Dict> beans) {
        Map<String, List<String>> map = buildSubsetMap(beans);
        beans.forEach(a -> a.setSubset(nullToDefault(map.get(a.getKey()), Lists.newArrayList())));
    }

    /**
     * 构建数据字典与其子集映射的map集合，key为数据字典的key，value为子集key集合
     *
     * @param beans 数据字典集合
     */
    private Map<String, List<String>> buildSubsetMap(Collection<Dict> beans) {
        Map<String, List<String>> map = Maps.newHashMap();
        beans.stream().filter(a -> isNotEmpty(a.getParent())).forEach(a -> appendToSubset(a, map));
        return map;
    }

    /**
     * 添加数据字典的key到其父级字典的子集key集合中
     *
     * @param dict 数据字典对象
     * @param map  数据字典子集映射的map集合
     */
    private void appendToSubset(Dict dict, Map<String, List<String>> map) {
        List<String> list = getSubset(dict.getParent(), map);
        if (!list.contains(dict.getKey())) {
            list.add(dict.getKey());
        }
    }

    /**
     * 根据数据字典的key，从子集映射map集合中获取子集编号集合
     *
     * @param key 数据字典key
     * @param map 子集映射map集合
     * @return List<String> 行数据字典key对应的子集编号集合
     */
    private List<String> getSubset(String key, Map<String, List<String>> map) {
        List<String> list = map.get(key);
        if (isNull(list)) {
            list = Lists.newArrayList();
            map.put(key, list);
        }
        return list;
    }

    private Dict buildRoot() {
        Dict dict = new Dict();
        dict.setKey(ROOT_KEY);
        dict.setName(dict.getKey());
        dict.setDescription("根级数据字典对象，初始化缓存数据时，由系统自动构建");
        dict.setSort(1D);
        return dict;
    }

    /**
     * 设置字典的默认值，这里主要是设置默认的父级编号、排序字段
     *
     * @param dict 数据字典对象
     */
    private void setDefaultValue(Dict dict) {
        if (isEmpty(dict.getParent())) {
            dict.setParent(ROOT_KEY);
        }
        if (isNull(dict.getSort())) {
            dict.setSort(1D);
        }
        if (isNull(dict.getSubset())) {
            dict.setSubset(Lists.newArrayList());
        }
    }

    /**
     * 添加字典至父级的子集列表中
     *
     * @param dict 数据字典
     */
    private void appendToParent(Dict dict) {
        Dict parent = get(dict.getParent());
        if (isNotNull(parent)) {
            List<String> subset = parent.getSubset();
            //如果父级字典的子集中没有当前添加的字典key
            if (!subset.contains(dict.getKey())) {
                subset.add(dict.getKey());
                getHash().put(parent.getKey(), parent);
            }
        }
    }

    /**
     * 设置字典的子集集合
     *
     * @param dict 数据字典
     */
    private void setDictSubset(Dict dict) {
        //首先根据key从缓存中获取已存在的数据字典
        Dict data = get(dict.getKey());
        if (isNotNull(data)) {
            List<String> subset = CollectionUtil.contact(dict.getSubset(), data.getSubset());
            dict.setSubset(subset);
        }
    }
}
