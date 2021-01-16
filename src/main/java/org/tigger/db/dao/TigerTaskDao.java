package org.tigger.db.dao;

import org.tigger.common.MemoryShareDataRegion;
import org.tigger.db.dao.entity.TigerTask;
import org.tigger.db.dao.entity.TigerTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TigerTaskDao {

    public static TigerTask getTigerTaskById(long id) {
        Connection connection = MemoryShareDataRegion.connectionPool.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tiger_task where id=" + id);
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
        }
    }
}
