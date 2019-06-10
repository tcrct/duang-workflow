package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;

/**
 * Created by laotang on 2019/6/11.
 */
public abstract class NodeConvetor {

    protected Node node;
    protected Edge edge;

    protected NodeConvetor(Edge edge, Node node) {
        this.node = node;
    }

    public abstract String getId();
    public abstract String getLabel();
    public abstract void convetor();

}
