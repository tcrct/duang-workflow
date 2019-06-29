package com.duangframework.workflow.core.model;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * 工作流的链接线对象
 */
public abstract class Edge extends BaseElement {

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

        Assert.isTrue(null != node, "baseElement is null");

        NamedNodeMap namedNodeMap = node.getAttributes();
        NodeList nodeList = node.getChildNodes();

        // 提取ID
        org.w3c.dom.Node idAttribute = namedNodeMap.getNamedItem("id");
        Assert.isTrue(null != idAttribute, "id must exist");
        this.setId(idAttribute.getNodeValue());

        for(int i=0; i<nodeList.getLength(); i++) {
            org.w3c.dom.Node nodeItem = nodeList.item(i);
//            System.out.println(nodeItem.getNodeType()+"                   "+node.getNodeName());
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

}
