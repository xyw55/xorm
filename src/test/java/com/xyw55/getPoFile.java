package com.xyw55;

import com.xyw55.xorm.bean.TableInfo;
import com.xyw55.xorm.core.MysqlTypeConvertor;
import com.xyw55.xorm.core.TableContext;
import com.xyw55.xorm.utils.JavaFileUtils;

import java.util.Map;

/**
 * Created by xiayiwei on 7/27/17.
 */
public class getPoFile {

    public static void main(String[] args) {

        Map<String, TableInfo> tables = TableContext.getTables();
        for (TableInfo tableInfo:
                tables.values()) {
            JavaFileUtils.createJavaPOFile(tableInfo, new MysqlTypeConvertor());
        }


    }
}
