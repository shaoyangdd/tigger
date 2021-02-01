package org.tiger.persistence.database.dao;

import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.ioc.SingletonBean;

import java.sql.*;
import java.util.List;

@SingletonBean
public class TigerTaskExecuteDao implements BaseDao<TigerTaskExecute> {

    public long insertOne(TigerTaskExecute tigerTaskExecute) {
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

    @Override
    public int insert(TigerTaskExecute record) {
        return (int) insertOne(record);
    }

    @Override
    public TigerTaskExecute findOne(TigerTaskExecute record) {
        return null;
    }

    @Override
    public List<TigerTaskExecute> findList(TigerTaskExecute record) {
        return null;
    }

    @Override
    public int update(TigerTaskExecute record) {
        updateAfterComplete(record.getId(), "S".equals(record.getTaskStatus()) ? true : false);
        return 0;
    }

    @Override
    public int delete(TigerTaskExecute record) {
        return 0;
    }

    public void updateAfterComplete(long id, boolean result) {
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
