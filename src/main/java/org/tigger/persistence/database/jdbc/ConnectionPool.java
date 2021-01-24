package org.tigger.persistence.database.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * 数据库连接池
 *
 * @author kangshaofei
 * @date 20200412
 */
public class ConnectionPool {

    private static Logger logger = Logger.getLogger(ConnectionPool.class.getSimpleName());

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
        init(initSize, totalSize);
    }

    /**
     * 初始化连接池
     *
     * @param size      初始连接数
     * @param totalSize 总连接数
     */
    public void init(int size, int totalSize) {
        String user = PropertiesContainer.getProperty(PropertiesContainer.PropertyKey.DATABASE_USER_NAME);
        String password = PropertiesContainer.getProperty(PropertiesContainer.PropertyKey.DATABASE_PASSWORD);
        String url = PropertiesContainer.getProperty(PropertiesContainer.PropertyKey.DATABASE_URL);
        logger.info("初始化连接池:userName:"+user + ",password:"+ password +",url:" + url);
        connectionQueue = new ArrayBlockingQueue<>(totalSize);
        for (int i = 0; i < size; i++) {
            Connection conn;
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connectionQueue.add(conn);
        }
        logger.info("初始化连接池结束，总连接数:" + totalSize + ",初始连接数:" + size);
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
