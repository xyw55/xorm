package com.xyw55.xorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了常用反射操作
 * Created by xiayiwei on 7/16/17.
 */
public class ReflectUtils {

    /**
     * 调用obj对象的对应属性fieldName的值
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object invokeGet(String fieldName, Object obj) {
        // 通过反射调用get set方法
        try {
            Class clazz = obj.getClass();
            Method method = clazz.getDeclaredMethod("get" + StringUtils.firstChar2Upper(fieldName), null);
            return method.invoke(obj, null);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置obj对象的对应属性fieldName的值
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void invokeSet(Object obj, String fieldName, Object fieldValue) {
        // 通过反射调用 set方法
        try {
            Class clazz = obj.getClass();
            Method method = clazz.getDeclaredMethod("set" + StringUtils.firstChar2Upper(fieldName), fieldValue.getClass());
            method.invoke(obj, fieldValue);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
