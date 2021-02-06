package org.tiger.common.datastruct;

/**
 * JVM信息
 *
 * @author kangshaofei
 * @date 2021-01-29
 */
public class JvmInfo extends AbstractRecord {


    /**
     * 初始化堆大小 MB
     */
    private long initHeapSize;

    /**
     * 最大堆大小 MB
     */
    private long maxHeapSize;

    /**
     * 已使用堆大小 MB
     */
    private long usedHeapSize;

    /**
     * 初始化非堆大小 MB
     */
    private long initNonHeapSize;

    /**
     * 最大非堆大小 MB
     */
    private long maxNonHeapSize;

    /**
     * 已使用非堆大小 MB
     */
    private long usedNonHeapSize;

    /**
     * 线程总数
     */
    private long threadTotalCount;

    /**
     * 高峰线程数
     */
    private long peakThreadCount;

    /**
     * 守护线程总数
     */
    private long demonThreadCount;

    /**
     * 当前线程CPU使用时间
     */
    private long threadCpuTime;

    /**
     * 当前线程用户使用时间
     */
    private long threadUserTime;

    /**
     * JVM当前占用的内存总量 MB
     */
    private long jvmCurrentSize;

    /**
     * 虚拟机中的空闲内存量 MB
     */
    private long jvmFreeSize;

    /**
     * 虚拟机最大内存量,最多可以向内存申请多少 MB
     */
    private long jvmMaxSize;

    public long getInitHeapSize() {
        return initHeapSize;
    }

    public void setInitHeapSize(long initHeapSize) {
        this.initHeapSize = initHeapSize;
    }

    public long getMaxHeapSize() {
        return maxHeapSize;
    }

    public void setMaxHeapSize(long maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
    }

    public long getUsedHeapSize() {
        return usedHeapSize;
    }

    public void setUsedHeapSize(long usedHeapSize) {
        this.usedHeapSize = usedHeapSize;
    }

    public long getInitNonHeapSize() {
        return initNonHeapSize;
    }

    public void setInitNonHeapSize(long initNonHeapSize) {
        this.initNonHeapSize = initNonHeapSize;
    }

    public long getMaxNonHeapSize() {
        return maxNonHeapSize;
    }

    public void setMaxNonHeapSize(long maxNonHeapSize) {
        this.maxNonHeapSize = maxNonHeapSize;
    }

    public long getUsedNonHeapSize() {
        return usedNonHeapSize;
    }

    public void setUsedNonHeapSize(long usedNonHeapSize) {
        this.usedNonHeapSize = usedNonHeapSize;
    }

    public long getThreadTotalCount() {
        return threadTotalCount;
    }

    public void setThreadTotalCount(long threadTotalCount) {
        this.threadTotalCount = threadTotalCount;
    }

    public long getPeakThreadCount() {
        return peakThreadCount;
    }

    public void setPeakThreadCount(long peakThreadCount) {
        this.peakThreadCount = peakThreadCount;
    }

    public long getDemonThreadCount() {
        return demonThreadCount;
    }

    public void setDemonThreadCount(long demonThreadCount) {
        this.demonThreadCount = demonThreadCount;
    }

    public long getThreadCpuTime() {
        return threadCpuTime;
    }

    public void setThreadCpuTime(long threadCpuTime) {
        this.threadCpuTime = threadCpuTime;
    }

    public long getThreadUserTime() {
        return threadUserTime;
    }

    public void setThreadUserTime(long threadUserTime) {
        this.threadUserTime = threadUserTime;
    }

    public long getJvmCurrentSize() {
        return jvmCurrentSize;
    }

    public void setJvmCurrentSize(long jvmCurrentSize) {
        this.jvmCurrentSize = jvmCurrentSize;
    }

    public long getJvmFreeSize() {
        return jvmFreeSize;
    }

    public void setJvmFreeSize(long jvmFreeSize) {
        this.jvmFreeSize = jvmFreeSize;
    }

    public long getJvmMaxSize() {
        return jvmMaxSize;
    }

    public void setJvmMaxSize(long jvmMaxSize) {
        this.jvmMaxSize = jvmMaxSize;
    }

    @Override
    public void setId(long id) {

    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getUnionKey() {
        return null;
    }
}
