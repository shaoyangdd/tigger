package org.tiger.ioc;

import org.junit.Test;
import org.tiger.common.datastruct.AutowireBeanParameter;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.log.TigerLogger;

import java.util.logging.Logger;

public class IocTest {

    private static Logger logger = TigerLogger.getLogger(IocTest.class.getSimpleName());

    @Test
    public void test() {
        //如果需要将日志文件写到文件系统中，需要创建一个FileHandler对象
        AutowireBeanParameter autowireBeanParameter = new AutowireBeanParameter();
        autowireBeanParameter.setPackageName("org.tiger");
        BeanFactory.autowireBean(autowireBeanParameter);
    }


}
