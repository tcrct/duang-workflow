package com.duangframework.workflow.core;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.event.*;
import com.duangframework.workflow.utils.Const;
import com.duangframework.workflow.utils.NodeEventEnum;

import java.util.*;

/**
 * @author laotang
 */
public class ProcessDefinition {

	private Map<String, Node> nodeMap;

	private Map<String, Edge> edgeMap;

	private List<ProcessInstance> processInstanceList;

	public ProcessDefinition(Map<String, Node> nodeMap, Map<String, Edge> edgeMap) throws Exception {

		Assert.isTrue(null != nodeMap, "not valid node map");
		Assert.isTrue(null != edgeMap, "not valid edge map");
		this.nodeMap = nodeMap;
		this.edgeMap = edgeMap;

		buildConnection();

	}

	public Map<String, Node> getNodeMap() {
		return nodeMap;
	}

	public Map<String, Edge> getEdgeMap() {
		return edgeMap;
	}

	/**
	 * 点和边进行关联->形成1个完整的拓扑图 ,否则孤立的没有意义
	 * 
	 * @throws Exception
	 * 
	 */
	private void buildConnection() throws Exception {

		// 这里我们来构造拓扑图,在构造的过程中我们来做有效性验证
		// 我们以边来作为主视角来串联起来点和边
		Collection<Edge> edges = edgeMap.values();
		for (Edge edge : edges) {
			// 连接线的源节点，左边节点ID
			String sourceNodeId = edge.getSourceId();
			// 连接线的目标节点，右边节点ID
			String targetNodeId = edge.getTargetId();
			Node sourceNode = nodeMap.get(sourceNodeId);
			Node targetNode = nodeMap.get(targetNodeId);
			Assert.isTrue(null != sourceNode, "find not node , not valid src node id -> " + sourceNodeId);
			Assert.isTrue(null != targetNode, "find not node , not valid target node id -> " + targetNodeId);

			// 现在我们已经知道这个边和这2个点都有关系

			// 完善源节点的信息点，即确定有多少条线出去连接到下一个节点的
			sourceNode.getOutgoing().add(edge);
			sourceNode.setConnected(true);

			// 完善目标节点的信息点
			targetNode.getIncoming().add(edge);
			targetNode.setConnected(true);

			// 完善edge的信息点
			edge.setSourceNode(sourceNode);
			edge.setTargetNode(targetNode);
			edge.setConnected(true);
		}
 	}

	public  List<ProcessInstance> deploy() throws Exception {
		processInstanceList = new ArrayList<>();
//		 上面已经保证了每个边都用到了,所以只要判断是不是每个点都被使用了
//		 顺便发现startEvent而且必须唯一
		StartEvent startEvent = null;
		Collection<Node> nodes = nodeMap.values();
		for (Node node : nodes) {
			Assert.isTrue(node.isConnected(), "节点不存在，请检查XML文件 , id is ->" + node.getId()+", label is ->"+node.getDescription());
			// 开启或结束节点
			if (node instanceof StartEvent) {
				if(isStartNode(node)) {
					Assert.isTrue(null == startEvent, "开始节点只能有1个!");
					startEvent = (StartEvent) node;
				}
			}
		}
		// 确保一个合格的流程开始是至少有1个起点,不然执行啥
		Assert.isTrue(null != startEvent, "开始节点不存在，请检查XML文件是否正确!");
         List<String> nodeIdList = new ArrayList<>();
        nodeIdList.add(startEvent.getId());
        // 递归取出所有审批线路，如果有分支节点，则以分支节点作为key
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<>();
		createProcessNode(startEvent, conditionMap, nodeIdList);
		return processInstanceList;
	}

	private boolean isStartNode(Node node) {
		return node instanceof StartEvent && ToolsKit.isEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing()) && Const.START_LABEL.equals(node.getLabel());
	}

	private boolean isRhombusNode(Node node) {
		return node instanceof RhombusEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
	}

	private boolean isTaskNode(Node node) {
		return node instanceof TaskEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
	}

	private boolean isCcNode(Node node) {
		return node instanceof CcEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
	}

	private boolean isEndNode(Node node) {
		return  node instanceof EndEvent && ToolsKit.isEmpty(node.getOutgoing()) && ToolsKit.isNotEmpty(node.getIncoming()) && Const.END_LABEL.equals(node.getLabel());
	}

	private void createProcessNode(Node processNode, LinkedHashMap<String, String> conditionMap,  List<String> nodeIdList) {
		List<Edge> outEdgeList = processNode.getOutgoing();
		/** 如果没有出边的线，则说明可能到了结束节点，这个时候，需要判定一下结束节点的上一个节点是否分支条件节点
		 * 如果是条件节点，即结束节点的上一节点是是分支条件节点，需要将分支条件添加到子流程实例，产生记录保存到数据库
		 */
		if(outEdgeList.isEmpty()) {
			List<Edge> inEdgeList =processNode.getIncoming();
			for(Edge edge : inEdgeList) {
				String sourceId = edge.getSourceId();
				Node node = nodeMap.get(sourceId);
				if(isRhombusNode(node) && edge.getLabel().length() > 0) {
					conditionMap.put(node.getId(), edge.getId());
					ProcessInstance instance = new ProcessInstance(conditionMap, nodeIdList);
//					System.out.println("  #########:  " + instance);
					processInstanceList.add(instance);
				}
			}
		} else {
			for (Edge edge : outEdgeList) {
				String targetId = edge.getTargetId();
				Node node = nodeMap.get(targetId);
				String nodeId = node.getId();
				if (isRhombusNode(node)) {
					for (Iterator<Edge> iterator = node.getOutgoing().iterator(); iterator.hasNext(); ) {
						Edge e = iterator.next();
						List<String> copyNodeIdList = new ArrayList(nodeIdList);
						LinkedHashMap<String, String> copyConditionMap = new LinkedHashMap<>(conditionMap);
						Node itemNode = nodeMap.get(e.getTargetId());
						copyNodeIdList.add(itemNode.getId());
						copyConditionMap.put(node.getId(), e.getId());
						createProcessNode(itemNode, copyConditionMap, copyNodeIdList);
					}
				} else if (isTaskNode(node) || isCcNode(node)) {
					if (isCcNode(node)) {
						nodeId = NodeEventEnum.CC.name().toLowerCase() + "_" + nodeId;
					}
					nodeIdList.add(nodeId);
					createProcessNode(node, conditionMap, nodeIdList);
				} else if (isEndNode(node)) {
					nodeIdList.add(nodeId);
					ProcessInstance instance = new ProcessInstance(conditionMap, nodeIdList);
//					System.out.println("  #########:  " + instance);
					processInstanceList.add(instance);
				}
			}
		}
	}
}
