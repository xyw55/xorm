package com.xyw55.xorm.core;

/**
 * mysql数据类型转换器
 * Created by xiayiwei on 7/16/17.
 */
public class MysqlTypeConvertor implements TypeConvertor {
    @Override
    public String databaseType2JavaType(String columnType) {

        if ("varchar".equalsIgnoreCase(columnType) || "char".equalsIgnoreCase(columnType)) {
            return "String";
        } else if ("bigint".equalsIgnoreCase(columnType) || "BIGINT UNSIGNED".equalsIgnoreCase(columnType)) {
            return "java.math.BigInteger";
        } else if ("int".equalsIgnoreCase(columnType)
                || "tinyint".equalsIgnoreCase(columnType)
                || "integer".equalsIgnoreCase(columnType)
                || "INT UNSIGNED".equalsIgnoreCase(columnType)) {
            return "Long";
        } else if ("double".equalsIgnoreCase(columnType) || "float".equalsIgnoreCase(columnType)) {
            return "Double";
        } else if ("clob".equalsIgnoreCase(columnType)) {
            return "java.sql.Clob";
        } else if ("blob".equalsIgnoreCase(columnType)) {
            return "java.sql.Blob";
        } else if ("date".equalsIgnoreCase(columnType) || "datetime".equalsIgnoreCase(columnType)) {
            return "java.sql.Date";
        } else if ("time".equalsIgnoreCase(columnType)) {
            return "java.sql.Time";
        } else if ("timestamp".equalsIgnoreCase(columnType)) {
            return "java.sql.Timestamp";
        }

        return null;
    }

    @Override
    public String javaType2DatabaseType(String columnType) {
        return null;
    }
}
