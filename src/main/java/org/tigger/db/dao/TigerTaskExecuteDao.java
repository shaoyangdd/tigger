package org.tigger.db.dao;

import org.tigger.common.MemoryShareDataRegion;
import org.tigger.db.dao.entity.TigerTaskExecute;

import java.sql.*;

public class TigerTaskExecuteDao {

    public static long insert(TigerTaskExecute tigerTaskExecute) {
        Connection connection = MemoryShareDataRegion.connectionPool.getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("insert into tiger_task_execute(task_id,start_time,end_time,task_status,task_executor_ip,task_parameter) values (?,?,?,?,?,?)");
            statement.setLong(1,tigerTaskExecute.getTaskId());
            statement.setTimestamp(2, tigerTaskExecute.getStartTime());
            statement.setTimestamp(3, tigerTaskExecute.getEndTime());
            statement.setString(4, tigerTaskExecute.getTaskStatus());
            statement.setString(5, tigerTaskExecute.getTaskExecutorIp());
            statement.setString(6, tigerTaskExecute.getTaskParameter());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                System.out.println("主键值为：" + resultSet.getObject(1));
            }
            resultSet.close();
            statement.close();
            return (long) resultSet.getObject(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            MemoryShareDataRegion.connectionPool.putBackConnection(connection);
        }
    }

    public static void updateAfterComplete(long id,boolean result) {
        Connection connection = MemoryShareDataRegion.connectionPool.getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("update tiger_task_execute set end_time=?,task_status=? where id=?");
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setString(2, result ? "S" : "F");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            MemoryShareDataRegion.connectionPool.putBackConnection(connection);
        }
    }
}
