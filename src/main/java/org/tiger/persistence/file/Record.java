package org.tiger.persistence.file;

import java.util.Map;

/**
 * 文件行记录JSON定长格式
 * 所有的字段都要有key，值补到最大长度，所有值都是字符串类型
 *
 * @author 康绍飞
 * @date 2021-01-25
 */
public interface Record {

    /**
     * 设置记录在文件中的起始位置
     *
     * @param index 起始位置
     */
    void setStartIndex(long index);

    /**
     * 设置物理主键
     *
     * @param id 主键
     */
    void setId(long id);

    /**
     * 获取主键
     *
     * @return id
     */
    Long getId();

    /**
     * toString
     *
     * @return string
     */
    String toString();

    /**
     * 获取联合主键，直接拼成字符串
     *
     * @return s
     */
    String getUnionKey();

    /**
     * 查询条件
     *
     * @return map
     */
    Map<String, Object> searchParam();

    /**
     * 记录的长度，固定长度
     *
     * @return int 长度，多少个字节
     */
    int recordLength();
}
