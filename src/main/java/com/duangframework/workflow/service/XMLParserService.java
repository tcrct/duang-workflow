package com.duangframework.workflow.service;

import com.duangframework.workflow.core.ProcessDefinition;
import com.duangframework.workflow.core.ProcessInstance;
import com.duangframework.workflow.event.*;
import com.duangframework.workflow.core.lParserService;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.utils.*;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * xml文件解析服务
 *
 * @author laotang
 */
public class XMLParserService implements lParserService {


    @Override
    public ProcessDefinition parse(String xmlDoc) throws Exception {
        Assert.notNull(xmlDoc, "xml resource is null");
        // 解析这个XML文档
        return parseDocument(xmlDoc);
    }



    private ProcessDefinition parseDocument(String xmlDoc) {
        xmlDoc = WorkflowUtils.replaceSpecialStr4Xml(xmlDoc);
        Assert.notNull(xmlDoc, "document is null");
        XmlHelper xmlHelper = XmlHelper.of(xmlDoc);
        // 获取root元素
        org.w3c.dom.Node rootNode = xmlHelper.getNode("/mxGraphModel/root");
        // 开始提取每个子元素，key为Id(xml里的id)
        Map<String, Node> nodeMap = new HashMap<>(64);
        // 连接线子元素，key为Id
        Map<String, Edge> edgeMap = new HashMap<>(64);
        // 取出所有第一级节点
        NodeList nodeList = rootNode.getChildNodes();
        int length = nodeList.getLength();
        try {
            StringBuilder nodeXmlString = new StringBuilder();
            StringBuilder edgeXmlString = new StringBuilder();
            NodeEventEnum[] nodeEventEnums = NodeEventEnum.values();
            for (int i = 0; i < length; i++) {
                org.w3c.dom.Node nodeItem = nodeList.item(i);
                String nodeType = nodeItem.getNodeName().trim();
                if(Const.EDGE_NODE_NAME.equalsIgnoreCase(nodeType)) {
                    EdgeEvent edgeEvent = new EdgeEvent();
                    edgeEvent.parse(nodeItem);
                    edgeMap.put(edgeEvent.getId(), edgeEvent);
                    edgeXmlString.append(nodeItem);
                } else {
                    for (NodeEventEnum eventEnum : nodeEventEnums) {
                        if (eventEnum.getType().equalsIgnoreCase(nodeType)) {
                            Event event = eventEnum.getEvent();
                            event.parse(nodeItem);
                            nodeMap.put(event.getId(), event);
                        }
                    }
                    nodeXmlString.append(nodeItem);
                }
            }
            String sortXml = nodeXmlString.toString()+edgeXmlString.toString();
            validate(nodeMap, edgeMap);
            // 真正连接点+边,并做有效性验证
            ProcessDefinition topology = new ProcessDefinition(nodeMap, edgeMap);
            topology.setSortXml(sortXml);
            System.out.println("sortXml: " + sortXml);
            return topology;
        } catch (Exception e) {
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

        System.out.println("两个集合之间没有重复的ID");

    }


    @Override
    public ProcessInstance deploy(ProcessDefinition processDefinition) throws Exception {
        ProcessInstance processInstanceList = processDefinition.deploy();
        return processInstanceList;
    }
}
