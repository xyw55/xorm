package com.xyw55.xorm.core;

import com.xyw55.xorm.bean.ColumnInfo;
import com.xyw55.xorm.bean.TableInfo;
import com.xyw55.xorm.utils.JavaFileUtils;
import com.xyw55.xorm.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责获取管理数据库所有表结构和类结构的关系,并可以根据表结构生成类结构
 * Created by xiayiwei on 7/16/17.
 */
public class TableContext {

    /**
     * 表名为key, 表对象为value
     */
    private static Map<String, TableInfo> tables = new HashMap<>();

    /**
     * 将po的class对象和表信息对象关联起来,可以重用
     */
    private static Map<Class, TableInfo> poClassTableMap = new HashMap<>();

    private TableContext() {
    }

    static {
        try {
            // 初始化,得到表信息
            Connection conn = DBManager.getConn();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet tableSet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});

            while (tableSet.next()) {
                String tableName = (String) tableSet.getObject("TABLE_NAME");
                TableInfo ti = new TableInfo(tableName, new HashMap<String, ColumnInfo>(),
                        new ArrayList<ColumnInfo>());
                tables.put(tableName, ti);

                ResultSet set = dbmd.getColumns(null, "%", tableName, "%"); // 查询表中所有字段
                while (set.next()) {
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"), 0);
                    ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
                }

                ResultSet pk = dbmd.getPrimaryKeys(null, "%", tableName); // 查询表中所有主键
                while (pk.next()) {
                    ColumnInfo ci = ti.getColumns().get(pk.getObject("COLUMN_NAME"));
                    ci.setKeyType(1);
                    ti.getPriKeys().add(ci);
                }

                if (ti.getPriKeys().size() == 1) {   // 取唯一主键,方便使用, 如果联合主键,则为空
                    ti.setOnlyPriKey(ti.getPriKeys().get(0));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 更新java po file
        updateJavaPOFile();
        // 将po的class对象和表信息对象关联起来,可以重用, 提高效率
        loadPoTables();
    }

    public static Map<String, TableInfo> getTables() {
        return tables;
    }

    public static Map<Class, TableInfo> getPoClassTableMap() {
        return poClassTableMap;
    }

    /**
     * 更新java po file
     */
    public static void updateJavaPOFile() {
        Map<String, TableInfo> tables = TableContext.getTables();
        for (TableInfo tableInfo:
                tables.values()) {
            JavaFileUtils.createJavaPOFile(tableInfo, new MysqlTypeConvertor());
        }
    }

    /**
     * 加载po下面的类
     */
    public static void loadPoTables() {
        for (TableInfo tableInfo : tables.values()) {
            try {
                Class c = Class.forName(DBManager.getConf().getPoPackage() + "." + StringUtils.firstChar2Upper(tableInfo.getTname()));
                poClassTableMap.put(c, tableInfo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Map<String, TableInfo> tables = getTables();
        System.out.println(tables);
    }
}

