package org.tigger.common;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 内存共享数据区
 * 缓存所有的共享数据
 * @author kangshaofei
 * @date 2020-01-16
 */
public class MemoryShareDataRegion {

    /**
     * 局域网所有IP
     */
    public static List<String> localAreaNetworkIp = new ArrayList<>();

    /**
     * 局域网运行的IP和Channel映射
     */
    public static Map<String, Channel> tigerRunningIpChannel = new ConcurrentHashMap<>();

    /**
     * 运行状态
     */
}
