package org.tigger.persistence.file.id;

import org.tigger.persistence.file.TigerFileReader;
import org.tigger.persistence.file.TigerFileWriter;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 序号生成器 TODO 效率有点低，后面看看怎么优化，先实现再说
 * 只适用于单机
 *
 * @author 康绍飞
 * @date 2020-01-26
 */
public class IdGenerator {

    private String idPath;

    private AtomicInteger atomicInteger;

    private TigerFileReader tigerFileReader;

    private TigerFileWriter tigerFileWriter;

    private File file;

    public IdGenerator() {
        String seq = tigerFileReader.readOneLine(new File(idPath));
        atomicInteger = new AtomicInteger(Integer.parseInt(seq));
        file = new File(idPath);
        if (!file.exists()) {
            throw new RuntimeException("序号文件不存在:" + idPath);
        }
    }

    synchronized public String getNextSeq() {
        String id = String.valueOf(atomicInteger.getAndDecrement());
        tigerFileWriter.writeOneLine(file, id);
        return id;
    }
}
