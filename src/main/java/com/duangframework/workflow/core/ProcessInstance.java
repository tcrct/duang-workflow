package com.duangframework.workflow.core;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.DuangId;
import com.duangframework.workflow.core.model.Action;
import com.duangframework.workflow.core.model.BaseElement;

import java.util.List;
import java.util.Map;

/**
 * 进程实例
 * @author laotang
 */
public class ProcessInstance {

    /**审批进程ID*/
    private String id;
    /**审批进程节点*/
    private List<BaseElement> actionList;

    public ProcessInstance() {
    }

    public ProcessInstance(List<BaseElement> actionList) {
        this.id = new DuangId().toString();
        this.actionList = actionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BaseElement> getActionList() {
        return actionList;
    }

    public void setActionList(List<BaseElement> actionList) {
        this.actionList = actionList;
    }

    @Override
    public String toString() {
        return "ProcessInstance{" +
                "id='" + id + '\'' +
                ", actionList=" + ToolsKit.toJsonString(actionList) +
                '}';
    }
}
