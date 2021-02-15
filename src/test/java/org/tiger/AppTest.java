package org.tiger;

import org.junit.Before;
import org.junit.Test;
import org.tiger.command.Starter;
import org.tiger.common.ObjectFactory;
import org.tiger.common.config.TigerConfiguration;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.common.util.ThreadUtil;

import static org.junit.Assert.assertTrue;

/**
 * 全流程测试
 */
@EnableIoc(scanPackages = "org.tiger")
public class AppTest {

    private Starter starter;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        starter = BeanFactory.getBean(Starter.class);
    }

    @Test
    public void execute() {
        TigerConfiguration tigerConfiguration = ObjectFactory.instance().getTigerConfiguration();
        tigerConfiguration.configTaskExecutor(new SpringBatchTaskExecutor());
        starter.run();
        ThreadUtil.sleep(1000 * 60 * 15);
        assertTrue(true);
    }
}
