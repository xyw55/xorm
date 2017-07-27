package com.xyw55.xorm.bean;

/**
 * 封装表中一个字段信息
 * Created by xiayiwei on 7/16/17.
 */
public class ColumnInfo {
    private String name;    //字段名称
    private String dataType;    // 字段数据类型
    private int keyType;    // 健的类型: 0:普通建, 1:主键, 2:外健

    public ColumnInfo() {
    }

    public ColumnInfo(String name, String dataType, int key) {
        this.name = name;
        this.dataType = dataType;
        this.keyType = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }
}
