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
     * 配置任务执行器，使用用户自定义的，根据任务名，任务参数分发到自己的任务
     *
     * @param taskExecutor 任务执行器
     */
    public void configTaskExecutor(TaskExecutor taskExecutor) {
        ObjectFactory.instance().setTaskExecutor(taskExecutor);
    }

    /**
     * 配置数据库
     */

    /**
     * 配置 AutowireBeanParameter
     */

    /**
     * 配置标准
     * 如：任务执行时间，GC时间，CPU使用率，内存使用率，带宽使用率，磁盘IO，磁盘
     */
}
