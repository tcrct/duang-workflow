package com.duangframework.workflow.service;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import com.duangframework.utils.XmlHelper;
import com.duangframework.workflow.core.ProcessDefinition;
import com.duangframework.workflow.core.ProcessInstance;
import com.duangframework.workflow.event.*;
import com.duangframework.workflow.core.lParserService;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.utils.Const;
import com.duangframework.workflow.utils.NodeEventEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author laotang
 */
public class XMLParserService implements lParserService {

    private static final Logger logger = LoggerFactory.getLogger(XMLParserService.class);

    @Override
    public ProcessDefinition parse(String xmlDoc) throws Exception {
        Assert.notNull(xmlDoc, "xml resource is null");
//        logger.debug("xml document is {}", xmlDoc);
        // 解析这个XML文档
        return parseDocument(xmlDoc);
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     * @param str
     * @return
     */
    private String replaceSpecialStr(String str) {
        String repl = "";
        if (str != null) {
            Pattern p = Pattern.compile(">(\\s*|\n|\t|\r)<");
            Matcher m = p.matcher(str);
            repl = m.replaceAll("><");
        }
        return repl;
    }

    private ProcessDefinition parseDocument(String xmlDoc) {
        xmlDoc = replaceSpecialStr(xmlDoc);
        Assert.notNull(xmlDoc, "document is null");
        // 获取definitions元素
        XmlHelper xmlHelper = XmlHelper.of(xmlDoc);
        org.w3c.dom.Node rootNode = xmlHelper.getNode("/mxGraphModel/root");
        // 开始提取每个子元素，key为ID
        Map<String, Node> nodeMap = new HashMap<>(64);
        Map<String, Edge> edgeMap = new HashMap<>(64);
        NodeList nodeList = rootNode.getChildNodes();
        int length = nodeList.getLength();
        try {
            NodeEventEnum[] nodeEventEnums = NodeEventEnum.values();
            for (int i = 0; i < length; i++) {
                org.w3c.dom.Node nodeItem = nodeList.item(i);
                String nodeType = nodeItem.getNodeName().trim();
                if(Const.EDGE_NODE_NAME.equalsIgnoreCase(nodeType)) {
                    EdgeEvent edgeEvent = new EdgeEvent();
                    edgeEvent.parse(nodeItem);
                    edgeMap.put(edgeEvent.getId(), edgeEvent);
                } else {
                    for (NodeEventEnum eventEnum : nodeEventEnums) {
                        if (eventEnum.getType().equalsIgnoreCase(nodeType)) {
                            Event event = eventEnum.getEvent();
                            event.parse(nodeItem);
                            nodeMap.put(event.getId(), event);
                        }
                    }
                }
            }
            validate(nodeMap, edgeMap);
            // 真正连接点+边,并做有效性验证
            ProcessDefinition topology = new ProcessDefinition(nodeMap, edgeMap);
            return topology;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }

    }

    /**
     * 判断两个集合里的ID是否有重复的
     * @param nodeMap   节点集合
     * @param edgeMap    链接线集合
     */
    private void validate(Map<String, Node> nodeMap, Map<String, Edge> edgeMap) {

        Assert.isTrue(null != nodeMap && nodeMap.size() > 0, "node map must size >=1");
        Assert.isTrue(null != edgeMap && edgeMap.size() > 0, "edge Map must size >=1");

        Set<String> nodeKeySet = nodeMap.keySet();
        Set<String> edgeKeySet = edgeMap.keySet();

        // 两个集合的ID不应该有重复的
        // 交集
        Set<String> sameKeySet = new HashSet<>();
        sameKeySet.addAll(nodeKeySet);
        sameKeySet.retainAll(edgeKeySet);
        Assert.isTrue(null == sameKeySet || 0 == sameKeySet.size(), "duplicate id -> " + sameKeySet);

        logger.info("两个集合之间没有重复的ID");

    }


    @Override
    public List<ProcessInstance> deploy(ProcessDefinition processDefinition) throws Exception {
        List<ProcessInstance> processInstanceList = processDefinition.deploy();
        return processInstanceList;
    }
}
