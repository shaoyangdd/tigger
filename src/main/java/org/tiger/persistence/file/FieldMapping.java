package org.tiger.persistence.file;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 字段类型映射，只保留JAVA开发工作中最常用的类型
 *
 * @author 康绍飞
 * @date 2021-01-25
 */
public enum FieldMapping {

    /**
     * INT类型 字符长度 String.valueOf(Integer.MAX_VALUE).length()
     */
    INT(new Class[]{Integer.class}, 10, null),

    /**
     * LONG类型 String.valueOf(Long.MAX_VALUE).length()
     */
    LONG(new Class[]{Long.class}, 20, null),

    /**
     * 只有一个字符
     */
    CHAR(new Class[]{Character.class}, 1, null),

    /**
     * true 或false 4个字符
     */
    BOOLEAN(new Class[]{Boolean.class}, 4, null),

    /**
     * 日期 yyyy-MM-dd
     */
    DATE(new Class[]{Date.class, LocalDate.class}, 10, "yyyy-MM-dd"),

    /**
     * 日期 yyyy-MM-dd HH:mm:ss
     */
    DATE_TIME(new Class[]{LocalDateTime.class}, 10, "yyyy-MM-dd HH:mm:ss"),

    /**
     * 时间戳 long类型 长度同long
     */
    TIMESTAMP(new Class[]{Timestamp.class}, 20, null),

    /**
     * 存小数
     */
    BIGDECIMAL(new Class[]{BigDecimal.class}, 20, null);

    private Class[] javaClasses;

    private int length;

    private String pattern;


    FieldMapping(Class[] javaClasses, int length, String pattern) {
        this.javaClasses = javaClasses;
        this.length = length;
        this.pattern = pattern;
    }

    public Class[] getJavaClasses() {
        return javaClasses;
    }

    public int getLength() {
        return length;
    }

    public String getPattern() {
        return pattern;
    }

    public static FieldMapping getEnumByClass(Class clazz) {
        for (FieldMapping value : FieldMapping.values()) {
            for (Class getaClass : value.getJavaClasses()) {
                if (clazz == getaClass) {
                    return value;
                }
            }
        }
        throw new RuntimeException("不支持此类型:" + clazz.toString());
    }

    public static int getLengthByClass(Class clazz) {
        return getEnumByClass(clazz).getLength();
    }

    public static String getPatternByClass(Class clazz) {
        return getEnumByClass(clazz).getPattern();
    }
}
