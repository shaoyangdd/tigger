package org.tiger.command;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ObjectFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.LogicTaskNode;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskFlow;
import org.tiger.common.ioc.BeanFactory;
import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.InjectCustomBean;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.thread.ThreadPool;
import org.tiger.common.util.CollectionUtil;
import org.tiger.common.util.NetUtil;
import org.tiger.common.util.ThreadUtil;
import org.tiger.communication.client.Client;
import org.tiger.communication.message.encoder.TigerMessageEncoder;
import org.tiger.communication.server.Server;
import org.tiger.persistence.database.jdbc.ConnectionPool;
import org.tiger.persistence.file.FileDataPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.tiger.command.Event.HEART_BEAT;
import static org.tiger.common.Constant.*;
import static org.tiger.communication.server.MessageType.ONLINE_NOTICE;

/**
 * tiger启动器  这里是入口
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
@SingletonBean
public class Starter {

    private static Logger logger = LoggerFactory.getLogger(Starter.class.getSimpleName());

    @Inject
    private AutoTrigger autoTrigger;

    @InjectCustomBean
    private FileDataPersistence<TigerTaskFlow> tigerTaskFlowDataPersistence;

    @InjectCustomBean
    private FileDataPersistence<TigerTask> tigerTaskDataPersistence;

    @Inject
    private Server server;

    public void run() {
        ThreadPool.getThreadPoolExecutor().execute(() -> {
            logger.info("tiger 启动开始...");

            // 初始化所有的Bean
            BeanFactory.autowireBean();

            //1. 启动Server
            logger.info("启动Server开始...");
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                server.run();
            });
            logger.info("启动Server结束");

            //2. 获取并缓存局域网所有IP
            logger.info("获取并缓存局域网所有IP开始...");
            MemoryShareDataRegion.localAreaNetworkIp.addAll(NetUtil.getIPsByWindows());
            logger.info("获取并缓存局域网所有IP结束");

            //3. 获取有Tiger运行的IP
            logger.info("获取有Tiger运行的IP开始...");
            MemoryShareDataRegion.tigerRunningIpChannel.putAll(getTigerRunningIp());
            MemoryShareDataRegion.ipOrder.add(MemoryShareDataRegion.localIp);
            MemoryShareDataRegion.ipOrder.addAll(MemoryShareDataRegion.tigerRunningIpChannel.keySet());
            logger.info("获取有Tiger运行的IP结束" + JSON.toJSONString(MemoryShareDataRegion.tigerRunningIpChannel.keySet()));

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
            autoTrigger.run();

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
     * @return Map<ip, Channel>
     */
    private static Map<String, Channel> getTigerRunningIp() {
        //暂时使用parallelStream多线程提高效率，如果执行机规模庞大时,选择
        Map<String, Channel> map = new ConcurrentHashMap<>();
        MemoryShareDataRegion.localAreaNetworkIp.forEach(ip -> {
            //只要端口开启就认为此IP上启动着tiger
            Client client = ObjectFactory.instance().getClient();
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                client.connect(ip, PORT);
            });
            //TODO 比较LOW，后面改一下
            ThreadUtil.sleep(1000);
            Channel channel = client.getChannel();
            if (channel != null) {
                map.put(ip, channel);
            }
        });
        return map;
    }


    public LogicTaskNode buildLogicTaskNode() {
        // 伪头节点 创建伪头节点是为了保证头只有一个节点，好遍历及其它操作
        LogicTaskNode fakeHead = new LogicTaskNode();
        List<LogicTaskNode> realHeadNode = getNextTigerTaskNodeList(EMPTY_STRING, true);
        fakeHead.setPreviousTigerTaskList(null);
        fakeHead.setCurrentTigerTask(null);
        fakeHead.setNextTigerTaskList(realHeadNode);
        fakeHead.setDummyNode(false);
        //body节点
        buildNextNode(fakeHead, realHeadNode);
        return fakeHead;
    }


    private List<LogicTaskNode> getNextTigerTaskNodeList(String previousId, boolean isHead) {
        TigerTaskFlow tigerTaskFlowSearch = new TigerTaskFlow();
        tigerTaskFlowSearch.searchParam().put("previousTaskId", previousId);
        //包含关系，而不是相等，逗号分割所以这样
        tigerTaskFlowSearch.searchParam().put("contain", true);
        logger.info("getNextTigerTaskNodeList:======={},{}", previousId, isHead);
        List<TigerTaskFlow> tigerTaskFlowList = tigerTaskFlowDataPersistence.findList(tigerTaskFlowSearch);
        if (tigerTaskFlowList.size() == 0) {
            if (isHead) {
                throw new RuntimeException("没有定义任务流");
            } else {
                return null;
            }
        }
        List<LogicTaskNode> nodeList = new ArrayList<>();
        for (TigerTaskFlow tigerTaskFlow : tigerTaskFlowList) {
            logger.info("next:{}", JSON.toJSONString(tigerTaskFlow));
            LogicTaskNode logicTaskNode = new LogicTaskNode();
            //先填上task
            TigerTask search = new TigerTask();
            search.setTaskName(tigerTaskFlow.getTaskName());
            logicTaskNode.setCurrentTigerTask(tigerTaskDataPersistence.findOne(search));
            nodeList.add(logicTaskNode);
        }
        return nodeList;
    }

    /**
     * 构建下一个节点
     *
     * @param currentNode  当前节点
     * @param nextNodeList 下面节点
     */
    private void buildNextNode(LogicTaskNode currentNode, List<LogicTaskNode> nextNodeList) {
        //构建当前节点
        TigerTask tigerTask = currentNode.getCurrentTigerTask();
        logger.info("构建:{}节点", tigerTask == null ? "" : tigerTask.getTaskName());
        currentNode.setNextTigerTaskList(nextNodeList);
        currentNode.setDummyNode(false);
        //构建后面的节点
        List<LogicTaskNode> currentNodeList = new ArrayList<>(1);
        currentNodeList.add(currentNode);
        for (LogicTaskNode nextNode : nextNodeList) {
            nextNode.setPreviousTigerTaskList(currentNodeList);
            List<LogicTaskNode> nextNextTigerTaskList = getNextTigerTaskNodeList(String.valueOf(nextNode.getCurrentTigerTask().getId()), false);
            nextNode.setNextTigerTaskList(nextNextTigerTaskList);
            nextNode.setDummyNode(false);
            //递归构建
            if (CollectionUtil.isNotEmpty(nextNextTigerTaskList)) {
                buildNextNode(nextNode, nextNextTigerTaskList);
            }
        }
    }
}
