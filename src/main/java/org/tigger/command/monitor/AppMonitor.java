package org.tigger.command.monitor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigger.command.Event;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.util.TigerUtil;
import org.tigger.communication.message.encoder.TigerMessageEncoder;

import java.util.Map;

import static org.tigger.communication.server.MessageType.TASK_START;

/**
 * 应用监视器
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class AppMonitor implements Monitor {

    private final Logger logger = LoggerFactory.getLogger(AppMonitor.class.getSimpleName());

    /**
     * 监视
     *
     * @param event     事件
     * @param parameter 参数
     */
    @Override
    public void monitor(Event event, Map<String, ?> parameter) {
        //TODO 监视进度，实时展示在控制台。任务流图进度，权重计算
        switch (event) {
            case TASK_FLOW_START:
                taskFlowStart(parameter);
                break;
            case TASK_START:
                taskStart(parameter);
                break;
            case TASK_COMPLETE:
                taskComplete(parameter);
                break;
            case TASK_FLOW_COMPLETE:
                taskFlowComplete(parameter);
                break;
            default:
                logger.info("非法 event!" + event);
        }
    }

    /**
     * 任务流结束
     *
     * @param parameter 参数
     */
    private void taskFlowStart(Map<String, ?> parameter) {

    }

    /**
     * 任务流结束
     *
     * @param parameter 参数
     */
    private void taskFlowComplete(Map<String, ?> parameter) {
        //初始化运行状态
        MemoryShareDataRegion.taskExecuteStatus.clear();
    }

    /**
     * 任务开始
     *
     * @param parameter 开始参数
     */
    private void taskStart(Map<String, ?> parameter) {
        MemoryShareDataRegion.tigerRunningIpChannel.forEach((k, v) -> {
            String message = JSON.toJSONString(parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_KEY));
            logger.info("任务开始通知:" + v.remoteAddress().toString() + ", 消息:" + "");
            TigerMessageEncoder.encode(TASK_START.getMsgType(), message);
        });
    }

    /**
     * 任务结束
     *
     * @param parameter 结束参数
     */
    private void taskComplete(Map<String, ?> parameter) {
        MemoryShareDataRegion.tigerRunningIpChannel.forEach((k, v) -> {
            String message = JSON.toJSONString(parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_KEY));
            logger.info("任务结束通知:" + v.remoteAddress().toString() + ", 消息:" + "");
            TigerMessageEncoder.encode(TASK_START.getMsgType(), message);
        });
    }
}
