package org.tigger.command.send_event_handler;

import java.util.Map;

public interface SendEventHandler {

    void sendHandle(Map<String, ?> parameter);

}
