package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Action;
import com.duangframework.workflow.core.model.ActionEdge;
import com.duangframework.workflow.core.model.ActionNode;
import com.duangframework.workflow.utils.DuangId;

import java.util.List;

/**
 * 进程实例
 * @author laotang
 */
public class ProcessInstance {

    /**审批进程ID*/
    private String id;
    /**所有进程节点*/
    private List<List<Action>> actionList;
    /**
     * 审批条件分支节点
     */
    private List<ActionEdge> actionEdgeList;
    /**
     * 审批人或抄送人
     */
    private List<ActionNode> actionNodeIdList;

    public ProcessInstance() {
    }

    public ProcessInstance(List<List<Action>> actionList, List<ActionEdge> actionEdgeList, List<ActionNode> actionNodeIdList) {
        this.id = new DuangId().toString();
        this.actionList = actionList;
        this.actionEdgeList = actionEdgeList;
        this.actionNodeIdList = actionNodeIdList;
    }

    public List<List<Action>> getActionList() {
        return actionList;
    }

    public void setActionList(List<List<Action>> actionList) {
        this.actionList = actionList;
    }

    public List<ActionEdge> getActionEdgeList() {
        return actionEdgeList;
    }

    public void setActionEdgeList(List<ActionEdge> actionEdgeList) {
        this.actionEdgeList = actionEdgeList;
    }

    public List<ActionNode> getActionNodeIdList() {
        return actionNodeIdList;
    }

    public void setActionNodeIdList(List<ActionNode> actionNodeIdList) {
        this.actionNodeIdList = actionNodeIdList;
    }
}
