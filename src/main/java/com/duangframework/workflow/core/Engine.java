package com.duangframework.workflow.core;

/**
 *  引擎接口
 * @author laotang
 */
public interface Engine {

	ProcessDefinition parse(String resource) throws Exception;

	ProcessInstance deploy(ProcessDefinition processDefinition) throws Exception;

	<T> T execute(ProcessInstance processInstance) throws Exception;
}
