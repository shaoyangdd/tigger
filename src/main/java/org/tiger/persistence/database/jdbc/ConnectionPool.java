package org.tiger.persistence.database.jdbc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.parameter.Parameters;
import org.tiger.common.util.StringUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 数据库连接池
 *
 * @author kangshaofei
 * @date 20200412
 */
@SingletonBean
public class ConnectionPool {

    private static Logger logger = LoggerFactory.getLogger(ConnectionPool.class.getSimpleName());

    private int totalSize = 10;

    private int initSize = 10;

    private ArrayBlockingQueue<Connection> connectionQueue;

    static {
        //1.加载驱动程序
        try {
            logger.info("加载mysql驱动");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectionPool() {

    }

    /**
     * 初始化连接池
     */
    //@AfterInstance
    public void init() {
        String user = Parameters.get("database.username");
        String password = Parameters.get("database.password");
        String url = Parameters.get("database.url");
        if (StringUtil.isNotEmpty(url)) {
            logger.info("初始化连接池:userName:" + user + ",password:" + password + ",url:" + url);
            connectionQueue = new ArrayBlockingQueue<>(totalSize);
            for (int i = 0; i < initSize; i++) {
                Connection conn;
                try {
                    conn = DriverManager.getConnection(url, user, password);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                connectionQueue.add(conn);
            }
            logger.info("初始化连接池结束，总连接数:" + totalSize + ",初始连接数:" + initSize);
        } else {
            logger.info("没有数据库连接配置，不需要初始化");
        }
    }

    /**
     * 获取连接
     *
     * @return Connection
     */
    public Connection getConnection() {
        try {
            return connectionQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 归还连接
     */
    public void putBackConnection(Connection connection) {
        connectionQueue.add(connection);
    }

    public int getPoolSize() {
        return connectionQueue.size();
    }

}
