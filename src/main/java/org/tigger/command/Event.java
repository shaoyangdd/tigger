package org.tigger.command;

/**
 * 监视事件
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public enum Event {

    /**
     * 心跳
     */
    HEART_BEAT,

    /**
     * 任务流开始
     */
    TASK_FLOW_START,

    /**
     * 任务流结束
     */
    TASK_FLOW_COMPLETE,

    /**
     * 任务开始
     */
    TASK_START,

    /**
     * 任务结束
     */
    TASK_COMPLETE
}
