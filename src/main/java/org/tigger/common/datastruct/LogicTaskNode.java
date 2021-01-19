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
    private List<TigerTask> previousNodes;

    /**
     * 当前节点 （必须等前序一级同级节点都执行完）
     */
    private TigerTask currentNode;

    /**
     * 后序一级同级节点
     */
    private List<TigerTask> nextNodes;

    /**
     * 假节点，空节点，特殊场景使用，如在此节点暂停住
     */
    private boolean isDummyNode;

    public List<TigerTask> getPreviousNodes() {
        return previousNodes;
    }

    public void setPreviousNodes(List<TigerTask> previousNodes) {
        this.previousNodes = previousNodes;
    }

    public TigerTask getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(TigerTask currentNode) {
        this.currentNode = currentNode;
    }

    public List<TigerTask> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(List<TigerTask> nextNodes) {
        this.nextNodes = nextNodes;
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
                "previousNodes=" + previousNodes +
                ", currentNode=" + currentNode +
                ", nextNodes=" + nextNodes +
                ", isDummyNode=" + isDummyNode +
                '}';
    }
}
