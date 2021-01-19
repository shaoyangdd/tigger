package org.tigger.command;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.tigger.common.ObjectFactory;

import static org.tigger.common.Constant.EMPTY_STRING;

public class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        ObjectFactory.instance().getTaskFlowScheduler().execute(EMPTY_STRING);
    }
}
