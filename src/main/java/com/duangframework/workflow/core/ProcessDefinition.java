package com.duangframework.workflow.core;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import com.duangframework.workflow.core.model.Edge;
import com.duangframework.workflow.core.model.Node;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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

			if(ToolsKit.isEmpty(sourceNodeId) || ToolsKit.isEmpty(targetNodeId)) {
				continue;
			}

			Node sourceNode = nodeMap.get(sourceNodeId);
			Node targetNode = nodeMap.get(targetNodeId);

			Assert.isTrue(null != sourceNode, "find not node , not valid src node id -> " + sourceNodeId);
			Assert.isTrue(null != targetNode, "find not node , not valid target node id -> " + targetNodeId);

			// 现在我们已经知道这个边和这2个点都有关系

			// 完善源节点的信息点
			sourceNode.getOutgoing().add(edge);
//			sourceNode.setConnected(true);

			// 完善目标节点的信息点
			targetNode.getIncoming().add(edge);
//			targetNode.setConnected(true);

			// 完善edge的信息点
			edge.setSourceNode(sourceNode);
			edge.setTargetNode(targetNode);
//			edge.setConnected(true);

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

		for(Iterator<Map.Entry<String,Node>> iterator = nodeMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String,Node> entry = iterator.next();
			Node node = entry.getValue();
			System.out.println(entry.getKey()+"       "+node.getIncoming()+"            "+node.getOutgoing());
		}

		// 上面已经保证了每个边都用到了,所以只要判断是不是每个点都被使用了
		// 顺便发现startEvent,而且必须唯一
//		SymbolEvent startEvent = null;
//		Collection<Node> nodes = nodeMap.values();
//		for (Node node : nodes) {
//			Assert.isTrue(node.isConnected(), "node not used , check your xml , id is ->" + node.getId());
//			if (node instanceof SymbolEvent) {
//				Assert.isTrue(null == startEvent, "startEvent XML element must be only 1");
//				startEvent = (SymbolEvent) node;
//			}
//		}

	}
/*
	@SuppressWarnings("unused")
	public ProcessInstance deploy() throws Exception {

		// 唯一开始事件
		Node startEvent = null;

		// 深度拷贝点
		Collection<Node> nodes = nodeMap.values();
		Map<String, Node> copiedNodeMap = new HashMap<String, Node>();
		for (Node node : nodes) {

			Node copiedNode = (Node) node.copy();
			copiedNodeMap.put(copiedNode.getId(), copiedNode);

			// 保留碰到的startEvent,记住一个流程只能有1个StartEvent元素
			if (node instanceof SymbolEvent) {
				Assert.isTrue(null == startEvent,
						"only 1 start event xml element supported in Single Process Definition");
				startEvent = copiedNode;
			}

		}

		// 深度拷贝边
		Collection<Edge> edges = edgeMap.values();
		Map<String, Edge> copiedEdgeMap = new HashMap<String, Edge>();
		for (Edge edge : edges) {
			Edge copiedEdge = (Edge) edge.copy();
			copiedEdgeMap.put(copiedEdge.getId(), copiedEdge);
		}

		// 内部会把点与边进行关联
		// 注意:这里是使用拷贝后的点集合+边集合
		ProcessDefinition topology = new ProcessDefinition(copiedNodeMap, copiedEdgeMap);

		// 执行是以点为主,点才是要执行的任务,边只是作为顺序+判断等用途
		ProcessInstance instance = new ProcessInstance();
		// 确保一个合格的流程开始是至少有1个起点,不然执行啥
		Assert.isTrue(null != startEvent, "start event not exist,check your xml definition");
		instance.addCandidate(startEvent);
		return instance;
	}
*/
}
