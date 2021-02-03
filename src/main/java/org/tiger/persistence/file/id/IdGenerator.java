package org.tiger.persistence.file.id;

import org.tiger.common.ioc.AfterInstance;
import org.tiger.common.ioc.InjectByType;
import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.file.TigerFileReader;
import org.tiger.persistence.file.TigerFileWriter;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序号生成器 TODO 效率有点低，后面看看怎么优化，先实现再说
 * 只适用于单机
 *
 * @author 康绍飞
 * @date 2020-01-26
 */
@SingletonBean
public class IdGenerator {

    @InjectParameter
    private String idPath;

    private AtomicLong atomicLong;

    @InjectByType
    private TigerFileReader tigerFileReader;

    @InjectByType
    private TigerFileWriter tigerFileWriter;

    private File file;

    public IdGenerator() {
    }

    synchronized public long getNextSeq() {
        long seq = atomicLong.getAndIncrement();
        tigerFileWriter.writeOneLine(file, String.valueOf(seq));
        return seq;
    }

    @AfterInstance
    public void init() {
        String seq = tigerFileReader.readOneLine(new File(idPath));
        atomicLong = new AtomicLong(Integer.parseInt(seq));
        file = new File(idPath);
        if (!file.exists()) {
            throw new RuntimeException("序号文件不存在:" + idPath);
        }
    }
}
