package org.tigger.command.monitor;

import org.tigger.command.Event;

import java.util.List;
import java.util.Map;

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
