package com.xyw55.xorm.pool;


import com.xyw55.xorm.core.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池类
 * Created by xiayiwei on 7/25/17.
 */
public class DBConnPool {

    /**
     * 连接池对象
     */
    private static List<Connection> pool;

    /**
     * 最大连接数
     */
    private static int POOL_MAX_SIZE;

    /**
     * 最小连接数
     */
    private static int POOL_MIN_SIZE;

    static {
        POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();
        POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();
    }

    /**
     * 初始化连接池
     */
    private void initPool() {
        if (pool == null) {
            pool = new ArrayList<>();
        }

        while (pool.size() < POOL_MIN_SIZE) {
            pool.add(DBManager.createConn());
            System.out.println("init pool, size is " + pool.size());
        }
    }

    /**
     *
     */
    public DBConnPool() {
        initPool();
    }

    /**
     * 从连接池中获取一个连接
     * @return
     */
    public synchronized Connection getConnection() {
        int lastIndex = pool.size() - 1;
        Connection conn = pool.get(lastIndex);
        pool.remove(lastIndex);
        return conn;
    }


    /**
     * 将连接放回池中
     *
     * @param conn
     */
    public synchronized void close(Connection conn) {
        if (pool.size() > POOL_MAX_SIZE) {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            pool.add(conn);
        }
    }



}
