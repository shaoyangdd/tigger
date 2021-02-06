package org.tiger.command.monitor;

import org.tiger.command.Event;
import org.tiger.common.ioc.AfterInstance;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.SingletonBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 事件监听器
 *
 * @author 康绍飞
 * @date 2021-02-01
 */
@SingletonBean
public class EventListener {

    private List<Monitor> monitorList;

    public EventListener() {
    }

    public void listen(Event event, Map<String, ?> parameters) {
        for (Monitor monitor : monitorList) {
            monitor.monitor(event, parameters);
        }
    }

    @AfterInstance
    public void init() {
        monitorList = new ArrayList<>();
        monitorList.add(BeanFactory.getBean(AppMonitor.class));
        monitorList.add(BeanFactory.getBean(JvmMonitor.class));
        monitorList.add(BeanFactory.getBean(SystemMonitor.class));
        monitorList.add(BeanFactory.getBean(WarnMonitor.class));
    }
}
