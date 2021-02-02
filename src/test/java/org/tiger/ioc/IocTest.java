package org.tiger.ioc;

import org.junit.Test;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.common.log.TigerLogger;

import java.util.logging.Logger;

@EnableIoc(scanPackages = {"org.tiger"})
public class IocTest {

    private static Logger logger = TigerLogger.getLogger(IocTest.class.getSimpleName());

    @Test
    public void test() {
        BeanFactory.autowireBean();
    }


}
