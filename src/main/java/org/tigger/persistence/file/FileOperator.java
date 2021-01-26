package org.tigger.persistence.file;

import org.tigger.persistence.DataPersistence;

import java.util.List;

/**
 * 文件持久化接口
 *
 * @author kangshaofei
 * @date 2020-01-25
 */
public class FileOperator<T extends Record> implements DataPersistence<T> {

    private TigerFileWriter tigerFileWriter;

    public int insert(T record) {
        tigerFileWriter.write(record);
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
