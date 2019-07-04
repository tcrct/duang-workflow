package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;

import java.util.List;
import java.util.Map;

/**
 * 逻辑算法接口类
 * @author laotang
 */
public interface IFlowAlgorithmService {

    /**
     *  流程引擎
     * @param xmlDoc    xml内容字符串
     * @throws Exception
     */
    <T> T engine(String xmlDoc) throws Exception;

    /**
     * 业务逻辑算法实现
     * @param edgeMap   链接线集合
     * @param nodeMap   节点集合
     * @param processInstanceList   工作流进程集合
     * @return
     * @throws Exception
     */
    <T> T execute(Map<String, Edge> edgeMap, Map<String, Node> nodeMap, List<ProcessInstance> processInstanceList) throws Exception;
}
