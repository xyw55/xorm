package com.xyw55.xorm.utils;

/**
 * 封装的字符串常用操作
 * Created by xiayiwei on 7/16/17.
 */
public class StringUtils {

    /**
     * 目标字符串首字母大写
     * @param str
     * @return
     */
    public static String firstChar2Upper(String str) {
        return str.toUpperCase().substring(0, 1) + str.substring(1);
    }
}
