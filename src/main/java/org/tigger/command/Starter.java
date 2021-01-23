package org.tigger.command;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.datastruct.LogicTaskNode;
import org.tigger.communication.client.util.NetUtil;
import org.tigger.communication.message.encoder.TigerMessageEncoder;
import org.tigger.communication.server.Server;
import org.tigger.database.dao.TigerTaskDao;
import org.tigger.database.dao.TigerTaskFlowDao;
import org.tigger.database.dao.entity.TigerTaskFlow;
import org.tigger.database.jdbc.ConnectionPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.tigger.common.Constant.EMPTY_STRING;
import static org.tigger.common.Constant.PORT;
import static org.tigger.communication.server.MessageType.ONLINE_NOTICE;

/**
 * tiger启动器  这里是入口
 * @author kangshaofei
 * @date 2020-01-16
 */
public class Starter {

    private static Logger logger = Logger.getLogger(Starter.class.getSimpleName());

    public static void run() {

        logger.info("tiger 启动开始...");

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

        //5. 初始化数据库，建表，建立连接池 TODO 建表
        MemoryShareDataRegion.connectionPool = new ConnectionPool();

        //6. 初始化任务流图
        MemoryShareDataRegion.taskNode = buildLogicTaskNode();

        //7. 启动定时任务
        AutoTrigger.run();

        //TODO 启动心跳


        logger.info("tiger 启动完成");
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



    private static LogicTaskNode buildLogicTaskNode() {
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


    private static List<LogicTaskNode> getNextTigerTaskList(String previousId, boolean isHead) {
        List<TigerTaskFlow> tigerTaskFlowList = TigerTaskFlowDao.getTigerTaskFlowByPreviousId(previousId);
        if (tigerTaskFlowList.size() == 0 && isHead) {
            throw new RuntimeException("没有定义任务流");
        }
        List<LogicTaskNode> nodeList = new ArrayList<>();
        for (TigerTaskFlow tigerTaskFlow : tigerTaskFlowList) {
            LogicTaskNode logicTaskNode = new LogicTaskNode();
            //先填上task
            logicTaskNode.setCurrentTigerTask(TigerTaskDao.getTigerTaskByName(tigerTaskFlow.getTaskName()));
            nodeList.add(logicTaskNode);
        }
        return nodeList;
    }

    private static void buildNextNode(LogicTaskNode previousNode, List<LogicTaskNode> nextNodeList) {

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
