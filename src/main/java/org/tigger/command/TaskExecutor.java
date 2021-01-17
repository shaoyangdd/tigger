package org.tigger.command;

public interface TaskExecutor {

    boolean execute(String taskName, String parameter);

}
