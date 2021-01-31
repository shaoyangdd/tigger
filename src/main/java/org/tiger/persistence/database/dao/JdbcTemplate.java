package org.tiger.persistence.database.dao;

import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.file.Record;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 业务库操作工具类
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
@SingletonBean
public class JdbcTemplate implements BaseDao<Record> {

    /**
     * 获取总数
     *
     * @param countSql 获取总数的SQL
     * @return 总数
     */
    public long getCount(String countSql) {
        Connection connection = getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(countSql);
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new RuntimeException("没有查到总数！");
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        } finally {
            closeAndPutBack(statement, resultSet, connection);
        }
    }


    @Override
    public int insert(Record record) {
        return 0;
    }

    @Override
    public Record findOne(Record record) {
        return null;
    }

    @Override
    public List<Record> findList(Record record) {
        return null;
    }

    @Override
    public int update(Record record) {
        return 0;
    }

    @Override
    public int delete(Record record) {
        return 0;
    }
}
