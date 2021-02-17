package org.tiger.consensus;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TaskExecuteStatus;
import org.tiger.common.datastruct.TaskStatus;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.thread.ThreadManager;
import org.tiger.common.thread.ThreadPool;
import org.tiger.common.util.CollectionUtil;
import org.tiger.communication.message.encoder.TigerMessageEncoder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.tiger.communication.server.MessageType.TASK_START;

/**
 * 共识层
 *
 * @author kangshaofei
 * @date 2021-02-16
 */
@SingletonBean
public class Consensus {

    @InjectParameter
    private String taskStartConsensusTimeout;

    private final Logger logger = LoggerFactory.getLogger(Consensus.class);

    /**
     * 多个执行节点达成可以启动任务的共识
     *
     * @return true:可以启动；false: 不可以启动
     */
    public boolean canStartTask(TigerTask tigerTask) {
        //通知其它执行节点我可以开始了
        MemoryShareDataRegion.tigerRunningIpChannel.forEach((k, v) -> {
            String message = JSON.toJSONString(tigerTask);
            logger.info("任务开始通知:" + v.remoteAddress().toString() + ", 消息:" + "");
            TigerMessageEncoder.encode(TASK_START.getMsgType(), message);
        });

        //收到其它执行节点的通知后就启动（此时可能因为网络原因收不到某个执行节点的，这时候怎么办？这种概率不大。任务结束的时候再兜底吧，停下还是怎么着）
        //启动一个守护线程来观察是不是所有执行节点都已就绪
        Set<String> notReadyIpList = new HashSet<>(MemoryShareDataRegion.tigerRunningIpChannel.keySet());
        ThreadPool.getThreadPoolExecutor().execute(() -> {
            while (true) {
                Iterator<String> iterator = notReadyIpList.iterator();
                while (iterator.hasNext()) {
                    String ip = iterator.next();
                    List<TaskExecuteStatus> taskExecuteStatuses = MemoryShareDataRegion.taskExecuteStatus.stream()
                            .filter(i -> ip.equals(i.getIp()) && i.getTaskStatus() == TaskStatus.RUNNABLE)
                            .collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(taskExecuteStatuses)) {
                        logger.info("ip:{}已就绪", taskExecuteStatuses.get(0).getTigerTask().getTaskName());
                        notReadyIpList.remove(ip);
                    }
                }
                if (CollectionUtil.isEmpty(notReadyIpList)) {
                    //未就绪的个数为0时，全部就绪，执行下面的任务
                    synchronized (this) {
                        ThreadManager.TASK_START_LOCK.notify();
                    }
                    break;
                } else {
                    logger.info("还有IP没有就绪:{}", JSON.toJSONString(notReadyIpList));
                }
            }
        });
        //N分钟内收不到其它节点的通知就返回失败
        try {
            ThreadManager.TASK_START_LOCK.wait(Long.parseLong(taskStartConsensusTimeout));
            if (!CollectionUtil.isEmpty(notReadyIpList)) {
                //没法判断是超时还是通知后的
                return false;
            }
        } catch (Exception e) {
            logger.error("多个执行节点达成可以启动任务的共识失败", e);
            return false;
        }
        return true;
    }
}
