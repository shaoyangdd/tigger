package org.tiger.file;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.persistence.file.FindConditionMapParam;
import org.tiger.persistence.file.TigerFileReader;

import java.io.File;
import java.util.List;

@EnableIoc(scanPackages = "org.tiger")
public class TigerFileReaderTest {

    private Logger logger = LoggerFactory.getLogger(TigerFileReaderTest.class);

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
        System.out.println(tigerFileReader.readAndFind(recordTest, (line) -> {
            TestRecord testRecord = JSON.parseObject(line, TestRecord.class);
            if (testRecord.getName().equals(recordTest.getName())) {
                return true;
            }
            return false;
        }));
    }

    @Test
    public void readAndFindList() {
        TestRecord recordTest = new TestRecord();
        recordTest.setName("tom5");
        FindConditionMapParam findCondition = (line, record) -> {
            TestRecord testRecord = JSON.parseObject(line, TestRecord.class);
            if (testRecord.getName().equals(recordTest.getName())) {
                return true;
            }
            return false;
        };
        List<String> result = tigerFileReader.readAndFindList(recordTest, findCondition);
        for (String s : result) {
            logger.info(s);
        }
    }
}
