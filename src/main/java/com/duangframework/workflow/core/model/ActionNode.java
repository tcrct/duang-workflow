package com.duangframework.workflow.core.model;

import java.util.List;
import java.util.Set;

/**
 *
 * @author laotang
 */
public class ActionNode implements java.io.Serializable {

    /**条件线条的ID总和*/
    private String edgeCountCode;
    /**审核或抄送人节点*/
    private List<Action> subActionList;

    public ActionNode() {

    }

    public ActionNode(String edgeCountCode, List<Action> subActionList) {
        this.edgeCountCode = edgeCountCode;
        this.subActionList = subActionList;
    }

    public String getEdgeCountCode() {
        return edgeCountCode;
    }

    public void setEdgeCountCode(String edgeCountCode) {
        this.edgeCountCode = edgeCountCode;
    }

    public List<Action> getSubActionList() {
        return subActionList;
    }

    public void setSubActionList(List<Action> subActionList) {
        this.subActionList = subActionList;
    }
}
