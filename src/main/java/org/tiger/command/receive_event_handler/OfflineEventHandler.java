package org.tiger.command.receive_event_handler;

import org.tiger.common.ioc.SingletonBean;

import java.util.Map;

@SingletonBean
public class OfflineEventHandler implements EventHandler {

    @Override
    public void handle(Map<String, ?> parameter) {

    }
}
