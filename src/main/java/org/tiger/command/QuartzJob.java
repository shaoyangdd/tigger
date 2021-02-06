package org.tiger.command;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.command.monitor.EventListener;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.ioc.BeanFactory;

import static org.tiger.command.Event.TASK_FLOW_COMPLETE;
import static org.tiger.command.Event.TASK_FLOW_START;

/**
 * tiger定时启动器
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class QuartzJob implements Job {

    private Logger logger = LoggerFactory.getLogger(QuartzJob.class.getSimpleName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("启动定时任务");
        EventListener eventListener = BeanFactory.getBean(EventListener.class);
        eventListener.listen(TASK_FLOW_START, null);
        BeanFactory.getBean(TaskFlowScheduler.class).iterateAndExecute(MemoryShareDataRegion.taskNode);
        eventListener.listen(TASK_FLOW_COMPLETE, null);
        logger.info("定时任务结束");
    }
}