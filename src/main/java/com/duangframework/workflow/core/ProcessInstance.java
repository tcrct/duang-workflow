package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Action;
import com.duangframework.workflow.utils.DuangId;

import java.util.List;

/**
 * 进程实例
 * @author laotang
 */
public class ProcessInstance {

    /**审批进程ID*/
    private String id;
    /**审批进程节点*/
    private List<Action> actionList;

    public ProcessInstance() {
    }

    public ProcessInstance(List<Action> actionList) {
        this.id = new DuangId().toString();
        this.actionList = actionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    @Override
    public String toString() {
        return "ProcessInstance{" +
                "id='" + id + '\'' +
                ", actionList=" + actionList +
                '}';
    }
}
