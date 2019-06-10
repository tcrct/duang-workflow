package com.duangframework.workflow.core;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.event.ShapeEvent;
import com.duangframework.workflow.event.SymbolEvent;
import com.duangframework.workflow.event.TaskEvent;
import com.duangframework.workflow.utils.Const;
import com.duangframework.workflow.utils.NodeConvetorFatctory;


import java.util.*;

/**
 * @author laotang
 */
public class ProcessDefinition {

	private Map<String, Node> nodeMap;

	private Map<String, Edge> edgeMap;

	public ProcessDefinition(Map<String, Node> nodeMap, Map<String, Edge> edgeMap) throws Exception {

		Assert.isTrue(null != nodeMap, "not valid node map");
		Assert.isTrue(null != edgeMap, "not valid edge map");
		this.nodeMap = nodeMap;
		this.edgeMap = edgeMap;

		buildConnection();

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
//		System.out.println(ToolsKit.toJsonString(edges));
		for (Edge edge : edges) {
			String sourceNodeId = edge.getSourceId();
			String targetNodeId = edge.getTargetId();
			Node sourceNode = nodeMap.get(sourceNodeId);
			Node targetNode = nodeMap.get(targetNodeId);
			Assert.isTrue(null != sourceNode, "find not node , not valid src node id -> " + sourceNodeId);
			Assert.isTrue(null != targetNode, "find not node , not valid target node id -> " + targetNodeId);

			// 现在我们已经知道这个边和这2个点都有关系

			// 完善源节点的信息点
			sourceNode.getOutgoing().add(edge);
			sourceNode.setConnected(true);

			// 完善目标节点的信息点
			targetNode.getIncoming().add(edge);
			targetNode.setConnected(true);

			// 完善edge的信息点
			edge.setSourceNode(sourceNode);
			edge.setTargetNode(targetNode);
			edge.setConnected(true);

			/*
			if (edge instanceof SequenceFlow) {

				SequenceFlow flow = (SequenceFlow) edge;
				ConditionExpression conditionExpression = flow.getConditionExpression();

				// 如果点是排它网关,则边的条件表达式应该不为空
				if (sourceNode instanceof ExclusiveGateway) {

					if (null == conditionExpression) {
						throw new Exception("the all edges of ExclusiveGateway node " + sourceNode.getId()
								+ " should has condition expression");
					}

					String expression = conditionExpression.getExpression();
					if (null == expression || expression.trim().length() <= 0) {
						throw new Exception("the all edges of ExclusiveGateway node " + sourceNode.getId()
								+ " should has condition expression");
					}

				}
				// 如果点是并行网关,则条件表达式必须为空
				else if (sourceNode instanceof ParallelGateway) {
					if (null != conditionExpression) {
						throw new Exception("the all edges of ExclusiveGateway node " + sourceNode.getId()
								+ " should no condition expression,delete it and retry");
					}
				}
			}
			*/
		}
		/*
		for(Iterator<Map.Entry<String,Node>> iterator = nodeMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String,Node> entry = iterator.next();
			Node node = entry.getValue();
			System.out.println(entry.getKey()+"       "+node.getIncoming()+"            "+node.getOutgoing());
		}
		System.out.println();
		System.out.println();System.out.println();
		for(Iterator<Map.Entry<String,Edge>> iterator = edgeMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String,Edge> entry = iterator.next();
			Edge node = entry.getValue();
			System.out.println(entry.getKey()+"       "+node.getSourceNode()+"            "+node.getTargetNode());
		}
		*/
 	}

	@SuppressWarnings("unused")
	public ProcessInstance deploy() throws Exception {

//		 上面已经保证了每个边都用到了,所以只要判断是不是每个点都被使用了
//		 顺便发现startEvent而且必须唯一
		SymbolEvent startEvent = null, endEvent = null;
		Collection<Node> nodes = nodeMap.values();
		for (Node node : nodes) {
			Assert.isTrue(node.isConnected(), "节点不存在，请检查XML文件 , id is ->" + node.getId()+", label is ->"+node.getDescription());
			// 开启或结束节点
			if (node instanceof SymbolEvent) {
				if(isStartNode(node)) {
					Assert.isTrue(null == startEvent, "开始节点只能有1个!");
					startEvent = (SymbolEvent) node;
					System.out.println("StartEvent: " + startEvent.getId() + "               " + startEvent.getOutgoing()+"                 "+startEvent.getDescription());
				}
				if(isEndNode(node)) {
					endEvent = (SymbolEvent) node;
					System.out.println("EndEvent: " + startEvent.getId() + "               " + startEvent.getOutgoing()+"                 "+startEvent.getDescription());
				}
			}
		}
		// 确保一个合格的流程开始是至少有1个起点,不然执行啥
		Assert.isTrue(null != startEvent, "开始节点不存在，请检查XML文件是否正确!");
//		List<Edge> edgeList = startEvent.getOutgoing();
//		for(Edge edge : edgeList) {
//			String targetId =  edge.getTargetId();
//
//		}
//		createProcessNode(startEvent.getId(), startEvent);
//		createProcessNode(startEvent.getId(), "0", startEvent);
		// 执行是以点为主,点才是要执行的任务,边只是作为顺序+判断等用途
		ProcessInstance instance = NodeConvetorFatctory.getInstance(nodeMap, edgeMap).convetor(startEvent);
//		instance.addCandidate(startEvent);
		return instance;
	}

	private boolean isStartNode(Node node) {
		return ToolsKit.isEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing()) && Const.START_LABEL.equals(node.getLabel());
	}

	private boolean isShapeNode(Node node) {
		return node instanceof ShapeEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
	}

	private boolean isTaskNode(Node node) {
		return node instanceof TaskEvent && ToolsKit.isNotEmpty(node.getIncoming()) && ToolsKit.isNotEmpty(node.getOutgoing());
	}

	private boolean isEndNode(Node node) {
		return ToolsKit.isEmpty(node.getOutgoing()) && ToolsKit.isNotEmpty(node.getIncoming()) && Const.END_LABEL.equals(node.getLabel());
	}

	private void createProcessNode(String pid, String taskId, Node processNode) {
		List<Edge> outEdgeList = processNode.getOutgoing();
		for (Edge edge : outEdgeList) {
			String targetId = edge.getTargetId();
			Node node = nodeMap.get(targetId);
			if(isShapeNode(node)) {
				for(Iterator<Edge> iterator = node.getOutgoing().iterator(); iterator.hasNext();) {
					Edge e = iterator.next();
					String pid2 = e.getId();
					System.out.println(pid + " @@@@@@@@@: " + pid2+"                       "+taskId);
//					taskId +=","+pid2;
					createProcessNode(pid2, taskId, nodeMap.get(e.getTargetId()));
				}
			}
			else if(isTaskNode(node)) {
				taskId += "," + node.getId();
			}
//			else if(isEndNode(node)) {
//				System.out.println(taskId+"            "+edge.getLabel() + "                                           "+node.getLabel());
//				System.out.println("###############################################");
//			}
			createProcessNode(pid, taskId, node);

		}
	}

	private void createProcessNode2(String pid, String spid, Node processNode) {
		List<Edge> outEdgeList = processNode.getOutgoing();
		for(Edge edge : outEdgeList) {
			String targetId =  edge.getTargetId();
			Node node = nodeMap.get(targetId);
//			List<Edge> inEdgeList = node.getIncoming();
//			System.out.println("getSourceId: " + inEdgeList.get(0).getSourceId());
//			System.out.println(inEdgeList);

				// 如果是执行节点，即incoming与outgoing集合都不为空
			if(!isEndNode(node)) {
				// 如果是分支节点
				if(isShapeNode(node)) {
//					for(Iterator<Edge> iterator = node.getOutgoing().iterator(); iterator.hasNext();) {
//						Edge e = iterator.next();
////						System.out.println("###########: " + e.getId());
//
//						spid += "/" + e.getId();
//					}
					String id = node.getOutgoing().get(0).getId();
					spid += "/" +id;
				} else if(isTaskNode(node)) {
					pid += "," + node.getId();
				}
				createProcessNode(pid, spid, node);
			} else if(isEndNode(node)) {
				System.out.println(pid+"            "+spid+"            "+edge.getLabel() + "                                           "+node.getLabel());
				System.out.println("###############################################");
			}



		}
	}

}
