package com.duangframework.workflow.service;

import com.duangframework.workflow.core.NodeConvetor;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.utils.NodeConvetorFatctory;

import java.util.List;

/**
 * Created by laotang on 2019/6/11.
 */
public class ShapeNodeConvetor extends NodeConvetor {

    public ShapeNodeConvetor(Edge edge, Node node) {
        super(edge, node);
    }

    @Override
    public String getId() {
        return edge.getTargetId();
    }

    @Override
    public String getLabel() {
        return edge.getTargetNode().getLabel();
    }

    @Override
    public void convetor() {
        List<Edge> edgeList = node.getOutgoing();
//        System.out.println("@@@@@@@@@: " + node.getLabel());
        for(Edge e : edgeList) {
            Node n = e.getTargetNode();
            System.out.println("    ######: " + e.getLabel());
            NodeConvetorFatctory.getInstance().convetor(n);
        }
//
    }
}
