package org.tigger.command.monitor;

import java.util.List;
import java.util.Map;

public class EventListener {

    private List<Monitor> monitorList;

    public EventListener(List<Monitor> monitorList) {
        this.monitorList = monitorList;
    }

    public void listen(Event event, Map<String,Object> parameters) {
        for (Monitor monitor : monitorList) {
            monitor.monit(event, parameters);
        }
    }
}
