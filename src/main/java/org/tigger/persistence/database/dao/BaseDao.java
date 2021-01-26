package org.tigger.persistence.database.dao;

import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.persistence.DataPersistence;
import org.tigger.persistence.file.Record;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDao<T extends Record> implements DataPersistence<T> {

    protected static Connection getConnection() {
        return MemoryShareDataRegion.connectionPool.getConnection();
    }

    protected static void closeAndPutBack(Statement statement, ResultSet resultSet, Connection connection) {
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

    public int insert(T record) {
        return 1;
    }

    @Override
    public T findOne(T record) {
        return null;
    }

    @Override
    public List<T> findList(T record) {
        return null;
    }

    @Override
    public int update(T record) {
        return 0;
    }

    @Override
    public int delete(T record) {
        return 0;
    }
}
