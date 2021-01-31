package org.tiger.command;

/**
 * 任务执行器
 * 根据taskName、parameter调用自己的业务逻辑
 * @author kangshaofei
 * @date 2020-01-19
 */
public interface TaskExecutor {

    boolean execute(String taskName, String parameter);

}
