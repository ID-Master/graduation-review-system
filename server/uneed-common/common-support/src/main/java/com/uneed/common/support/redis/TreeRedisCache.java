package com.uneed.common.support.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2020/1/17
 */
public interface TreeRedisCache<T, ID extends Serializable, V> {

    /**
     * 接收一个bean对象，将bean解析成Node对象，存储到redis中
     *
     * @param bean bean对象
     */
    void add(T bean);

    /**
     * 接收一个集合的bean对象，并解析成Node对象集合，存储到redis中
     *
     * @param beans bean对象集合
     */
    void add(Collection<T> beans);

    /**
     * 根据节点id获取节点对象
     *
     * @param id 节点id
     * @return Node<ID, V> 节点对象
     */
    Node<ID, V> get(ID id);

    /**
     * 根据节点id，获取父级节点对象
     *
     * @param id 节点id
     * @return Node<ID, V> 节点对象
     */
    Node<ID, V> parent(ID id);

    /**
     * 根据节点id，获取其所有父级节点，并按照直属关系排序
     *
     * @param id 节点id
     * @return List<Node < ID, V>> 所有父级节点集合
     */
    List<Node<ID, V>> parents(ID id);

    /**
     * 根据节点id，获取其所有父级节点id，并按照直属关系排序
     *
     * @param id 节点id
     * @return List<ID> 所有父级节点id集合
     */
    List<ID> parentIds(ID id);

    /**
     * 根据节点id，获取其直属子集
     *
     * @param id 节点id
     * @return List<Node < ID, V>> 直属子级节点集合
     */
    default List<Node<ID, V>> children(ID id) {
        return children(id, false);
    }

    /**
     * 根据节点id，获取其子集，根据isAll来判断是否获取全部子集
     *
     * @param id    节点id
     * @param isAll 若值为false，则只直属直属子集，否则全部子集
     * @return List<Node < ID, V>> 子级节点集合
     */
    List<Node<ID, V>> children(ID id, boolean isAll);

    /**
     * 根据节点id，获取其直属子集id集合
     *
     * @param id 节点id
     * @return List<ID> 直属子级节点id集合
     */
    default List<ID> childrenIds(ID id) {
        return childrenIds(id, false);
    }

    /**
     * 根据节点id，获取其子集id集合，根据isAll来判断是否获取全部子集id
     *
     * @param id    节点id
     * @param isAll 若值为false，则只直属直属子集id集合，否则全部子集id集合
     * @return List<ID> 子级节点id集合
     */
    List<ID> childrenIds(ID id, boolean isAll);

    /**
     * 根据节点id，构建一个树形结构数据，如果id为空或是0，则构建全部数据的树形结构数据
     *
     * @param id 节点id
     * @return Node<ID, V> 树结构数据
     */
    default Node<ID, V> tree(ID id) {
        return tree(id, false);
    }

    /**
     * 根据节点id，构建一个树形结构数据，如果id为空或是0，则构建全部数据的树形结构数据，isSort用来标记是否排序
     *
     * @param id     节点id
     * @param isSort 是否排序
     * @return Node<ID, V> 树结构数据
     */
    Node<ID, V> tree(ID id, boolean isSort);

    /**
     * 根据节点id，删除当前id对应的节点及其所有子节点
     *
     * @param id 节点id
     */
    void del(ID id);

    /**
     * 初始化bean数据，会先清空对应的数据集合，再将数据对象集合缓存到redis中
     *
     * @param beans 初始化的数据对象集合
     */
    void init(Collection<T> beans);

    /**
     * 清空redis中的所有Node对象
     */
    void clear();
}
