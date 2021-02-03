package org.tiger.ioc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;

@EnableIoc(scanPackages = {"org.tiger"})
public class IocTest {

    private static Logger logger = LoggerFactory.getLogger(IocTest.class.getSimpleName());

    @Test
    public void test() {
        BeanFactory.autowireBean();
    }


}
