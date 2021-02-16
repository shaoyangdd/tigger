package org.tiger.command.quartz;


import org.junit.Before;
import org.junit.Test;
import org.tiger.command.AutoTrigger;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.common.util.ThreadUtil;

/**
 * 定时调度
 *
 * @author 康绍飞
 * @date 2021-02-06
 */
@EnableIoc(scanPackages = "org.tiger")
public class QuartzTest {

    private AutoTrigger autoTrigger;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        autoTrigger = BeanFactory.getBean(AutoTrigger.class);
    }

    @Test
    public void test() {
        new Thread(() -> {
            autoTrigger.run();
        }).start();
        ThreadUtil.sleep(1000 * 60 * 2);
    }
}
