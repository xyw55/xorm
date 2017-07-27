package com.xyw55.xorm.bean;

/**
 * 封装java属性和get set方法的源代码
 * Created by xiayiwei on 7/16/17.
 */
public class JavaFieldGetSet {

    /**
     * 属性源码信息,如: private int userId;
     */
    private String fieldInfo;

    /**
     * get方法源码信息
     */
    private String getInfo;

    /**
     * set方法源码信息
     */
    private String setInfo;

    public JavaFieldGetSet() {
    }

    public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
        this.fieldInfo = fieldInfo;
        this.getInfo = getInfo;
        this.setInfo = setInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    @Override
    public String toString() {
        return fieldInfo + getInfo + setInfo;
    }
}
