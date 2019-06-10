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
    public String getId() {
        return edge.getId();
    }

    @Override
    public String getLabel() {
        return edge.getLabel();
    }

    @Override
    public void convetor() {
        NodeConvetorFatctory.getInstance().convetor(node);
    }
}
