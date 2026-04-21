package com.uneed.common.core.collection;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Enumeration}对象转{@link Iterator}对象
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @param <E> 元素类型
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class EnumerationIter<E> implements Iterator<E>, Iterable<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private final Enumeration<E> e;

    /**
     * 构造
     *
     * @param enumeration {@link Enumeration}对象
     */
    public EnumerationIter(Enumeration<E> enumeration) {
        this.e = enumeration;
    }

    @Override
    public boolean hasNext() {
        return e.hasMoreElements();
    }

    @Override
    public E next() {
        return e.nextElement();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

}
