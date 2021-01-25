package org.tigger.persistence.file;

/**
 * 文件行记录JSON定长格式
 * 所有的字段都要有key，值补到最大长度，所有值都是字符串类型
 *
 * @author 康绍飞
 * @date 2021-01-25
 */
public interface Record {

    String toString();

}
