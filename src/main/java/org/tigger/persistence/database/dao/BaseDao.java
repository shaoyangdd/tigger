package org.tigger.persistence.database.dao;

import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.persistence.DataPersistence;
import org.tigger.persistence.file.Record;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface BaseDao<T extends Record> extends DataPersistence<T> {

    default Connection getConnection() {
        return MemoryShareDataRegion.connectionPool.getConnection();
    }

    default void closeAndPutBack(Statement statement, ResultSet resultSet, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        MemoryShareDataRegion.connectionPool.putBackConnection(connection);
    }
}
