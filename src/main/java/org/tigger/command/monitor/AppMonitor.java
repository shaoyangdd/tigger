package org.tigger.command.monitor;

import java.util.Map;

/**
 * 应用监视器
 * @author kangshaofei
 * @date 2020-01-16
 */
public class AppMonitor implements Monitor {

    /**
     * 监视
     * @param event     事件
     * @param parameter 参数
     */
    @Override
    public void monitor(Event event, Map<String, Object> parameter) {
        //TODO 监视进度，实时展示在控制台。任务流图进度，权重计算
    }
}
