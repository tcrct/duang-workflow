package com.duangframework.workflow.core;

import com.duangframework.utils.DuangId;
import java.util.List;
import java.util.Map;

/**
 * 进程实例
 * @author laotang
 */
public class ProcessInstance {

    /**审批进程ID*/
    private String id;
    /**审批进程标识*/
    private String code;
    /**审批进程分支条件路径*/
    private Map<String, String> conditionMap;
    /**审批进程条件分支节点ID*/
    private List<String> processNode;

    public ProcessInstance() {
    }

    public ProcessInstance(Map<String, String> conditionMap, List<String> processNode) {
        this.id = new DuangId().toString();
        this.conditionMap = conditionMap;
        this.processNode = processNode;
        this.code = createCode();
    }

    public ProcessInstance(String id, String code, Map<String, String> conditionMap, List<String> processNode) {
        this.id = id;
        this.code = code;
        this.conditionMap = conditionMap;
        this.processNode = processNode;
    }

    private String createCode() {
        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public List<String> getProcessNode() {
        return processNode;
    }

    public void setProcessNode(List<String> processNode) {
        this.processNode = processNode;
    }

    @Override
    public String toString() {
        return "ProcessInstance{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", conditionMap=" + conditionMap +
                ", processNode=" + processNode +
                '}';
    }
}
