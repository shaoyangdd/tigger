package org.tigger.persistence;

import org.tigger.persistence.file.Record;

/**
 * 数据持久化接口，支持文件，数据库
 *
 * @author kangshaofei
 * @date 2020-01-25
 */
public interface DataPersistence {

    int insert(Record record);

}
