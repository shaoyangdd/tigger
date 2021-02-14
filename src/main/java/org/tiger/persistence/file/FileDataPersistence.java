package org.tiger.persistence.file;

import com.alibaba.fastjson.JSON;
import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.DataPersistence;
import org.tiger.persistence.file.id.IdGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 文件持久化接口
 *
 * @author kangshaofei
 * @date 2020-01-25
 */
@SingletonBean
public class FileDataPersistence<T extends Record> implements DataPersistence<T> {

    @Inject
    private IdGenerator idGenerator;
    @Inject
    private TigerFileWriter tigerFileWriter;
    @Inject
    private TigerFileReader tigerFileReader;
    @Inject
    private RecordOperator recordOperator;

    public int insert(T record) {
        record.setId(idGenerator.getNextSeq());
        tigerFileWriter.write(record, true);
        return record.getId().intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findOne(T record) {
        String line = tigerFileReader.readAndFind(record, (s) -> {
            Record fileRecord = recordOperator.stringToRecord(s, record.getClass());
            if (record.getId() != null && fileRecord.getId() != null) {
                if (record.getId().equals(fileRecord.getId())) {
                    return true;
                }
            }
            return (fileRecord.getUnionKey() != null && fileRecord.getUnionKey().equals(record.getUnionKey()));
        });
        return (T) recordOperator.stringToRecord(line, record.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findList(T record) {
        List<String> list = tigerFileReader.readAndFindList(record, (line, paramMap) -> {
            Map<String, Object> lineMap = JSON.parseObject(line, Map.class);
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Object recordValue = lineMap.get(key);
                //TODO 先写死，后面好好搞一下
                if ("previousTaskId".equals(key)) {
                    String[] values = ((String) recordValue).split(",");
                    if (Arrays.asList(values).contains(value)) {
                        return true;
                    }
                } else if (value.equals(recordValue)) {
                    return true;
                }
            }
            return false;
        });
        List<T> recordList = new ArrayList<>(list.size());
        list.forEach(s -> {
            recordList.add((T) recordOperator.stringToRecord(s, record.getClass()));
        });
        return recordList;
    }

    @Override
    public int update(T record) {
        List<T> list = findList(record);
        for (T t : list) {
            //TODO 记录中加一个索引字段，记录行的起始位置然后用随机写文件的方式来写文件
        }
        return 0;
    }

    @Override
    public int delete(T record) {
        //暂时没有删除，先不实现
        return 0;
    }
}
