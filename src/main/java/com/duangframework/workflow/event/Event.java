package com.duangframework.workflow.event;


import com.duangframework.workflow.core.model.Node;

/**
 * 节点类型
 * @author laotang
 */
public abstract class Event extends Node {
	public void parse(org.w3c.dom.Node element) throws Exception {
		super.parse(element);
	}

}
