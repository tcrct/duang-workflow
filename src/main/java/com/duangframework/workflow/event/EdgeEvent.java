package com.duangframework.workflow.event;


import com.duangframework.workflow.core.model.Edge;
import org.w3c.dom.Node;

public class EdgeEvent extends Edge {

	public void parse(Node node) throws Exception {
		super.parse(node);
	}

//	public Object copy() {
//		ShapeEvent endEvent = new ShapeEvent();
//		copy(endEvent);
//		return endEvent;
//	}
//
//	protected void copy(Object obj) {
//		super.copy(obj);
//	}

}
