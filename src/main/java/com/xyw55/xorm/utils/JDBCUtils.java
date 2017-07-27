package com.xyw55.xorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了jdbc常用操作
 * Created by xiayiwei on 7/16/17.
 */
public class JDBCUtils {

    /**
     * 给sql传参
     * @param ps
     * @param params
     */
    public static void handleParams(PreparedStatement ps, Object[] params) throws SQLException {
        if (params != null) {
            for (int i=0; i <params.length; i++) {
                ps.setObject(1 + i, params[i]);
            }
        }
    }
}
