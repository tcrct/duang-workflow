package com.duangframework.workflow.entity;

import com.duangframework.workflow.core.model.Approve;

import java.util.List;
import java.util.Map;

/**
 * 审批进程实例
 * 是指每一条工作流程线，例如一个工作流有一个分支节点，分支节点上有两个条件，则代表有两条进程线，进程线上如果又存在条件分支节点，则再次拆分。以此类推
 * 
 * @author laotang 
 */
public class ProcessInstance {
    /**
     * 进程ID
     */
    private String id;
    /**
     * 进程代号，将ConditionDto里的kv键值对以Base64的方式生成
     */
    private String processCode;
    /**
     * 条件 Map集合
     */
    private Map<String,String> conditionMap;
    /**
     * 工作进程实例节点集合，即每个进程线经过的开始结束节点，审批人节点与抄送人节点
     */
    private List<Approve> processNodeList;

    public ProcessInstance(String id, String processCode, Map<String,String> conditionMap,  List<Approve> processNodeList) {
        this.id = id;
        this.processCode = processCode;
        this.conditionMap = conditionMap;
        this.processNodeList = processNodeList;
    }

    public ProcessInstance() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public Map<String, String> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public List<Approve> getProcessNodeList() {
        return processNodeList;
    }

    public void setProcessNodeList(List<Approve> processNodeList) {
        this.processNodeList = processNodeList;
    }
}
