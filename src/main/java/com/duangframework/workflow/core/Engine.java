package com.duangframework.workflow.core;

import com.duangframework.workflow.entity.WorkFlow;

import java.util.List;

/**
 *  引擎接口
 * @author laotang
 */
public interface Engine {

	ProcessDefinition parse(String resource) throws Exception;

	List<ProcessInstance> deploy(ProcessDefinition processDefinition) throws Exception;

	WorkFlow execute(List<ProcessInstance> processInstance) throws Exception;
}
