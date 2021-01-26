package org.tigger.persistence;

import org.tigger.persistence.file.Record;

import java.util.List;

/**
 * 数据持久化接口，支持文件，数据库
 *
 * @author kangshaofei
 * @date 2020-01-25
 */
public interface DataPersistence<T extends Record> {

    int insert(T record);

    T findOne(T record);

    List<T> findList(T record);

    int update(T record);

    int delete(T record);

}
