package com.duangframework.workflow.entity;

import java.util.List;
import com.duangframework.workflow.core.ProcessInstance;
/**
 * 工作流Dto
 * @author laotang
 */
public class WorkFlow {

    /**
     * 工作流名称
     */
    private String name;
    /**
     *  工作进程实例集合
     */
    private List<ProcessInstance> processInstanceList;

    public WorkFlow(String name, List<ProcessInstance> processInstanceList) {
        this.name = name;
        this.processInstanceList = processInstanceList;
    }

    public WorkFlow() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProcessInstance> getProcessInstanceList() {
        return processInstanceList;
    }

    public void setProcessInstanceList(List<ProcessInstance> processInstanceList) {
        this.processInstanceList = processInstanceList;
    }
}
