package org.tigger.persistence.file;

import org.tigger.persistence.DataPersistence;

/**
 * 文件持久化接口
 *
 * @author kangshaofei
 * @date 2020-01-25
 */
public class FileOperator implements DataPersistence {

    private TigerFileWriter tigerFileWriter;

    public int insert(Record record) {
        tigerFileWriter.write(record);
        return 1;
    }

}
