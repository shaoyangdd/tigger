package org.tigger.persistence.file;

import org.tigger.common.util.StringUtil;

import java.lang.reflect.Field;
import java.sql.Timestamp;

public class RecordOperator {

    public String toString(Record record) throws IllegalAccessException {

        Field[] fields = record.getClass().getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                String fileValue = String.valueOf(field.get(record));
                String padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
                stringBuilder.append(padValue);
            } else if (field.getType() == Long.class) {
                String fileValue = String.valueOf(field.getLong(record));
                String padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
                stringBuilder.append(padValue);
            } else if (field.getType() == Integer.class) {
                String fileValue = String.valueOf(field.getInt(record));
                String padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
                stringBuilder.append(padValue);
            } else if (field.getType() == Timestamp.class) {
                String fileValue = String.valueOf(field.getLong(record));
                String padValue = StringUtil.leftPadSpace(fileValue, FieldMapping.getLengthByClass(field.getClass()));
                stringBuilder.append(padValue);
            } else {
                throw new RuntimeException("不支持的类型:" + record.getClass().getSimpleName() + " ,filed:" + field.getName());
            }
        }
        return stringBuilder.toString();

    }
}
