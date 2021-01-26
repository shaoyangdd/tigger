package org.tigger.persistence.file.id;

import org.tigger.persistence.file.TigerFileReader;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private String idPath;

    private AtomicInteger atomicInteger;

    private TigerFileReader tigerFileReader;

    public void init() {
        String seq = tigerFileReader.readOneLine(new File(idPath));
        atomicInteger = new AtomicInteger(Integer.parseInt(seq));
    }

    public int getNextSeq() {
        return atomicInteger.getAndDecrement();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }
}
