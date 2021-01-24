package org.tigger.common.cache;

import io.netty.channel.Channel;
import org.tigger.common.datastruct.LogicTaskNode;
import org.tigger.common.datastruct.TaskExecuteStatus;
import org.tigger.persistence.database.jdbc.ConnectionPool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存共享数据区
 * 缓存所有的共享数据
 * @author kangshaofei
 * @date 2020-01-16
 */
public class MemoryShareDataRegion {

    public static ConnectionPool connectionPool = null;

    /**
     * 本机IP
     */
    public static String localIp;

    static {
        try {
            localIp = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 局域网所有IP
     */
    public static List<String> localAreaNetworkIp = new ArrayList<>();

    /**
     * 局域网运行的IP顺序列表，包括本机IP
     */
    public static List<String> ipOrder = new ArrayList<>();

    /**
     * 心跳最新时间 key :ip,value: 时间戳
     */
    public static Map<String, Long> heartBeatTime = new ConcurrentHashMap<>();

    /**
     * 局域网运行的IP和Channel映射  client->server
     */
    public static Map<String, Channel> tigerRunningIpChannel = new ConcurrentHashMap<>();

    /**
     * 局域网运行的IP和Channel映射  server->client
     */
    public static Map<String, Channel> tigerRunningIpChannelS2C = new ConcurrentHashMap<>();

    /**
     * 任务流图 目前只支持一个图
     */
    public static LogicTaskNode taskNode;

    /**
     * 运行状态
     */
    public static List<TaskExecuteStatus> taskExecuteStatus = new ArrayList<>();
}
