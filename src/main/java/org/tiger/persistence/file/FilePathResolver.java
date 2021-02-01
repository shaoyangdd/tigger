package org.tiger.persistence.file;

import org.tiger.common.ioc.SingletonBean;

import java.io.File;

@SingletonBean
public class FilePathResolver {

    public File getFile(Record record) {
        return new File(record.getClass().getName());
    }


}
