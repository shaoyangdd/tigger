package org.tiger.command.machine_learning;

/**
 * 机器学习，动态计算分片参数
 *
 * @author kangshaofei
 * @date 2020-01-26
 */
public class ShardingParameter {

    public long fromId;

    public long toId;

    private int threadCount;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}
