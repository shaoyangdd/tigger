package org.tigger.common.datastruct;

import java.util.List;

/**
 * 工作流图
 * @author kangshaofei
 * @date 2020-01-19
 */
public class TaskFlowGraph {

    /**
     * 头节点
     */
    private List<LogicTaskNode> headNodes;

    /**
     * 所有节点
     */
    private List<LogicTaskNode> allNodes;

    /**
     * 尾节点
     */
    private List<LogicTaskNode> tailNodes;

    public List<LogicTaskNode> getHeadNodes() {
        return headNodes;
    }

    public void setHeadNodes(List<LogicTaskNode> headNodes) {
        this.headNodes = headNodes;
    }

    public List<LogicTaskNode> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(List<LogicTaskNode> allNodes) {
        this.allNodes = allNodes;
    }

    public List<LogicTaskNode> getTailNodes() {
        return tailNodes;
    }

    public void setTailNodes(List<LogicTaskNode> tailNodes) {
        this.tailNodes = tailNodes;
    }

    @Override
    public String toString() {
        return "TaskFlowGraph{" +
                "headNodes=" + headNodes +
                ", allNodes=" + allNodes +
                ", tailNodes=" + tailNodes +
                '}';
    }
}
