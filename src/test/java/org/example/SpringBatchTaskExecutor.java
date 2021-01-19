package org.example;


import org.tigger.command.TaskExecutor;
import org.tigger.common.cache.MemoryShareDataRegion;

public class SpringBatchTaskExecutor implements TaskExecutor {
    @Override
    public boolean execute(String taskName, String parameter) {
        System.out.println("本IP:" + MemoryShareDataRegion.ipOrder + "执行:" +taskName + "任务,参数:" + parameter);
        return 4/2 == 2;
    }
}
