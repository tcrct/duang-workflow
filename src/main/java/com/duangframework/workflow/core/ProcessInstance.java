package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Node;

import java.util.List;

/**
 * 进程实例
 * @author laotang
 */
public class ProcessInstance {

    /**审批进程ID*/
    private String id;
    /**审批进程标识*/
    private String code;
    /**审批进程路径*/
    private String processPath;
    /**审批进程条件分支节点*/
    private List<Node> processNode;

}
