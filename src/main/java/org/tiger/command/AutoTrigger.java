package org.tiger.command;

import com.alibaba.fastjson.JSON;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.tiger.common.datastruct.TaskSchedule;
import org.tiger.common.ioc.InjectCustomBean;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.file.FileDataPersistence;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * 自动触发器
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
@SingletonBean
public class AutoTrigger {

    @InjectCustomBean
    private FileDataPersistence<TaskSchedule> taskScheduleFileDataPersistence;

    public void run() {

        List<TaskSchedule> taskScheduleList = taskScheduleFileDataPersistence.findList(null);

        for (TaskSchedule taskSchedule : taskScheduleList) {
            try {
                String data = JSON.toJSONString(taskSchedule);

                //创建一个scheduler
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.getContext().put("taskSchedule", data);

                //创建一个Trigger
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger1", "group1")
                        .usingJobData("data", data)
                        .withSchedule(cronSchedule(taskSchedule.getCron())).build();
                trigger.getJobDataMap().put("parameter", taskSchedule.getParameter());

                //创建一个job
                JobDetail job = JobBuilder.newJob(QuartzJob.class)
                        .usingJobData("parameter", taskSchedule.getParameter())
                        .withIdentity(taskSchedule.getId() + "", taskSchedule.getId() + "").build();
                job.getJobDataMap().put("test", "111");

                //注册trigger并启动scheduler
                scheduler.scheduleJob(job, trigger);
                scheduler.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
