package com.duangframework.workflow.utils;

import com.duangframework.kit.ToolsKit;
import com.duangframework.workflow.core.NodeConvetor;
import com.duangframework.workflow.core.ProcessInstance;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.service.EndNodeConvetor;
import com.duangframework.workflow.service.ShapeNodeConvetor;
import com.duangframework.workflow.service.TaskNodeConvetor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by laotang on 2019/6/10.
 */
public class NodeConvetorFatctory {

    private final static Logger logger = LoggerFactory.getLogger(NodeConvetorFatctory.class);
    private static Map<String, Node> nodeMap;
    private static Map<String, Edge> edgeMap;
    private static NodeConvetorFatctory nodeConvetorFatctory = null;

    private NodeConvetorFatctory() {
    }


    public static NodeConvetorFatctory getInstance() {
        return nodeConvetorFatctory;
    }

    public static NodeConvetorFatctory getInstance(Map<String, Node> nodeMap, Map<String, Edge> edgeMap) {
        if(null == nodeConvetorFatctory) {
            nodeConvetorFatctory = new NodeConvetorFatctory();
            NodeConvetorFatctory.nodeMap =  nodeMap;
            NodeConvetorFatctory.edgeMap =  edgeMap;
        }

        return nodeConvetorFatctory;
    }



    public ProcessInstance convetor(Node processNode) {
        if(ToolsKit.isEmpty(processNode)) {
            throw new NullPointerException("node is null!");
        }
        ProcessInstance instance = new ProcessInstance();
        List<Edge> outEdgeList = processNode.getOutgoing();
        for (Edge edge : outEdgeList) {
            String targetId = edge.getTargetId();
            Node node = nodeMap.get(targetId);
            NodeConvetor nodeConvetor = parse(edge, node);
//            System.out.println(nodeConvetor.getId()+"                 "+nodeConvetor.getLabel());
            nodeConvetor.convetor();
        }
        return null;
    }

    private NodeConvetor parse(Edge edge, Node node) {
        NodeConvetor convetor = null;
        // 如果是分支节点
        if(WorkflowUtils.isShapeNode(node)) {
            convetor = new ShapeNodeConvetor(edge, node);
        } else if (WorkflowUtils.isTaskNode(node)) {
            convetor = new TaskNodeConvetor(edge, node);
        } else if (WorkflowUtils.isEndNode(node)) {
            convetor = new EndNodeConvetor(edge, node);
        }
        return convetor;
    }

}
