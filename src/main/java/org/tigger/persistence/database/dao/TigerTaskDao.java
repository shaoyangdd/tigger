package org.tigger.persistence.database.dao;

import org.tigger.persistence.database.dao.entity.TigerTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TigerTaskDao extends BaseDao {

    public static TigerTask getTigerTaskByName(String taskName) {
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
}
