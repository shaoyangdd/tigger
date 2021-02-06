package org.tiger.file;

import org.tiger.persistence.file.Record;

import java.util.Map;

public class TestWriterRecord implements Record {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(long id) {

    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getUnionKey() {
        return null;
    }

    @Override
    public Map<String, Object> searchParam() {
        return null;
    }
}
