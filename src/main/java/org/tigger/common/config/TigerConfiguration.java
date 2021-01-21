package org.tigger.common.config;

import org.tigger.command.TaskExecutor;
import org.tigger.common.ObjectFactory;
import org.tigger.common.datastruct.DBMode;

/**
 * tiger配置类
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class TigerConfiguration {

    private DBMode dbMode = DBMode.DB;

    /**
     * 配置任务执行器，使用用户自定义的
     * @param taskExecutor 任务执行器
     */
    public void configTaskExecutor(TaskExecutor taskExecutor) {
        ObjectFactory.instance().setTaskExecutor(taskExecutor);
    }

    /**
     * 配置数据库
     */
}
