package org.example;


import org.tigger.command.TaskExecutor;

public class SpringBatchTaskExecutor implements TaskExecutor {
    @Override
    public boolean execute(String taskName, String parameter) {
        return false;
    }
}
