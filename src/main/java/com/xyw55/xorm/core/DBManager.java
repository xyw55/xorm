package com.xyw55.xorm.core;

import com.xyw55.xorm.bean.Configuration;
import com.xyw55.xorm.bean.TableInfo;
import com.xyw55.xorm.pool.DBConnPool;
import com.xyw55.xorm.utils.JavaFileUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

/**
 * 根据配置信息,维持连接对象的管理(增加连接池功能)
 * Created by xiayiwei on 7/16/17.
 */
public class DBManager {
    private static Configuration conf;
    private static DBConnPool dbConnPool;

    static {
        Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf = new Configuration();
        conf.setDriver(props.getProperty("driver"));
        conf.setUrl(props.getProperty("url"));
        conf.setUser(props.getProperty("user"));
        conf.setPwd(props.getProperty("pwd"));
        conf.setUsingDB(props.getProperty("usingDB"));
        conf.setSrcPath(props.getProperty("srcPath"));
        conf.setPoPackage(props.getProperty("poPackage"));
        conf.setQueryClass(props.getProperty("queryClass"));
        conf.setPoolMinSize(Integer.parseInt(props.getProperty("poolMinSize")));
        conf.setPoolMaxSize(Integer.parseInt(props.getProperty("poolMaxSize")));
        // 连接池
        dbConnPool = new DBConnPool();
    }

    public static Configuration getConf() {
        return conf;
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConn() {
        return dbConnPool.getConnection();
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection createConn() {
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(),
                    conf.getUser(), conf.getPwd()); //目前直接建立连接,后期连接池处理
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭连接
     * @param rs
     * @param ps
     * @param conn
     */
    public static void close(ResultSet rs, Statement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                dbConnPool.close(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 关闭连接
     * @param ps
     * @param conn
     */
    public static void close(Statement ps, Connection conn) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                dbConnPool.close(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * 关闭连接
     * @param conn
     */
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                dbConnPool.close(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
