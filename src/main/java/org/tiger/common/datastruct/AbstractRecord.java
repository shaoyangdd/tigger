package org.tiger.common.datastruct;

import org.tiger.persistence.file.Record;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRecord implements Record {

    /**
     * 设置记录在文件中的起始位置
     */
    private long startIndex;

    /**
     * 物理主键
     */
    private Long id;

    private Map<String, Object> searchParam = new HashMap<>();

    @Override
    public Map<String, Object> searchParam() {
        return searchParam;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Object> getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(Map<String, Object> searchParam) {
        this.searchParam = searchParam;
    }

    public long getStartIndex() {
        return startIndex;
    }

    @Override
    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public int getRecordLength() {
        //TODO 偷个懒，先定500个字节,后面根据字段上的注解里的长度计算出来
        return 500;
    }
}
