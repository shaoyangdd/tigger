package org.tigger.command;

import java.util.Map;

public interface TaskExecutor {

    boolean execute(String taskName, String parameter);

}
