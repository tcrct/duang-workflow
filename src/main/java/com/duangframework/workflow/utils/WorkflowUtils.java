package com.duangframework.workflow.utils;

import com.duangframework.kit.ToolsKit;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.event.ShapeEvent;
import com.duangframework.workflow.event.TaskEvent;

/**
 * Created by laotang on 2019/6/11.
 */
public class WorkflowUtils {

    public static boolean isStartNode(Node node) {
        return ToolsKit.isEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing()) && Const.START_LABEL.equals(node.getLabel());
    }

    public static boolean isShapeNode(Node node) {
        return node instanceof ShapeEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
    }

    public static boolean isTaskNode(Node node) {
        return node instanceof TaskEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
    }

    public static boolean isEndNode(Node node) {
        return ToolsKit.isEmpty(node.getOutgoing()) && ToolsKit.isNotEmpty(node.getIncoming()) && Const.END_LABEL.equals(node.getLabel());
    }
}
