package com.duangframework.workflow.utils;

import com.duangframework.workflow.event.*;

/**
 * 工作流节点枚举
 *
 * @author  laotang
 * @date 2019/5/16
 */
public enum NodeEventEnum {

    /**
     *
     */
    START("Start", "开始节点", StartEvent.class),
    END("End", "结束节点", EndEvent.class),
    RHOMBUS("Rhombus", "条件节点", RhombusEvent.class),
    TASK("Task", "任务/审批人节点", TaskEvent.class),
    CC("Cc", "抄送节点", CcEvent.class),
    ;

    /**节点类型*/
    private String type;
    /**节点说明*/
    private String desc;
    /**对应的处理类*/
    private Class<?> eventClass;

    NodeEventEnum(String type, String desc, Class<?> eventClass) {
        this.type = type;
        this.desc = desc;
        this.eventClass = eventClass;
    }

    public String getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }
    public Event getEvent() {
        return WorkflowUtils.newInstance(eventClass);
    }
}
