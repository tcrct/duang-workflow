package com.duangframework.workflow.utils;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mvc.http.enums.ConstEnums;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.event.ShapeEvent;
import com.duangframework.workflow.event.TaskEvent;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by laotang on 2019/6/11.
 */
public class WorkflowUtils {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowUtils.class);

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

    /**
     * 创建条件分支的代码，方便根据条件分支值找出对应的流程审批人节点
     * @param conditionMap 条件内容Map
     * @return
     */
    public static String createProcessCode(Map<String,String> conditionMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if(ToolsKit.isNotEmpty(conditionMap)) {
            conditionMap.entrySet().iterator().forEachRemaining(new Consumer<Map.Entry<String, String>>() {
                @Override
                public void accept(Map.Entry<String, String> entry) {
                    stringBuilder.append(entry.getKey() + entry.getValue());
                }
            });
        }
        try {
            String conditionString = stringBuilder.toString();
            if(ToolsKit.isNotEmpty(conditionString)) {
                return Base64.encodeBase64String(conditionString.getBytes(ConstEnums.PROPERTIES.DEFAULT_ENCODING.getValue()));
            }
            return "";
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return "";
        }
    }
}
