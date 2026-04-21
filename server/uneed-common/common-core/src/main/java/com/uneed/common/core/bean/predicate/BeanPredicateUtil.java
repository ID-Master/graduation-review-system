package com.uneed.common.core.bean.predicate;

import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.core.lang.ObjectUtil;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import java.util.Collection;

/**
 * @author diablo
 * @date 2017/12/26
 */
public final class BeanPredicateUtil {

    private BeanPredicateUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static <T, V> Predicate<T> equalPredicate(String property, V value) {
        Validate.notEmpty(property, "property can't be blank!");
        return new BeanPredicate<>(property, PredicateUtils.equalPredicate(value));
    }

    @SafeVarargs
    public static <T, V> Predicate<T> containsPredicate(final String property, final V... values) {
        Validate.notEmpty(property, "property can't be blank!");
        return new BeanPredicate<>(property, new Predicate<V>() {
            @Override
            public boolean evaluate(V value) {
                return ArrayUtil.contains(values, value);
            }
        });
    }

    public static <T, V> Predicate<T> containsPredicate(final String property, final Collection<V> collection) {
        Validate.notEmpty(property, "property can't be blank!");
        return new BeanPredicate<>(property, new Predicate<V>() {
            @Override
            public boolean evaluate(V value) {
                return !ObjectUtil.isEmpty(collection) && collection.contains(value);
            }
        });
    }
}
