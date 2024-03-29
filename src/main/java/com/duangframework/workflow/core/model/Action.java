package com.duangframework.workflow.core.model;

import com.duangframework.workflow.utils.WorkflowUtils;

/**
 * 所经过的节点
 * @author laotang
 */
public class Action implements java.io.Serializable{

    /**
     * 节点ID
     */
    private String id;
    /**
     * 节点标签，如果是分支节点，则是分支条件字符串
     */
    private String label;
    /**
     * 节点详细内容, 一般是json字符串
     */
    private String desc;
    /**
     * 节点类型
     */
    private String type;
    /**
     * 节点唯一标识
     */
    private String code;

    public Action() {
    }

    public Action(String id, String label, String desc, String type, String code) {
        this.id = id;
        this.label = label;
        this.desc = desc;
        this.type = type;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
