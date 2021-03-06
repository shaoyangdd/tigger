package org.tiger.command.monitor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.command.Event;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.util.TigerUtil;
import org.tiger.communication.message.encoder.TigerMessageEncoder;
import org.tiger.consensus.Consensus;

import java.util.Map;

import static org.tiger.communication.server.MessageType.TASK_START;

/**
 * 应用监视器
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
@SingletonBean
public class AppMonitor implements Monitor {

    private final Logger logger = LoggerFactory.getLogger(AppMonitor.class.getSimpleName());

    @Inject
    private Consensus consensus;

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
     * 任务流开始
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
        TigerTask tigerTask = (TigerTask) parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_KEY);
        //是否可以开始
        if (!consensus.canStartTask(tigerTask)) {
            throw new RuntimeException("任务无法开始:" + tigerTask.getTaskName());
        }
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
