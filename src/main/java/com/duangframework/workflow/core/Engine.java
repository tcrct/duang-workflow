package com.duangframework.workflow.core;

/**
 *  引擎接口
 *
 */
public interface Engine {

	public ProcessDefinition parse(Resource resource) throws Exception;

	public ProcessInstance deploy(ProcessDefinition processDefinition) throws Exception;

	public void run(ProcessInstance processInstance) throws Exception;
}
