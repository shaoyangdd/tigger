package org.tigger.command.monitor;

import java.util.Map;

public interface Monitor {

    void monit(Event event, Map<String,Object> parameter);

}
