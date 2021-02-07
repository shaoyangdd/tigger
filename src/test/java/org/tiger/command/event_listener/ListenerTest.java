package org.tiger.command.event_listener;

import org.junit.Before;
import org.junit.Test;
import org.tiger.command.monitor.EventListener;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.EnableIoc;
import org.tiger.common.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;

import static org.tiger.command.Event.*;
import static org.tiger.common.util.TigerUtil.TIGER_TASK_PARAM_MAP_EXECUTE_KEY;

@EnableIoc(scanPackages = "org.tiger")
public class ListenerTest {

    private EventListener eventListener;

    @Before
    public void before() {
        BeanFactory.autowireBean();
        eventListener = BeanFactory.getBean(EventListener.class);
    }

    @Test
    public void test() {
        TigerTaskExecute tigerTaskExecute = new TigerTaskExecute();
        tigerTaskExecute.setId(101);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put(TIGER_TASK_PARAM_MAP_EXECUTE_KEY, tigerTaskExecute);
        eventListener.listen(TASK_FLOW_START, parameter);
        eventListener.listen(TASK_START, parameter);
        //模拟执行60秒
        ThreadUtil.sleep(30000);
        eventListener.listen(TASK_COMPLETE, parameter);
        eventListener.listen(TASK_FLOW_COMPLETE, null);
        ThreadUtil.sleep(30000);
    }
}
