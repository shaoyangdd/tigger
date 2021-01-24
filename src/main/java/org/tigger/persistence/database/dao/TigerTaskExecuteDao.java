package org.tigger.persistence.database.dao;

import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.persistence.database.dao.entity.TigerTaskExecute;

import java.sql.*;

public class TigerTaskExecuteDao extends BaseDao {

    public static long insert(TigerTaskExecute tigerTaskExecute) {
        Connection connection = MemoryShareDataRegion.connectionPool.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("insert into tiger_task_execute(task_id,start_time,end_time,task_status,task_executor_ip,task_parameter) values (?,?,?,?,?,?)");
            statement.setLong(1, tigerTaskExecute.getTaskId());
            statement.setTimestamp(2, tigerTaskExecute.getStartTime());
            statement.setTimestamp(3, tigerTaskExecute.getEndTime());
            statement.setString(4, tigerTaskExecute.getTaskStatus());
            statement.setString(5, tigerTaskExecute.getTaskExecutorIp());
            statement.setString(6, tigerTaskExecute.getTaskParameter());

            statement.executeUpdate();
            statement.getGeneratedKeys();
            if (resultSet.next()) {
                System.out.println("主键值为：" + resultSet.getObject(1));
            }
            return (long) resultSet.getObject(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAndPutBack(statement, resultSet, connection);
        }
    }

    public static void updateAfterComplete(long id,boolean result) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update tiger_task_execute set end_time=?,task_status=? where id=?");
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setString(2, result ? "S" : "F");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAndPutBack(statement, null, connection);
        }
    }
}
