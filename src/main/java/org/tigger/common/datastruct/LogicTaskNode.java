package org.tigger.common.datastruct;

import org.tigger.persistence.database.dao.entity.TigerTask;

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
    private List<LogicTaskNode> previousTigerTaskList;

    /**
     * 当前节点 （必须等前序一级同级节点都执行完）
     */
    private TigerTask currentTigerTask;

    /**
     * 后序一级同级节点
     */
    private List<LogicTaskNode> nextTigerTaskList;

    /**
     * 假节点，空节点，特殊场景使用，如在此节点暂停住
     */
    private boolean isDummyNode;

    public List<LogicTaskNode> getPreviousTigerTaskList() {
        return previousTigerTaskList;
    }

    public void setPreviousTigerTaskList(List<LogicTaskNode> previousTigerTaskList) {
        this.previousTigerTaskList = previousTigerTaskList;
    }

    public TigerTask getCurrentTigerTask() {
        return currentTigerTask;
    }

    public void setCurrentTigerTask(TigerTask currentTigerTask) {
        this.currentTigerTask = currentTigerTask;
    }

    public List<LogicTaskNode> getNextTigerTaskList() {
        return nextTigerTaskList;
    }

    public void setNextTigerTaskList(List<LogicTaskNode> nextTigerTaskList) {
        this.nextTigerTaskList = nextTigerTaskList;
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
                '}';
    }
}
