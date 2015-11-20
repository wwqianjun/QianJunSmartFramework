package com.qianjun.helper;

import com.qianjun.utils.CollectionUtil;
import com.qianjun.utils.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by ZiJun
 * Description:
 * Date: 2015/11/18 :18:03.
 */
public final class DatabaseHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseHelper.class);

    // --------------DbUtils使用--------------------------------
    private static final QueryRunner QUERY_RUNNER;

    // --------------线程安全，确保connection每个线程只有一个--------
    private static final ThreadLocal<Connection> CONNECTION_HOLDE;

    // --------------s数据库连接池，减少getConnection每次与数据库的直接连接
    private static final BasicDataSource DATA_SOURCE;

    // --------------数据库连接字段-------------------------------
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        QUERY_RUNNER = new QueryRunner();
        CONNECTION_HOLDE = new ThreadLocal<Connection>();

        Properties conf = PropsUtil.loadPros("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL  = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        // 改用dbcp连接
//        try {
//            Class.forName(DRIVER);
//        }catch (ClassNotFoundException e){
//            LOG.error("can`t load jdbc driver",e);
//        }

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }

    /**
     * 获取数据库连接 unSafeThread
     */
    public static Connection getConnectionUnSafeThread(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOG.error("get db connection failure", e);
        }

        return  conn;
    }

    /**
     * 获取数据库连接 safeThread but 重复与数据库连接
     */
    public static Connection getConnectionRepeatDB(){
        Connection conn = CONNECTION_HOLDE.get();
        if ( conn == null ){
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                LOG.error("get db connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDE.set(conn);
            }
        }

        return conn;
    }

    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDE.get();
        if ( conn == null ){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOG.error("get db connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDE.set(conn);
            }
        }

        return conn;
    }

    /**
     * 关闭数据库连接 unSafeThread
     */
    public  static void closeConnection(Connection conn){
        if ( conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("close db connection failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDE.remove();// Connection使用完毕后，移除ThreadLocal中持有的Connection
            }
        }
    }

    /**
     * 关闭数据库连接
     */
    public  static void closeConnection(){
        Connection conn = CONNECTION_HOLDE.get();
        if ( conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("close db connection failure", e);
            }
        }
    }

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,Connection conn, String sql, Object ... params){
        List<T> entityList;
        try {
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity list failure", e);
            throw  new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体列表
     */
    public  static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object ... params){
        try {
            Connection conn = getConnection();
            return queryEntityList(entityClass, conn, sql, params);
        } catch (Exception e){
            throw  new RuntimeException(e);
        } /*finally {
            closeConnection();
        }*/
    }

    /**
     * 查询实体
     */
    public  static <T> T queryEntity(Class<T> entityClass, String sql, Object ... params){
        T entity;

        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity failure", e);
            throw new RuntimeException(e);
        } /*finally {
            closeConnection();
        }*/

        return entity;
    }

    /**
     * 执行查询语句
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object ... params){
        List<Map<String, Object>> result;
        try{
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (Exception e){
            LOG.error("execute query failure ",e);
            throw new RuntimeException(e);
        }
        return  result;
    }

    /**
     * 执行更新语句（包括update、insert、delete）
     */
    public static int executeUpdate(String sql, Object ... params){
        int rows = 0;
        try{
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e){
            LOG.error("execute update failure ", e);
            throw  new RuntimeException(e);
        }
        return rows;
    }

    // --------------基于上述方法，对应insert update delete---------------
    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOG.error("can`t insert entity: filedMap is empty!");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        // 要插入的字段(field1,field2,...)
        StringBuilder columns = new StringBuilder("(");
        // 要插入的值(value1,value2,...)
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }

        // 去最后一个ex:2345, (4,5]
        columns.replace(columns.lastIndexOf(","), columns.length(),")");
        values.replace(values.lastIndexOf(","), values.length(),")");

        // 拼接成完整的sql
        sql += columns + "VALUES" + values;

        Object[] parms = fieldMap.values().toArray();

        return executeUpdate(sql, parms) == 1;
    }

    /**
     * 删除实体
     *
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id){
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }

    private static <T> String getTableName(Class<T> entityClass) {
        return  entityClass.getSimpleName();
    }
}
