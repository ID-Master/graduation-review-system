package com.uneed.common.support.redis;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2020/1/17
 */
@Data
public class Node<ID extends Serializable, V> implements Serializable, Comparable<Node> {

    private static final long serialVersionUID = -2507929770227249280L;

    /**
     * 节点id
     */
    private ID id;

    /**
     * 父id
     */
    private ID pid;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点值
     */
    private V value;

    /**
     * 排序字段
     */
    private Double sort;

    /**
     * 子级
     */
    private List<Node<ID, V>> children;

    public Node() {

    }

    public Node(ID id, String name) {
        this.id = id;
        this.name = name;
    }

    public Node(ID id, ID pid, String name, V value, Double sort) {
        this(id, name);
        this.pid = pid;
        this.value = value;
        this.sort = sort;
    }

    @Override
    public int compareTo(Node o) {
        return this.getSort().compareTo(o.getSort());
    }

    public Double getSort() {
        return sort != null ? sort : 0D;
    }
}
