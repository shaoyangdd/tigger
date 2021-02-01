package org.tiger.persistence.file;

import com.alibaba.fastjson.JSON;
import org.tiger.common.ioc.InjectByType;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.DataPersistence;
import org.tiger.persistence.file.id.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文件持久化接口
 *
 * @author kangshaofei
 * @date 2020-01-25
 */
@SingletonBean
public class FileDataPersistence<T extends Record> implements DataPersistence<T> {

    @InjectByType
    private IdGenerator idGenerator;
    @InjectByType
    private TigerFileWriter tigerFileWriter;
    @InjectByType
    private TigerFileReader tigerFileReader;
    @InjectByType
    private RecordOperator recordOperator;

    public int insert(T record) {
        record.setId(idGenerator.getNextSeq());
        tigerFileWriter.write(record);
        return (int) record.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findOne(T record) {
        String line = tigerFileReader.readAndFind(record, (s, r) -> {
            Record fileRecord = recordOperator.stringToRecord(s, record.getClass());
            return record.getId() == fileRecord.getId()
                    || (fileRecord.getUnionKey() != null && fileRecord.getUnionKey().equals(record.getUnionKey()));
        });
        return (T) recordOperator.stringToRecord(line, record.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findList(T record) {
        List<String> list = tigerFileReader.readAndFindList(record, ((line, paramMap) -> {
            Map<String, Object> lineMap = JSON.parseObject(line, Map.class);
            AtomicBoolean find = new AtomicBoolean(true);
            paramMap.forEach((k, v) -> {
                if (!v.equals(lineMap.get(k))) {
                    find.set(false);
                }
            });
            return find.get();
        }));
        List<T> recordList = new ArrayList<>(list.size());
        list.forEach(s -> {
            recordList.add((T) recordOperator.stringToRecord(s, record.getClass()));
        });
        return recordList;
    }

    @Override
    public int update(T record) {
        //暂时没有更新，先不实现
        return 0;
    }

    @Override
    public int delete(T record) {
        //暂时没有删除，先不实现
        return 0;
    }
}
