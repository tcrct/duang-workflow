package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.entity.WorkFlow;

import java.util.List;
import java.util.Map;

/**
 * 逻辑算法接口类
 * @author laotang
 */
public interface IFlowAlgorithmService {
    /**
     * 业务逻辑算法实现
     * @param nodeMap   节点集合
     * @param processInstanceList   工作流进程集合
     * @return
     * @throws Exception
     */
    WorkFlow execute(Map<String, Node> nodeMap, List<ProcessInstance> processInstanceList) throws Exception;
}
