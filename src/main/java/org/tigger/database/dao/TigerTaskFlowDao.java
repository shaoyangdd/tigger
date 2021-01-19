package org.tigger.database.dao;

import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.database.dao.entity.TigerTaskFlow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TigerTaskFlowDao {

    public static List<TigerTaskFlow> getTigerTaskFlow(String previousId) {
        Connection connection = MemoryShareDataRegion.connectionPool.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tiger_task_flow where previous_task_id='" + previousId + "'");
            List<TigerTaskFlow> tigerTaskFlows = new ArrayList<>();
            while (resultSet.next()) {
                TigerTaskFlow tigerTaskFlow = new TigerTaskFlow();
                tigerTaskFlow.setId(resultSet.getLong(1));
                tigerTaskFlow.setTaskName(resultSet.getString(2));
                tigerTaskFlow.setPreviousTaskId(resultSet.getLong(3));
                tigerTaskFlow.setPreviousTaskStatus(resultSet.getString(4));
                tigerTaskFlow.setNextTaskId(resultSet.getLong(5));
                tigerTaskFlow.setTaskParameter(resultSet.getString(6));
                tigerTaskFlows.add(tigerTaskFlow);
            }
            return tigerTaskFlows;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        } finally {
            MemoryShareDataRegion.connectionPool.putBackConnection(connection);
        }
    }

}
