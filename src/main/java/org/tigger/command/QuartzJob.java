package org.tigger.command;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import static org.tigger.common.Constant.EMPTY_STRING;

public class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        TaskFlowScheduler.execute(EMPTY_STRING);
    }
}
