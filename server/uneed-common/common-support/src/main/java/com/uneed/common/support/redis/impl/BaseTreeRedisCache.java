package com.uneed.common.support.redis.impl;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.text.JsonUtil;
import com.uneed.common.support.redis.Node;
import com.uneed.common.support.redis.TreeRedisCache;
import com.uneed.common.support.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.support.constant.KeyConstant.TREE_NODE;
import static com.uneed.common.support.constant.KeyConstant.TREE_SUBSET;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2020/1/16
 */
public abstract class BaseTreeRedisCache<T, ID extends Serializable, V> implements TreeRedisCache<T, ID, V> {

    //************************************************* 常量 *******************************************************************************/

    /**
     * 日志记录器
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 树根节点id
     */
    protected static final String SUB_ROOT = "0";

    //************************************************* 私有属性 *******************************************************************************/

    /**
     * redisTemplate对象，私有属性，用做缓存，使用必须通过getRedisTemplate()方法获取
     */
    private RedisTemplate<String, Node<ID, V>> redisTemplate;

    /**
     * id的获取方法，私有属性，用来缓存，使用必须通过getIdGetterMethod()方法获取
     */
    private Method idGetterMethod;

    /**
     * pid的获取方法，私有属性，用来缓存，使用必须通过getIdGetterMethod()方法获取
     */
    private Method pidGetterMethod;

    /**
     * name的获取方法，私有属性，用来缓存，使用必须通过getNameGetterMethod()方法获取
     */
    private Method nameGetterMethod;

    /**
     * sort的获取方法，私有属性，用来缓存，使用必须通过getSortGetterMethod()方法获取
     */
    private Method sortGetterMethod;

    /**
     * bean类型，私有属性，用来缓存，使用必须通过getBeanClass()方法获取
     */
    private Class<T> beanClass;

    /**
     * id的class类型，私有属性，用来缓存，使用必须通过getIdClass()方法获取
     */
    private Class<ID> idClass;

    //************************************************* 公用方法 *******************************************************************************/

    @Override
    public void add(T bean) {
        long time = System.currentTimeMillis();
        addNode(buildNode(bean));
        if (log.isDebugEnabled()) {
            log.debug(" [add] ----> 添加树结构缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[bean=" + JsonUtil.toJson(bean)
                    + "]");
        }
    }

    @Override
    public void add(Collection<T> beans) {
        long time = System.currentTimeMillis();
        addNode(buildNode(beans), true);
        if (log.isDebugEnabled()) {
            log.debug(
                    " [add] ----> 批量添加树结构缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[beans=" + JsonUtil.toJson(beans)
                            + "]");
        }
    }

    @Override
    public Node<ID, V> get(ID id) {
        long time = System.currentTimeMillis();
        String key = getKey(id);
        Node<ID, V> node = isNotEmpty(key) ? getHash().get(key) : null;
        if (log.isDebugEnabled()) {
            log.debug(" [get] ----> 获取树结构节点缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + "]");
        }
        return node;
    }

    @Override
    public Node<ID, V> parent(ID id) {
        long time = System.currentTimeMillis();
        Node<ID, V> node = get(id);
        Node<ID, V> parent = isNotNull(node) ? get(node.getPid()) : null;
        if (log.isDebugEnabled()) {
            log.debug(" [parent] ----> 获取树结构父级缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + "]");
        }
        return parent;
    }

    @Override
    public List<Node<ID, V>> parents(ID id) {
        long time = System.currentTimeMillis();
        List<Node<ID, V>> list = Lists.newArrayList();
        buildParent(parent(id), list);
        if (log.isDebugEnabled()) {
            log.debug(" [parents] ----> 批量获取树结构父级缓存！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + "]");
        }
        return list;
    }

    @Override
    public List<ID> parentIds(ID id) {
        long time = System.currentTimeMillis();
        List<ID> list = CollectionUtil.getPropertyList(parents(id), "id");
        if (log.isDebugEnabled()) {
            log.debug(" [parentIds] ----> 批量获取树结构父级id缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + "]");
        }
        return list;
    }

    @Override
    public List<Node<ID, V>> children(ID id, boolean isAll) {
        long time = System.currentTimeMillis();
        Set<String> keys = getKeys(childrenIds(id, isAll));
        List<Node<ID, V>> list = filterNullNode(getHash().multiGet(keys));
        if (log.isDebugEnabled()) {
            log.debug(" [children] ----> 批量获取树结构子集缓存！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + ", isAll="
                    + isAll + "]");
        }
        return list;
    }

    @Override
    public List<ID> childrenIds(ID id, boolean isAll) {
        long time = System.currentTimeMillis();
        List<ID> list = Lists.newArrayList();
        if (isAll) {
            //所有所有子集数据
            Map<String, List<ID>> map = getSubHash().entries();
            if (isNotNull(map)) {
                buildChildrenIds(id, list, map);
            }
        } else {
            list = nullToDefault(getSubHash().get(getSubKey(id)), list);
        }
        if (log.isDebugEnabled()) {
            log.debug(" [childrenIds] ----> 批量获取树结构子集id缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id
                    + ", isAll=" + isAll + "]");
        }
        return list;
    }

    @Override
    public Node<ID, V> tree(ID id, boolean isSort) {
        long time = System.currentTimeMillis();
        Node<ID, V> node = buildTree(id, isSort);
        if (log.isDebugEnabled()) {
            log.debug(" [tree] ----> 构建树结构的数据缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + "]");
        }
        return node;
    }

    @Override
    public void del(ID id) {
        long time = System.currentTimeMillis();
        //不支持全部删除、需要全部删除请调用clear()方法
        if (isNull(id) || equal(getKey(id), SUB_ROOT)) {
            log.error("当前删除节点id[" + id + "]，系统不支持全部删除缓存库中数据，请调用clear()进行清空！");
            return;
        }
        //删除其父级子集映射的id
        delWithParent(id);
        //获取节点下的所有子节点，并将当前节点添加到集合中
        List<ID> ids = childrenIds(id, true);
        ids.add(id);
        Object[] keys = getKeys(ids).toArray();
        //删除node和subset
        getHash().delete(keys);
        getSubHash().delete(keys);
        if (log.isDebugEnabled()) {
            log.debug(" [del] ----> 删除树结构缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[id=" + id + "]");
        }
    }

    @Override
    public void init(Collection<T> beans) {
        long time = System.currentTimeMillis();
        clear();
        addNode(buildNode(beans), false);
        if (log.isDebugEnabled()) {
            log.debug(
                    " [init] ----> 初始化树结构缓存完成！时长[" + (System.currentTimeMillis() - time) + "]，参数[beans=" + JsonUtil.toJson(beans)
                            + "]");
        }
    }

    @Override
    public void clear() {
        long time = System.currentTimeMillis();
        getRedisTemplate().delete(getHashKey(HashType.NODE));
        getRedisTemplate().delete(getHashKey(HashType.SUBSET));
        if (log.isDebugEnabled()) {
            log.debug(" [clear] ----> 清空树结构缓存完成！时长[" + (System.currentTimeMillis() - time) + "]");
        }
    }

    //************************************************* redis操作工具 *******************************************************************************/

    /**
     * 设置redisTemplate，需要子类注入
     *
     * @param redisTemplate redisTemplate对象
     */
    public void setRedisTemplate(RedisTemplate<String, Node<ID, V>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取redisTemplate对象，如果注入的为空，则从spring容器中获取
     *
     * @return RedisTemplate<String, T> redisTemplate对象
     */
    @SuppressWarnings("ALL")
    protected RedisTemplate<String, Node<ID, V>> getRedisTemplate() {
        if (ObjectUtil.isNull(redisTemplate)) {
            redisTemplate = (RedisTemplate<String, Node<ID, V>>) SpringUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 通过redisTemplate对象获取HashOperation对象
     *
     * @return BoundHashOperations<String, String, Node < ID, V>> HashOperation对象
     */
    protected BoundHashOperations<String, String, Node<ID, V>> getHash() {
        return getRedisTemplate().boundHashOps(getHashKey(HashType.NODE));
    }

    /**
     * 通过redisTemplate对象获取HashOperation对象
     *
     * @return BoundHashOperations<String, String, List < ID>> HashOperation对象
     */
    protected BoundHashOperations<String, String, List<ID>> getSubHash() {
        return getRedisTemplate().boundHashOps(getHashKey(HashType.SUBSET));
    }

    //************************************************* redis键值 *******************************************************************************/

    /**
     * 获取redis的key值，key值组成：HashType + prefix()
     *
     * @return type hash类型
     */
    protected String getHashKey(HashType type) {
        return type.getCode() + ":" + prefix();
    }

    /**
     * redis key 的前缀，默认为实体类名
     *
     * @return redis的前缀
     */
    protected String prefix() {
        return getBeanClass().getName();
    }

    /**
     * 获取节点node的key
     *
     * @param id 节点id
     * @return node节点的hashKey值
     */
    protected String getKey(ID id) {
        return StringUtil.toString(id);
    }

    /**
     * 获取子集subset的key
     *
     * @param id 节点id
     * @return String 节点子集的hashKey值
     */
    protected String getSubKey(ID id) {
        return isNotNull(id) ? getKey(id) : SUB_ROOT;
    }

    /**
     * 获取节点node的key值集合
     *
     * @param ids 节点id集合
     * @return Set<String> node节点的hashKey值集合
     */
    protected Set<String> getKeys(Collection<ID> ids) {
        Set<String> set = Lists.newHashSet();
        for (ID id : ids) {
            String key = getKey(id);
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            set.add(key);
        }
        return set;
    }

    /**
     * 构建树结构时，根节点名称，可以通过子类，更换实现名称
     *
     * @return String 根节点名称
     */
    protected String treeRootName() {
        return "ROOT";
    }

    //************************************************* bean对象的class *******************************************************************************/

    /**
     * 获取泛型的java类型
     *
     * @return Class<T> 泛型类型
     */
    @SuppressWarnings("ALL")
    protected Class<T> getBeanClass() {
        if (ObjectUtil.isNull(beanClass)) {
            beanClass = (Class<T>) BeanUtil.getSuperClassActualType(getClass(), 0);
        }
        return beanClass;
    }

    /**
     * 获取泛型的java类型
     *
     * @return Class<T> 泛型类型
     */
    @SuppressWarnings("ALL")
    protected Class<ID> getIdClass() {
        if (ObjectUtil.isNull(idClass)) {
            idClass = (Class<ID>) BeanUtil.getSuperClassActualType(getClass(), 1);
        }
        return idClass;
    }

    //************************************************* 获取 id *******************************************************************************/

    /**
     * 主键字段名称
     *
     * @return 主键字段
     */
    protected String idField() {
        return "id";
    }

    /**
     * 获取id的反射方法
     *
     * @return Method 反射方法
     */
    protected Method getIdGetterMethod() {
        if (isNull(idGetterMethod)) {
            idGetterMethod = BeanUtil.getReadMethod(getBeanClass(), idField());
        }
        return idGetterMethod;
    }

    /**
     * 获取主键值
     *
     * @param bean bean对象
     * @return ID 主键值
     */
    @SuppressWarnings("ALL")
    protected ID getId(T bean) {
        try {
            return isNotNull(bean) ? (ID) getIdGetterMethod().invoke(bean) : null;
        } catch (Exception e) {
            log.error("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树id失败：" + e.getMessage(), e);
            throw new BusinessException("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树id失败：" + e.getMessage());
        }
    }

    //************************************************* 获取 pid *******************************************************************************/

    /**
     * pid字段名称
     *
     * @return 主键字段
     */
    protected String pidField() {
        return "parentId";
    }

    /**
     * 获取主键值
     *
     * @param bean bean对象
     * @return ID pid
     */
    @SuppressWarnings("ALL")
    protected ID getPid(T bean) {
        try {
            ID pid = (ID) getPidGetterMethod().invoke(bean);
            return equal(StringUtil.toString(pid), SUB_ROOT) ? null : pid;
        } catch (Exception e) {
            log.error("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树pid失败：" + e.getMessage(), e);
            throw new BusinessException("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树pid失败：" + e.getMessage());
        }
    }

    /**
     * 获取pid的反射方法
     *
     * @return Method 反射方法
     */
    protected Method getPidGetterMethod() {
        if (isNull(pidGetterMethod)) {
            pidGetterMethod = BeanUtil.getReadMethod(getBeanClass(), pidField());
        }
        return pidGetterMethod;
    }

    //************************************************* 获取 name *******************************************************************************/

    /**
     * 树结构名称字段名称，需要子类实现
     *
     * @return 主键字段
     */
    protected abstract String nameField();

    /**
     * 获取name的值
     *
     * @param bean bean对象
     * @return Long 主键值
     */
    protected String getName(T bean) {
        try {
            return StringUtil.toString(getNameGetterMethod().invoke(bean));
        } catch (Exception e) {
            log.error("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树name失败：" + e.getMessage(), e);
            throw new BusinessException("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树name失败：" + e.getMessage());
        }
    }

    /**
     * 获取name的反射方法
     *
     * @return Method 反射方法
     */
    protected Method getNameGetterMethod() {
        if (isNull(nameGetterMethod)) {
            nameGetterMethod = BeanUtil.getReadMethod(getBeanClass(), nameField());
        }
        return nameGetterMethod;
    }

    //************************************************* 获取 sort *******************************************************************************/

    /**
     * 树结构排序字段，需要子类实现
     *
     * @return 排序字段
     */
    protected abstract String sortField();

    /**
     * 获取name的值
     *
     * @param bean bean对象
     * @return Long 主键值
     */
    protected Double getSort(T bean) {
        try {
            return convert(getSortGetterMethod().invoke(bean), 0D);
        } catch (Exception e) {
            log.error("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树sort失败：" + e.getMessage(), e);
            throw new BusinessException("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取树sort失败：" + e.getMessage());
        }
    }

    /**
     * 获取name的反射方法
     *
     * @return Method 反射方法
     */
    protected Method getSortGetterMethod() {
        if (isNull(sortGetterMethod)) {
            sortGetterMethod = BeanUtil.getReadMethod(getBeanClass(), sortField());
        }
        return sortGetterMethod;
    }

    //************************************************* 获取树结构值 *******************************************************************************/

    /**
     * 获取树节点data的值，需要子类实现
     *
     * @param bean bean对象
     * @return V 数据节点data值
     */
    protected abstract V getData(T bean);

    //************************************************* node操作 *******************************************************************************/

    /**
     * 添加节点数据，并加该节点id追加到对应父级的缓存子集中
     *
     * @param node 节点数据
     */
    protected void addNode(Node<ID, V> node) {
        if (isNull(node)) {
            return;
        }
        getHash().put(getKey(node.getId()), node);
        addSubset(node);
    }

    /**
     * 批量添加节点数据，判断是否需要从缓存库中加载已存在数据再取并集，初始化时不需要该操作
     *
     * @param nodes 节点集合
     * @param load  是否从缓存库中加载已存在的数据
     */
    protected void addNode(List<Node<ID, V>> nodes, boolean load) {
        if (CollectionUtil.isEmpty(nodes)) {
            return;
        }
        Map<String, Node<ID, V>> map = Maps.newHashMap();
        for (Node<ID, V> node : nodes) {
            map.put(getKey(node.getId()), node);
        }
        getHash().putAll(map);
        addSubset(nodes, load);
    }

    /**
     * 将java类对象构建成树节点对象
     *
     * @param bean java类对象
     * @return Node<ID, V> 树节点对象
     */
    protected Node<ID, V> buildNode(T bean) {
        ID id = getId(bean);
        return isNotNull(id) ? new Node<>(id, getPid(bean), getName(bean), getData(bean), getSort(bean)) : null;
    }

    /**
     * 将java类对象集合构建成树节点对象集合
     *
     * @param beans java类对象集合
     * @return Node<ID, V> 树节点对象
     */
    protected List<Node<ID, V>> buildNode(Collection<T> beans) {
        List<Node<ID, V>> list = Lists.newArrayList();

        if (isNull(beans)) {
            return list;
        }

        for (T bean : beans) {
            Node<ID, V> node = buildNode(bean);
            if (isNotNull(node)) {
                list.add(node);
            }
        }
        return list;
    }

    /**
     * 递归构建子集节点id集合
     *
     * @param id   节点id
     * @param list 用来存储构建完成的预设集合
     * @param map  缓存库中获取的所有子集节点id集合
     */
    protected void buildChildrenIds(ID id, List<ID> list, Map<String, List<ID>> map) {
        List<ID> subset = map.get(getSubKey(id));

        if (isNull(subset)) {
            return;
        }

        for (ID sub : subset) {
            //过滤null
            if (isNotNull(sub)) {
                list.add(sub);
                buildChildrenIds(sub, list, map);
            }
        }
    }

    /**
     * 递归构建父级节点对象
     *
     * @param parent 父节点
     * @param list   用来存储构建完成的预设集合
     */
    protected void buildParent(Node<ID, V> parent, List<Node<ID, V>> list) {
        if (isNull(parent)) {
            return;
        }
        list.add(parent);
        buildParent(parent(parent.getId()), list);
    }

    /**
     * 构建树对象，从缓存中循环获取子级节点，封装成树结构数据
     *
     * @param id     节点id
     * @param isSort 标记是否排序
     * @return Node<ID, V> 树结构对象
     */
    protected Node<ID, V> buildTree(ID id, boolean isSort) {
        //构建root
        Node<ID, V> root =
                (isNull(id) || equal(getKey(id), SUB_ROOT)) ? new Node<>(convert(SUB_ROOT, getIdClass()), treeRootName())
                        : get(id);
        if (isNotNull(root)) {
            Map<ID, List<Node<ID, V>>> map = buildSubsetMap(children(id, true));
            buildTree(root, map, isSort);
        }
        return root;
    }

    /**
     * 递归构建树对象
     *
     * @param root   跟节点
     * @param map    节点集合
     * @param isSort 标记是否排序
     */
    protected void buildTree(Node<ID, V> root, Map<ID, List<Node<ID, V>>> map, boolean isSort) {
        List<Node<ID, V>> nodes = map.get(root.getId());
        if (CollectionUtil.isEmpty(nodes)) {
            return;
        }
        //如果需要排序
        if (isSort && nodes.size() > 1) {
            Collections.sort(nodes);
        }
        root.setChildren(nodes);
        for (Node<ID, V> node : nodes) {
            buildTree(node, map, isSort);
        }
    }

    /**
     * 将节点集合，构建成一个map对象，key为节点id，value为该节点的所有子集
     *
     * @param list 节点集合
     * @return Map<ID, List < Node < ID, V>>> 构建完成的map对象
     */
    protected Map<ID, List<Node<ID, V>>> buildSubsetMap(List<Node<ID, V>> list) {
        Map<ID, List<Node<ID, V>>> map = Maps.newHashMap();
        for (Node<ID, V> node : list) {
            ID pid = isNotNull(node.getPid()) ? node.getPid() : convert(getSubKey(node.getPid()), getIdClass());
            List<Node<ID, V>> subList = map.get(pid);
            if (isNull(subList)) {
                subList = Lists.newArrayList();
                map.put(pid, subList);
            }
            subList.add(node);
        }
        return map;
    }

    /**
     * 过滤空节点
     *
     * @param nodes 节点集合
     * @return 过滤之后的集合
     */
    protected List<Node<ID, V>> filterNullNode(List<Node<ID, V>> nodes) {
        List<Node<ID, V>> list = Lists.newArrayList();
        for (Node<ID, V> node : nullToDefault(nodes, list)) {
            if (isNull(node)) {
                continue;
            }
            list.add(node);
        }
        return list;
    }

    //************************************************* subset 操作 *******************************************************************************/

    /**
     * 根据节点，添加其直属子集
     *
     * @param node 节点
     */
    protected void addSubset(Node<ID, V> node) {
        getSubHash().put(getSubKey(node.getPid()), buildSubset(node, null));
    }

    /**
     * 批量添加节点，判断是否需要从缓存库中加载已存在数据再取并集，初始化时不需要该操作
     *
     * @param nodes 节点集合
     * @param load  是否从缓存库中加载已存在数据
     */
    protected void addSubset(List<Node<ID, V>> nodes, boolean load) {
        Map<String, List<ID>> map = Maps.newHashMap();
        for (Node<ID, V> node : nodes) {
            map.put(getSubKey(node.getPid()), buildSubset(node, map));
        }
        if (map.isEmpty()) {
            return;
        }
        //判断是否需要加载缓存库中已存在的数据
        if (load) {
            for (String key : map.keySet()) {
                if (!ObjectUtil.equal(getSubHash().hasKey(key), Boolean.TRUE)) {
                    continue;
                }
                //则取库中数据，取并集后再设置
                List<ID> list = CollectionUtil.contact(getSubHash().get(key), map.get(key));
                map.put(key, list);
            }
        }
        //添加到缓存库中
        getSubHash().putAll(map);
    }

    /**
     * 构建节点的子级id集合，并过滤重复
     *
     * @param node 节点
     * @return List<ID> 子级id集合
     */
    protected List<ID> buildSubset(Node<ID, V> node, Map<String, List<ID>> map) {
        String key = getSubKey(node.getPid());
        List<ID> subset = isNull(map) ? getSubHash().get(key) : map.get(key);
        if (isNull(subset)) {
            return Lists.newArrayList(node.getId());
        }
        if (!containsId(node.getId(), subset)) {
            subset.add(node.getId());
        }
        return subset;
    }

    /**
     * 过滤重复的element
     *
     * @param id     集合中的元素
     * @param subset 集合
     * @return 是否包含
     */
    protected boolean containsId(ID id, List<ID> subset) {
        return subset.contains(id);
    }

    /**
     * 将节点id从其父级子集中删除
     *
     * @param id 节点id
     */
    protected void delWithParent(ID id) {
        Node<ID, V> parent = parent(id);
        String key = isNull(parent) ? SUB_ROOT : getKey(parent.getId());
        List<ID> subset = getSubHash().get(key);
        if (subset == null || subset.isEmpty()) {
            return;
        }
        subset.removeIf(data -> equal(id, data));

        //如果子集已经全部被清空，则删除当前节点
        if (subset.isEmpty()) {
            getSubHash().delete(key);
            return;
        }
        getSubHash().put(key, subset);
    }

    //************************************************* hash类型 *******************************************************************************/

    /**
     * hash类型，用来判断是节点还是子集
     */
    protected enum HashType {
        /**
         * 节点
         */
        NODE(TREE_NODE),
        /**
         * 子集
         */
        SUBSET(TREE_SUBSET);

        private String code;

        HashType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
