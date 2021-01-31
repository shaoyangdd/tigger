package org.tiger.common;

public interface Constant {

    /**
     * 监听的端口号
     */
    int PORT = 9999;

    String EMPTY_STRING = "";

    String SPACE = " ";

    String IP = "ipKey";

    /**
     * 心跳间隔时间 TODO 先写死，后面看看怎么做到可配置化
     */
    long HEART_BEAT_SLEEP_TIME = 20000L;

    /**
     * 心跳间隔时间 TODO 先写死，后面看看怎么做到可配置化
     */
    long HEART_BEAT_CHECK_SLEEP_TIME = 30000L;

    int MB = 1024 * 1024;

    String THREAD_COUNT = "THREAD_COUNT";

    String COUNT_SQL = "COUNT_SQL";

    String SHARDING_PARAMETER_KEY = "shardingParameters";

}
