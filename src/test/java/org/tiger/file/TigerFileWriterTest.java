package org.tiger.file;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.persistence.file.TigerFileWriter;

import java.io.File;

@EnableIoc(scanPackages = "org.tiger")
public class TigerFileWriterTest {

    private Logger logger = LoggerFactory.getLogger(TigerFileWriterTest.class);

    private TigerFileWriter tigerFileWriter;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        tigerFileWriter = BeanFactory.getBean(TigerFileWriter.class);
    }

    @Test
    public void writeOneLine() {
        File file = new File("C:\\project\\tiger\\src\\test\\resources\\db_file\\WriteOnline.txt");
        tigerFileWriter.writeOneLine(file, "写一行看看");
    }

    @Test
    public void write() {
        TestWriterRecord testRecord = new TestWriterRecord();
        testRecord.setName("test write!!");
        tigerFileWriter.write(testRecord);
    }
}
