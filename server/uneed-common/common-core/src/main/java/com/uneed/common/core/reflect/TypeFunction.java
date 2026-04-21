package com.uneed.common.core.reflect;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
public interface TypeFunction<T, R> extends Serializable, Function<T, R> {
    /**
     * 以get开头的方法
     */
    String METHOD_PREFIX_GET = "get";
    /**
     * 以is开头的方法
     */
    String METHOD_PREFIX_IS = "is";
    /**
     * 获取列名称
     * @param lambda
     * @return String
     */
    static SerializedLambda getSerializedLambda(Serializable lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            return serializedLambda;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取列名称
     * @param lambda
     * @return String
     */
    static Field getLambdaField(Serializable lambda) {
        SerializedLambda serializedLambda = getSerializedLambda(lambda);
        // 从lambda信息取出method、field、class等
        String getMethodName = serializedLambda.getImplMethodName();
        String fieldName = Introspector.decapitalize(getMethodName.replace(METHOD_PREFIX_GET, ""));
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }

}
