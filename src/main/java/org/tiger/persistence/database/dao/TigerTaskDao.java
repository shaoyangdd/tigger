package org.tiger.persistence.database.dao;

import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.SingletonBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@SingletonBean
public class TigerTaskDao implements BaseDao<TigerTask> {

    public TigerTask getTigerTaskByName(String taskName) {
        Connection connection = getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from tiger_task where task_name=" + taskName);
            List<TigerTask> tigerTaskList = new ArrayList<>();
            while (resultSet.next()) {
                TigerTask tigerTask = new TigerTask();
                tigerTask.setId(resultSet.getLong(1));
                tigerTask.setTaskName(resultSet.getString(2));
                tigerTask.setTaskLayerId(resultSet.getString(3));
                tigerTask.setTaskParameter(resultSet.getString(4));
                tigerTaskList.add(tigerTask);
            }
            return tigerTaskList.get(0);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        } finally {
            closeAndPutBack(statement, resultSet, connection);
        }
    }

    @Override
    public int insert(TigerTask record) {
        return 0;
    }

    @Override
    public TigerTask findOne(TigerTask record) {
        return getTigerTaskByName(record.getTaskName());
    }

    @Override
    public List<TigerTask> findList(TigerTask record) {
        return null;
    }

    @Override
    public int update(TigerTask record) {
        return 0;
    }

    @Override
    public int delete(TigerTask record) {
        return 0;
    }
}
