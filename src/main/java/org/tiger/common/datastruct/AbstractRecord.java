package org.tiger.common.datastruct;

import org.tiger.persistence.file.Record;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRecord implements Record {

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
}
