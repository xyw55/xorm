package com.xyw55.xorm.utils;

import com.xyw55.xorm.bean.ColumnInfo;
import com.xyw55.xorm.bean.JavaFieldGetSet;
import com.xyw55.xorm.bean.TableInfo;
import com.xyw55.xorm.core.DBManager;
import com.xyw55.xorm.core.MysqlTypeConvertor;
import com.xyw55.xorm.core.TableContext;
import com.xyw55.xorm.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了生成java 文件(源代码)常用操作
 * Created by xiayiwei on 7/16/17.
 */
public class JavaFileUtils {

    /**
     * 根据字段信息生成java属性信息,如var username -> private String username; 以及相应的set和get方法源码
     * @param columnInfo    字段信息
     * @param typeConvertor 类型转换器
     * @return
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo, TypeConvertor typeConvertor) {
        JavaFieldGetSet javaFieldGetSet = new JavaFieldGetSet();
        String javaFieldType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());

        javaFieldGetSet.setFieldInfo("\tprivate " + javaFieldType + " " + columnInfo.getName() + ";\n");

        StringBuilder getSrc = new StringBuilder();
        getSrc.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2Upper(columnInfo.getName()) + "(){ \n");
        getSrc.append("\t\treturn " + columnInfo.getName() + ";\n");
        getSrc.append("\t}\n");
        javaFieldGetSet.setGetInfo(getSrc.toString());

        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic void set" + StringUtils.firstChar2Upper(columnInfo.getName()) + "(" +
                javaFieldType + " " + columnInfo.getName() + "){\n");
        setSrc.append("\t\tthis." + columnInfo.getName() + " = " + columnInfo.getName() + ";\n");
        setSrc.append("\t}\n");
        javaFieldGetSet.setSetInfo(setSrc.toString());

        return javaFieldGetSet;

    }

    /**
     * 根据表信息生成java类的源代码
     *
     * @param tableInfo
     * @return
     */
    public static String createJavaSrc(TableInfo tableInfo, TypeConvertor typeConvertor) {
        StringBuilder javaSrc = new StringBuilder();

        Map<String, ColumnInfo> columns = tableInfo.getColumns();

        List<JavaFieldGetSet> javaFields = new ArrayList<>();

        for (ColumnInfo columnInfo:
             columns.values()) {
            javaFields.add(createFieldGetSetSRC(columnInfo, typeConvertor));
        }

        // 1. package 语句
        javaSrc.append("package " + DBManager.getConf().getPoPackage() + ";\n\n");
        // 2. import 语句
        javaSrc.append("import java.sql.*;\n");
        javaSrc.append("import java.util.*;\n\n");
        // 3. 生成类声明语句
        javaSrc.append("public class " + StringUtils.firstChar2Upper(tableInfo.getTname()) + " {\n\n");
        // 4. 生成类属性语句
        for (JavaFieldGetSet javaField :
                javaFields) {
            javaSrc.append(javaField.getFieldInfo());
        }
        javaSrc.append("\n\n");
        // 5. 生成get方法
        for (JavaFieldGetSet javaField :
                javaFields) {
            javaSrc.append(javaField.getGetInfo());
        }
        javaSrc.append("\n\n");

        // 6. 生成set方法
        for (JavaFieldGetSet javaField :
                javaFields) {
            javaSrc.append(javaField.getSetInfo());
        }
        javaSrc.append("\n\n");

        // 7. 结束符
        javaSrc.append("}");

        return javaSrc.toString();

    }

    /**
     * 生成java po file
     * @param tableInfo
     * @param typeConvertor
     */
    public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor typeConvertor) {
        String javaSrc = createJavaSrc(tableInfo, typeConvertor);

        String srcPath = DBManager.getConf().getSrcPath() + "/";
        String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");

        File f = new File(srcPath + packagePath);
        // 不存在,新建
        if (!f.exists()) {
            f.mkdirs();
        }

        f = new File(srcPath + packagePath + "/" + StringUtils.firstChar2Upper(tableInfo.getTname()) + ".java");


        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(f));
            bw.write(javaSrc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
//        ColumnInfo ci = new ColumnInfo("username", "varchar", 0);
//        JavaFieldGetSet javaFieldGetSet = createFieldGetSetSRC(ci, new MysqlTypeConvertor());
//        System.out.println(javaFieldGetSet);

        Map<String, TableInfo> tables = TableContext.getTables();
        for (TableInfo tableInfo:
             tables.values()) {
            createJavaPOFile(tableInfo, new MysqlTypeConvertor());
        }


    }
}
