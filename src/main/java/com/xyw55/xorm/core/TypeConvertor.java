package com.xyw55.xorm.core;

/**
 * 负责java数据类型和数据库数据类型的转换
 * Created by xiayiwei on 7/16/17.
 */
public interface TypeConvertor {

    /**
     * 负责数据类型转换为java类型
     * @param columnType
     * @return java数据类型
     */
    public String databaseType2JavaType(String columnType);

    /**
     * 负责java类型转换为数据库类型
     * @param columnType
     * @return 数据库数据类型
     */
    public String javaType2DatabaseType(String columnType);

}
