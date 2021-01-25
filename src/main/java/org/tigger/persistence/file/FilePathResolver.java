package org.tigger.persistence.file;

import java.io.File;

public class FilePathResolver {

    public File getFile(Record record) {
        return new File(record.getClass().getName());
    }


}
