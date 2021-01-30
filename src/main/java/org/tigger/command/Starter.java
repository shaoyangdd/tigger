package org.tigger.command;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.datastruct.LogicTaskNode;
import org.tigger.common.datastruct.TigerTask;
import org.tigger.common.datastruct.TigerTaskFlow;
import org.tigger.common.ioc.BeanFactory;
import org.tigger.common.parameter.Parameters;
import org.tigger.common.threadpool.ThreadPool;
import org.tigger.communication.client.util.NetUtil;
import org.tigger.communication.message.encoder.TigerMessageEncoder;
import org.tigger.communication.server.Server;
import org.tigger.persistence.DataPersistence;
import org.tigger.persistence.database.jdbc.ConnectionPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.tigger.command.Event.HEART_BEAT;
import static org.tigger.common.Constant.*;
import static org.tigger.communication.server.MessageType.ONLINE_NOTICE;

/**
 * tiger启动器  这里是入口
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class Starter {

    private static Logger logger = Logger.getLogger(Starter.class.getSimpleName());

    private DataPersistence<TigerTaskFlow> tigerTaskFlowDataPersistence;

    private DataPersistence<TigerTask> tigerTaskDataPersistence;

    public void run() {
        ThreadPool.getThreadPoolExecutor().execute(() -> {
            logger.info("tiger 启动开始...");

            // 初始化所有的Bean
            BeanFactory.autowireBean(Parameters.getAutowireBeanParameter());

            //1. 启动Server
            logger.info("启动Server开始...");
            new Server(PORT).run();
            logger.info("启动Server结束");

            //2. 获取并缓存局域网所有IP
            logger.info("获取并缓存局域网所有IP开始...");
            MemoryShareDataRegion.localAreaNetworkIp.addAll(NetUtil.getIPsByWindows());
            logger.info("获取并缓存局域网所有IP结束");

            //3. 获取有Tiger运行的IP
            logger.info("获取有Tiger运行的IP开始...");
            MemoryShareDataRegion.tigerRunningIpChannel.putAll(getTigerRunningIp());
            logger.info("获取有Tiger运行的IP结束" + JSON.toJSONString(MemoryShareDataRegion.tigerRunningIpChannel));

            //4. 上线通知
            logger.info("上线通知开始...");
            onlineNotice();
            logger.info("上线通知结束...");

            //5. 启动心跳
            logger.info("启动心跳");
            startHeartBeat();

            //6. 初始化数据库，建表，建立连接池 TODO 建表
            MemoryShareDataRegion.connectionPool = new ConnectionPool();

            //7. 初始化任务流图
            MemoryShareDataRegion.taskNode = this.buildLogicTaskNode();

            //8. 启动定时任务
            AutoTrigger.run();

            logger.info("tiger 启动完成");
        });
    }

    /**
     * 启动心跳
     */
    private static void startHeartBeat() {
        //每隔一段时间发一次心跳
        ThreadPool.getThreadPoolExecutor().execute(() -> {
            while (true) {
                try {
                    Thread.sleep(HEART_BEAT_SLEEP_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ObjectFactory.instance().getEventHandlerRegistry().getSendEventHandler(HEART_BEAT).sendHandle(null);
            }
        });
        //启动后台看下是否有别的执行机掉线
        ThreadPool.getThreadPoolExecutor().execute(() -> {
            while (true) {
                try {
                    Thread.sleep(HEART_BEAT_CHECK_SLEEP_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Map<String, Long> ipTime = MemoryShareDataRegion.heartBeatTime;
                for (Map.Entry<String, Long> entry : ipTime.entrySet()) {
                    if (System.currentTimeMillis() - entry.getValue() > (2 * HEART_BEAT_SLEEP_TIME)) {
                        //下线操作 TODO 这块可能有线程安全问题，后面再考虑
                        ipTime.remove(entry.getKey());
                        MemoryShareDataRegion.localAreaNetworkIp.remove(entry.getKey());
                        MemoryShareDataRegion.ipOrder.remove(entry.getKey());
                        MemoryShareDataRegion.tigerRunningIpChannel.remove(entry.getKey());
                        MemoryShareDataRegion.tigerRunningIpChannelS2C.remove(entry.getKey());
                        //TODO 如果正在运行任务，需要做故障转移
                    }
                }
            }

        });
    }

    private static void onlineNotice() {
        MemoryShareDataRegion.tigerRunningIpChannel.forEach((k, v) -> {
            v.writeAndFlush(TigerMessageEncoder.encode(ONLINE_NOTICE.getMsgType(), "上线通知"));
        });
    }

    /**
     * 获取有Tiger运行的IP\channel映射
     *
     * @return ip
     */
    private static Map<String, Channel> getTigerRunningIp() {
        //暂时使用parallelStream多线程提高效率，如果执行机规模庞大时,选择
        Map<String, Channel> map = new ConcurrentHashMap<>();
        MemoryShareDataRegion.localAreaNetworkIp.forEach(ip -> {
            //只要端口开启就认为此IP上启动着tiger
            Channel channel = ObjectFactory.instance().getClient().connect(ip, PORT);
            if (channel != null) {
                map.put(ip,channel);
            }
        });
        return map;
    }


    private LogicTaskNode buildLogicTaskNode() {
        // 伪头节点 创建伪头节点是为了保证头只有一个节点，好遍历及其它操作
        LogicTaskNode fakeHead = new LogicTaskNode();
        List<LogicTaskNode> realHeadNode = getNextTigerTaskList(EMPTY_STRING, true);
        fakeHead.setPreviousTigerTaskList(null);
        fakeHead.setCurrentTigerTask(null);
        fakeHead.setNextTigerTaskList(realHeadNode);
        fakeHead.setDummyNode(false);
        //body节点
        buildNextNode(fakeHead, realHeadNode);
        return fakeHead;
    }


    private List<LogicTaskNode> getNextTigerTaskList(String previousId, boolean isHead) {
        TigerTaskFlow tigerTaskFlowSearch = new TigerTaskFlow();
        tigerTaskFlowSearch.searchParam().put("previousTaskId", previousId);
        List<TigerTaskFlow> tigerTaskFlowList = tigerTaskFlowDataPersistence.findList(tigerTaskFlowSearch);
        if (tigerTaskFlowList.size() == 0 && isHead) {
            throw new RuntimeException("没有定义任务流");
        }
        List<LogicTaskNode> nodeList = new ArrayList<>();
        for (TigerTaskFlow tigerTaskFlow : tigerTaskFlowList) {
            LogicTaskNode logicTaskNode = new LogicTaskNode();
            //先填上task
            TigerTask search = new TigerTask();
            search.setTaskName(tigerTaskFlow.getTaskName());
            logicTaskNode.setCurrentTigerTask(tigerTaskDataPersistence.findOne(search));
            nodeList.add(logicTaskNode);
        }
        return nodeList;
    }

    private void buildNextNode(LogicTaskNode previousNode, List<LogicTaskNode> nextNodeList) {

        List<LogicTaskNode> previousNodeList = new ArrayList<>(1);
        previousNodeList.add(previousNode);

        for (LogicTaskNode nextNode : nextNodeList) {
            List<LogicTaskNode> nextTigerTaskList2 = getNextTigerTaskList(String.valueOf(nextNode.getCurrentTigerTask().getId()), false);
            LogicTaskNode node = new LogicTaskNode();
            //再把节点的前后补上
            node.setPreviousTigerTaskList(previousNodeList);
            node.setNextTigerTaskList(nextTigerTaskList2);
            node.setDummyNode(false);
            //递归构建
            buildNextNode(node, nextTigerTaskList2);
        }
    }
}
