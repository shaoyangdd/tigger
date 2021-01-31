package org.tiger.command;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * 自动触发器
 * @author kangshaofei
 * @date 2020-01-16
 */
public class AutoTrigger {

    public static void run() {
        try {
            //创建一个scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.getContext().put("skey", "svalue");

            //创建一个Trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .usingJobData("t1", "tv1")
                    .withSchedule(cronSchedule("0 0/2 8-17 * * ?")).build();
            trigger.getJobDataMap().put("t2", "tv2");

            //创建一个job
            JobDetail job = JobBuilder.newJob(QuartzJob.class)
                    .usingJobData("j1", "jv1")
                    .withIdentity("myjob", "mygroup").build();
            job.getJobDataMap().put("j2", "jv2");

            //注册trigger并启动scheduler
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
