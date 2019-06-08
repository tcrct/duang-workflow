package com.duangframework.workflow.core;

/**
 *  引擎接口
 * @author laotang
 */
public interface Engine {

	public ProcessDefinition parse(String resource) throws Exception;

//	public ProcessInstance deploy(ProcessDefinition processDefinition) throws Exception;

//	public void run(ProcessInstance processInstance) throws Exception;
}
