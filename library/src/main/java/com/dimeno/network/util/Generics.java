package com.dimeno.network.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Generics
 * Created by wangzhen on 2020/4/15.
 */
public class Generics {
    private Generics() {
    }

    /**
     * 获取类声明的泛型
     *
     * @param realize 目标类
     * @param define  定义泛型的 class
     * @return 返回泛型对应type {@link Type}, 获取失败返回null.
     */
    public static Type getGenericType(final Class realize, final Class define) {
        Type type = null;
        if (realize != null && realize != Object.class) {
            if (define == null || define.isAssignableFrom(realize)) {
                Class[] interfaces = realize.getInterfaces();
                // 1、获取接口声明泛型
                for (Class _interface : interfaces) {
                    if (define == null || define.isAssignableFrom(_interface)) {
                        type = getGenericType(_interface, define);
                        break;
                    }
                }
                // 2、获取父类声明泛型
                if (type == null) {
                    Class superClass = realize.getSuperclass();
                    if (superClass != null) {
                        if (define == null || define.isAssignableFrom(superClass))
                            type = getGenericType(superClass, define);
                    }
                }
                // 3、获取本身声明的泛型
                if (type == null) {
                    type = Generics.getCurrentGenericType(realize, define);
                }
            }
        }
        return type;
    }

    /**
     * 获取类当前泛型
     *
     * @param realize 参数化类型的类 class
     * @param define  定义泛型的 class
     * @return 实际类型参数（泛型）
     */
    public static Type getCurrentGenericType(final Class realize, final Class define) {
        if (realize != Object.class) {
            Type type = null;
            // 接口上的实际类型参数（泛型）
            Type[] generics = realize.getGenericInterfaces();
            for (Type generic : generics) {
                if (generic instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) generic).getRawType();
                    if (define == null ||
                            (rawType instanceof Class && define.isAssignableFrom((Class<?>) rawType))) {
                        type = generic;
                        break;
                    }
                }
            }

            if (type == null) {
                if (define == null || define.isAssignableFrom(realize))
                    // 类/抽象类上的实际类型参数（泛型）
                    type = realize.getGenericSuperclass();
            }

            if (type instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    return args[0];
                } else if (args[0] instanceof ParameterizedType) { // eg: List<String>
                    return args[0];
                }
            }
        }
        return null;
    }
}
