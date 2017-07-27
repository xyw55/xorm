package com.xyw55.xorm.core;

import com.xyw55.xorm.bean.ColumnInfo;
import com.xyw55.xorm.bean.TableInfo;
import com.xyw55.xorm.utils.JDBCUtils;
import com.xyw55.xorm.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询,对外提供服务的核心类
 * Created by xiayiwei on 7/16/17.
 */
public abstract class Query implements Cloneable{

    /**
     * 采用模版方法模式将jdbc操作封装成模版,便于重用
     * @param sql
     * @param clazz
     * @param params
     * @param callback
     * @return
     */
    public Object executeQueryTemplate(String sql, Class clazz, Object[] params, Callback callback) {
        Connection conn = DBManager.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps, params);
            rs = ps.executeQuery();

            return callback.doExecute(conn, ps, rs);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBManager.close(rs, ps, conn);
        }

    }

    /**
     * 查询执行一个DML语句
     *
     * @param sql   执行的sql,对象参数
     * @param params
     * @return
     */
    public int executeDML(String sql, Object[] params){
        Connection conn = DBManager.getConn();
        PreparedStatement ps = null;

        int count = 0;
        try {
            ps = conn.prepareStatement(sql);
            JDBCUtils.handleParams(ps, params);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBManager.close(ps, conn);
        }
        return count;
    }

    /**
     * 插入一个对象到数据库中
     * @param obj   需要插入的对象
     */
    public void insert(Object obj){
        Class clazz = obj.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);

        StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + "(");
        int countNotNull = 0;
        List<Object> params = new ArrayList<>();
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            String fieldName = f.getName();
            Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
            if (fieldValue != null) {
                sql.append(fieldName + ",");
                countNotNull++;
                params.add(fieldValue);
            }
        }

        sql.setCharAt(sql.length()-1, ')');
        sql.append(" values (");
        for (int i = 0; i < countNotNull; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length() - 1, ')');
        executeDML(sql.toString(), params.toArray());
    };

    /**
     * 删除clazz类对应的表中记录(按主键id删除)
     * @param clazz 要删除的类
     * @param id    类的主键id值
     */
    public void delete(Class clazz, Object id){
        // 1. 通过clazz获取TableInfo
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + "=? ";
        executeDML(sql, new Object[]{id});
    }

    /**
     * 删除指定对象,根据对象的类找到表,对象主键为对应记录主键
     * @param obj 删除对象
     */
    public void delete(Object obj){
        Class clazz = obj.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        // 通过反射调用get set方法
//        try {
//            Method method = clazz.getMethod("get" + StringUtils.firstChar2Upper(onlyPriKey.getName()), null);
//            Object priKeyValue = method.invoke(obj, null);
//
//            String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + "=? ";
//            executeDML(sql, new Object[]{priKeyValue});
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

        Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), obj);
        delete(clazz, priKeyValue);
    }

    /**
     * 更新对应记录,更新指定字段
     * @param obj   更新的对象
     * @param fieldNames    更新的属性列表
     * @return
     */
    public int update(Object obj, String[] fieldNames){
        Class clazz = obj.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(clazz);

        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set ");
        for (String fieldName : fieldNames) {
            Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
            if (fieldValue != null) {
                sql.append(fieldName + "=?,");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1, ' ');

        sql.append("where " + onlyPriKey.getName() + "=? ");

        params.add(ReflectUtils.invokeGet(onlyPriKey.getName(), obj));

        return executeDML(sql.toString(), params.toArray());
    }

    /**
     * 返回多行记录,并将每行记录封装到clazz的指定类
     * @param sql   查询语句
     * @param clazz 封装javabean类的class对象
     * @param params    sql参数
     * @return  查询得到的结果
     */
    public List queryRows(String sql, Class clazz, Object[] params){

        return (List) executeQueryTemplate(sql, clazz, params, new Callback() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                List list = null;
                try {
                    ResultSetMetaData metaData = rs.getMetaData();
                    while (rs.next()) {
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        Object rowObj = clazz.newInstance();
                        for (int i = 0; i < metaData.getColumnCount(); i++) {
                            String columnName = metaData.getColumnLabel(i + 1);
                            Object columnValue = rs.getObject(i + 1);
                            ReflectUtils.invokeSet(rowObj, columnName, columnValue);
                        }
                        list.add(rowObj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return list;
            }
        });

    }


    /**
     * 查询返回一行记录,并将每行记录封装到clazz的指定类
     * @param sql   查询语句
     * @param clazz 封装javabean类的class对象
     * @param params    sql参数
     * @return  查询得到的结果
     */
    public Object queryUniqueRow(String sql, Class clazz, Object[] params){
        List list = queryRows(sql, clazz, params);
        return list.get(0);
    }


    /**
     * 查询返回一行一列
     * @param sql   查询语句
     * @param params    sql参数
     * @return  查询得到的结果
     */
    public Object queryValue(String sql, Object[] params){

        return executeQueryTemplate(sql, null, params, new Callback() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                Object value = null;
                try {
                    while (rs.next()) {
                        value = rs.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });
    }


    /**
     * 查询返回一行一列
     * @param sql   查询语句
     * @param params    sql参数
     * @return  查询得到的结果
     */
    public Number queryNumber(String sql, Object[] params){
        return (Number) queryValue(sql, params);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param size
     * @return
     */
    public Object queryPagenate(int pageNum, int size) {
        return null;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
