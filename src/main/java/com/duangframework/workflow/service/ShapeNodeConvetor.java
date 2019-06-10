package com.duangframework.workflow.service;

import com.duangframework.workflow.core.NodeConvetor;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.utils.NodeConvetorFatctory;

/**
 * Created by laotang on 2019/6/11.
 */
public class ShapeNodeConvetor extends NodeConvetor {

    public ShapeNodeConvetor(Edge edge, Node node) {
        super(edge, node);
    }

    @Override
    public Node convetor() {
        System.out.println(node.getId()+"                "+node.getLabel());
        NodeConvetorFatctory.getInstance().convetor(node);
        return null;
    }
}
