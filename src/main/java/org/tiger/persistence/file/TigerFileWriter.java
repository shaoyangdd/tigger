package org.tiger.persistence.file;

import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.SingletonBean;

import java.io.*;

import static org.tiger.common.Constant.LINE_SEPARATOR;

/**
 * 文件写
 *
 * @author 康绍飞
 * @date 2021-02-03
 */
@SingletonBean
public class TigerFileWriter {

    @Inject
    private FilePathResolver filePathResolver;

    @Inject
    private RecordOperator recordOperator;

    public void writeOneLineNoAppend(File file, String record) {
        writeLine(file, record, false);
    }

    public void writeOneLine(File file, String record) {
        writeLine(file, record, true);
    }

    public void writeByStartIndex(Record record) {
        File file = filePathResolver.getFile(record);
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "rw");
            //将记录指针移动到文件最后
            raf.seek(raf.length());
            raf.write((recordOperator.toString(record) + LINE_SEPARATOR).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Record record, boolean append) {
        File file = filePathResolver.getFile(record);
        //固定长度，所以除一下就行
        record.setStartIndex(file.length() / (record.recordLength() + LINE_SEPARATOR.length()));
        writeLine(file, recordOperator.toString(record), append);
    }

    private void writeLine(File file, String string, boolean append) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, append));
            bufferedWriter.write(string);
            bufferedWriter.write(LINE_SEPARATOR);
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
