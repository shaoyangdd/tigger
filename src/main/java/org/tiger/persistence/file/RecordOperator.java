package org.tiger.persistence.file;

import com.alibaba.fastjson.JSON;
import org.tiger.common.ioc.SingletonBean;

/**
 * 记录操作器
 *
 * @author 康绍飞
 * @date 2021-01-26
 */
@SingletonBean
public class RecordOperator {

    /**
     * 实体转成字符串，JSON字符串，value定长，不足补空格，文件更新时直接更新，不用挪位置
     *
     * @param record 实体
     * @return 字符串
     */
    public String toString(Record record) {
//        Map<String, String> recordMap = new HashMap<>();
//        try {
//            Field[] fields = record.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                String fileValue;
//                String padValue;
//                if (field.getType() == String.class) {
//                    fileValue = String.valueOf(field.get(record));
//                    padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
//                } else if (field.getType() == Long.class) {
//                    fileValue = String.valueOf(field.getLong(record));
//                    padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
//                } else if (field.getType() == Integer.class) {
//                    fileValue = String.valueOf(field.getInt(record));
//                    padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
//                } else if (field.getType() == Timestamp.class) {
//                    fileValue = String.valueOf(field.getLong(record));
//                    padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
//                } else {
//                    throw new RuntimeException("不支持的类型:" + record.getClass().getSimpleName() + " ,filed:" + field.getName());
//                }
//                recordMap.put(field.getName(), padValue);
//            }
//            return JSON.toJSONString(recordMap);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        //TODO 先不补齐长度了，后面再考虑更新时覆盖
        return JSON.toJSONString(record);
    }

    /**
     * JSON字符串转Record对象
     *
     * @param string
     * @return
     */
    public Record stringToRecord(String string, Class<? extends Record> clazz) {
        return JSON.parseObject(string, clazz);
    }

}
