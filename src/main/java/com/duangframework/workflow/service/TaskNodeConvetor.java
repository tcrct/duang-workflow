package com.duangframework.workflow.service;

import com.duangframework.workflow.core.NodeConvetor;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.utils.NodeConvetorFatctory;

/**
 * Created by laotang on 2019/6/11.
 */
public class TaskNodeConvetor extends NodeConvetor {

    public TaskNodeConvetor(Edge edge, Node node) {
        super(edge, node);
    }

    @Override
    public String getId() {
        return node.getId();
    }

    @Override
    public String getLabel() {
        return node.getLabel();
    }

    @Override
    public void convetor() {
        System.out.println(node.getId()+"                "+node.getLabel());
        NodeConvetorFatctory.getInstance().convetor(node);
    }


}
