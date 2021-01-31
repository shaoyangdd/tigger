package org.tiger.command;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.tiger.common.ObjectFactory;
import org.tiger.common.cache.MemoryShareDataRegion;

import java.util.logging.Logger;

import static org.tiger.command.Event.TASK_FLOW_COMPLETE;
import static org.tiger.command.Event.TASK_FLOW_START;

/**
 * tiger启动器  这里是入口
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class QuartzJob implements Job {

    private Logger logger = Logger.getLogger(QuartzJob.class.getSimpleName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("启动定时任务");
        ObjectFactory.instance().getEventListener().listen(TASK_FLOW_START, null);
        ObjectFactory.instance().getTaskFlowScheduler().iterateAndExecute(MemoryShareDataRegion.taskNode);
        ObjectFactory.instance().getEventListener().listen(TASK_FLOW_COMPLETE, null);
        logger.info("定时任务结束");
    }
}
