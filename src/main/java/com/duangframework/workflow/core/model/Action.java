package com.duangframework.workflow.core.model;

/**
 * 所经过的节点
 * @author laotang
 */
public class Action implements java.io.Serializable{

    /**
     * 源节点，即连接线的开始(左边或上边)节点
     */
    private Node sourceNode;
    /**
     * 连接线
     */
    private Edge edge;
    /**
     * 目标节点，即连接线的结束(箭头指向,右边或下边)节点
     */
    private Node targetNode;

    public Action() {
    }

    public Action(Node sourceNode, Node targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }


    public Action(Edge edge, Node targetNode) {
        this.edge = edge;
        this.targetNode = targetNode;
    }

    public Action(Node sourceNode, Edge edge, Node targetNode) {
        this.sourceNode = sourceNode;
        this.edge = edge;
        this.targetNode = targetNode;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }
}
