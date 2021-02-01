package org.tiger.command.monitor;

import org.tiger.command.Event;
import org.tiger.common.ioc.SingletonBean;

import java.util.List;
import java.util.Map;

@SingletonBean
public class EventListener {

    private List<Monitor> monitorList;

    public EventListener(List<Monitor> monitorList) {
        this.monitorList = monitorList;
    }

    public void listen(Event event, Map<String, ?> parameters) {
        for (Monitor monitor : monitorList) {
            monitor.monitor(event, parameters);
        }
    }
}