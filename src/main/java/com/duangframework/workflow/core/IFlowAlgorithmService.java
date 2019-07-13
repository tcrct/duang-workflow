package com.duangframework.workflow.core;

import java.util.List;

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
     * @param processInstanceList   工作流进程集合
     * @return
     * @throws Exception
     */
    <T> T execute(ProcessInstance processInstanceList) throws Exception;
}
