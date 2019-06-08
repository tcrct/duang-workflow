package com.duangframework.workflow.core.model;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * 工作流的链接线对象
 */
public abstract class Edge extends BaseElement {

    public static final Logger LOGGER = LoggerFactory.getLogger(Edge.class);

    /**
     * 源节点引用，即连接线的起始节点ID
     */
    private String sourceId;
    /**
     * 目标节点引用，即连接线的结束节点ID
     */
    private String targetId;

    /**与sourceId对应的节点*/
    private Node sourceNode;

    /**与targetId对应的节点*/
    private Node targetNode;

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
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

    public void parse(org.w3c.dom.Node node) throws Exception {

        super.parse(node);
//        System.out.print(node.getNodeName()+"               ");

        Assert.isTrue(null != node, "baseElement is null");

        NamedNodeMap namedNodeMap = node.getAttributes();
        NodeList nodeList = node.getChildNodes();

        // 提取ID
        org.w3c.dom.Node idAttribute = namedNodeMap.getNamedItem("id");
        Assert.isTrue(null != idAttribute, "id must exist");
        this.setId(idAttribute.getNodeValue());

        for(int i=0; i<nodeList.getLength(); i++) {
            org.w3c.dom.Node nodeItem = nodeList.item(i);
//            System.out.println(nodeItem.getNodeName());
            NamedNodeMap attributesNodeMap = nodeItem.getAttributes();
            // 提取sourceId
            org.w3c.dom.Node sourceNode = attributesNodeMap.getNamedItem("source");
            // 提取targetId
            org.w3c.dom.Node targetNode = attributesNodeMap.getNamedItem("target");
            if(ToolsKit.isNotEmpty(sourceNode)) {
                this.sourceId = sourceNode.getNodeValue();
            }
            if(ToolsKit.isNotEmpty(targetNode)) {
                this.targetId = targetNode.getNodeValue();
            }
        }
    }

    protected void copy(Object obj) {

//        super.copy(obj);

        Edge edge = (Edge) obj;

        // 只拷贝节点ID标志
        edge.sourceId = this.sourceId;
        edge.targetId = this.targetId;

        // 拷贝时明确不拷贝节点
        edge.sourceNode = null;
        edge.targetNode = null;

    }

//    @Override
//    public void ignore(ProcessInstance instance) {
//        setState(State.IGNORED);
//        LOGGER.debug("edge [{}] -> ignored", getId());
//    }

}
