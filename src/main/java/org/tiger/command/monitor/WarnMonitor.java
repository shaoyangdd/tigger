package org.tiger.command.monitor;

import org.tiger.command.Event;

import java.util.Map;

/**
 * 报警监视器
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class WarnMonitor implements Monitor {

    @Override
    public void monitor(Event event, Map<String, ?> parameter) {
        //监控CPU

        //监控内存

        //监控磁盘使用率

        //监控网络

        //发邮件、短信、微信通知

    }
}
