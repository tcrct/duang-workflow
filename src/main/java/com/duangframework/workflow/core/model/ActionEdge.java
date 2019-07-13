package com.duangframework.workflow.core.model;

import java.util.List;
import java.util.Set;

/**
 *
 * @author laotang
 */
public class ActionEdge implements java.io.Serializable {

    /**条件线条的父节点*/
    private Action parentEdge;
    /**对应的条件线条节点*/
    private List<Action> subEdgeList;

    public ActionEdge() {

    }

    public ActionEdge(Action parentEdge, List<Action> subEdgeList) {
        this.parentEdge = parentEdge;
        this.subEdgeList = subEdgeList;
    }

    public Action getParentEdge() {
        return parentEdge;
    }

    public void setParentEdge(Action parentEdge) {
        this.parentEdge = parentEdge;
    }

    public List<Action> getSubEdgeList() {
        return subEdgeList;
    }

    public void setSubEdgeList(List<Action> subEdgeList) {
        this.subEdgeList = subEdgeList;
    }
}
