package org.tigger.common.datastruct;

import org.tigger.database.dao.entity.TigerTask;

import java.util.List;

/**
 * 逻辑任务节点
 * @author kangshaofei
 * @date 2020-01-19
 */
public class LogicTaskNode {

    /**
     * 前序一级同级节点
     */
    private List<TigerTask> previousTigerTaskList;

    /**
     * 当前节点 （必须等前序一级同级节点都执行完）
     */
    private TigerTask currentTigerTask;

    /**
     * 后序一级同级节点
     */
    private List<TigerTask> nextTigerTaskList;

    /**
     * 假节点，空节点，特殊场景使用，如在此节点暂停住
     */
    private boolean isDummyNode;

    /**
     * 下一节点
     */
    private LogicTaskNode nextNode;

    public List<TigerTask> getPreviousTigerTaskList() {
        return previousTigerTaskList;
    }

    public void setPreviousTigerTaskList(List<TigerTask> previousTigerTaskList) {
        this.previousTigerTaskList = previousTigerTaskList;
    }

    public TigerTask getCurrentTigerTask() {
        return currentTigerTask;
    }

    public void setCurrentTigerTask(TigerTask currentTigerTask) {
        this.currentTigerTask = currentTigerTask;
    }

    public List<TigerTask> getNextTigerTaskList() {
        return nextTigerTaskList;
    }

    public void setNextTigerTaskList(List<TigerTask> nextTigerTaskList) {
        this.nextTigerTaskList = nextTigerTaskList;
    }

    public LogicTaskNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(LogicTaskNode nextNode) {
        this.nextNode = nextNode;
    }

    public boolean isDummyNode() {
        return isDummyNode;
    }

    public void setDummyNode(boolean dummyNode) {
        isDummyNode = dummyNode;
    }

    @Override
    public String toString() {
        return "LogicTaskNode{" +
                "previousTigerTaskList=" + previousTigerTaskList +
                ", currentTigerTask=" + currentTigerTask +
                ", nextTigerTaskList=" + nextTigerTaskList +
                ", isDummyNode=" + isDummyNode +
                ", nextNode=" + nextNode +
                '}';
    }
}
