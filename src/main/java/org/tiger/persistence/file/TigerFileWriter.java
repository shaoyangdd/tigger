package org.tiger.persistence.file;

import org.tiger.common.ioc.InjectByType;
import org.tiger.common.ioc.SingletonBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SingletonBean
public class TigerFileWriter {

    @InjectByType
    private FilePathResolver filePathResolver;

    @InjectByType
    private RecordOperator recordOperator;

    public void writeOneLine(File file, String record) {
        writeLine(file, record);
    }

    public void write(Record record) {
        writeLine(filePathResolver.getFile(record), recordOperator.toString(record));
    }

    private void writeLine(File file, String string) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
