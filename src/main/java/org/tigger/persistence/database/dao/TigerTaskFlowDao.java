package org.tigger.persistence.database.dao;

import org.tigger.common.datastruct.TigerTaskFlow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TigerTaskFlowDao extends BaseDao {

    public static List<TigerTaskFlow> getTigerTaskFlowByPreviousId(String previousId) {
        Connection connection = getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from tiger_task_flow where previous_task_id='" + previousId + "'");
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAndPutBack(statement, resultSet, connection);
        }
    }

}
