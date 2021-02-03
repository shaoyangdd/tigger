package org.tiger.file;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.persistence.file.TigerFileReader;

import java.io.File;

@EnableIoc(scanPackages = "org.tiger")
public class TigerFileReaderTest {


    private TigerFileReader tigerFileReader;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        tigerFileReader = BeanFactory.getBean(TigerFileReader.class);
    }

    @Test
    public void readOneLine() {
        File file = new File("/Users/kangshaofei/Documents/project/tigger/src/test/java/org/tiger/file/TigerFileReaderTest.java");
        System.out.println(tigerFileReader.readOneLine(file));
    }

    @Test
    public void readAndFind() {
        TestRecord recordTest = new TestRecord();
        recordTest.setName("tom1");
        System.out.println(tigerFileReader.readAndFind(recordTest, (line, record) -> {
            TestRecord testRecord = JSON.parseObject(line, TestRecord.class);
            if (testRecord.getName().equals(recordTest.getName())) {
                return true;
            }
            return false;
        }));
    }
}
