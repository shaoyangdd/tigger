package org.tiger.persistence.file;

import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;

import java.io.File;

@SingletonBean
public class FilePathResolver {

    @InjectParameter
    private String dbFilePath;

    public File getFile(Record record) {
        return new File(dbFilePath + record.getClass().getSimpleName() + ".txt");
    }


}
