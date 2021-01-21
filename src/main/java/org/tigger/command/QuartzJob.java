package org.tigger.command;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;

public class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        ObjectFactory.instance().getTaskFlowScheduler().iterateAndExecute(MemoryShareDataRegion.taskNode);
    }
}
