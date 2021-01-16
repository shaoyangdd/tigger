package org.tigger.command;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.tigger.common.Constant;
import org.tigger.common.MemoryShareDataRegion;
import org.tigger.common.ObjectFactory;
import org.tigger.communication.client.Client;
import org.tigger.communication.client.util.NetUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * tigger启动要做的事
 * @author kangshaofei
 * @date 2020-01-16
 */
public class Starter {

    private static Logger logger = Logger.getLogger(Starter.class.getSimpleName());

    public static void run() {

        logger.info("tiger 启动开始...");

        //1. 获取并缓存局域网所有IP
        logger.info("获取并缓存局域网所有IP开始...");
        MemoryShareDataRegion.localAreaNetworkIp.addAll(NetUtil.getIPsByWindows());

        //2. 获取有Tiger运行的IP
        logger.info("获取有Tiger运行的IP开始...");
        MemoryShareDataRegion.tigerRunningIpChannel.putAll(getTigerRunningIp());
        logger.info("获取有Tiger运行的IP结束" + JSON.toJSONString(MemoryShareDataRegion.tigerRunningIpChannel));

        //2. 上线通知



        logger.info("tiger 启动完成");
    }

    /**
     * 获取有Tiger运行的IP\channel映射
     * @return ip
     */
    private static Map<String, Channel> getTigerRunningIp() {
        //暂时使用parallelStream多线程提高效率，如果执行机规模庞大时,选择
        Map<String,Channel> map = new ConcurrentHashMap<>();
        MemoryShareDataRegion.localAreaNetworkIp.forEach(ip->{
            //只要端口开启就认为此IP上启动着tiger
            Channel channel = ObjectFactory.getClient().connect(ip, Constant.PORT);
            if (channel != null) {
                map.put(ip,channel);
            }
        });
        return map;
    }
}
