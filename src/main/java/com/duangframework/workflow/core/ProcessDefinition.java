package com.duangframework.workflow.core;

import com.duangframework.kit.ToolsKit;
import com.duangframework.utils.Assert;
import com.duangframework.workflow.core.model.Action;
import com.duangframework.workflow.core.model.BaseElement;
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
    private List<ProcessInstance2> processInstanceList2;

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
        processInstanceList2 = new ArrayList<>();
//		 上面已经保证了每个边都用到了,所以只要判断是不是每个点都被使用了
//		 顺便发现startEvent而且必须唯一
		StartEvent startEvent = null;
//        Action action = null;
		Collection<Node> nodes = nodeMap.values();
		for (Node node : nodes) {
			Assert.isTrue(node.isConnected(), "节点不存在，请检查XML文件 , id is ->" + node.getId()+", label is ->"+node.getDescription());
			// 开启或结束节点
			if (node instanceof StartEvent) {
				if(isStartNode(node)) {
					Assert.isTrue(null == startEvent, "开始节点只能有1个!");
					startEvent = (StartEvent) node;
//                    action = new Action(new EdgeEvent(),node);
				}
			}
		}
		// 确保一个合格的流程开始是至少有1个起点,不然执行啥
		Assert.isTrue(null != startEvent, "开始节点不存在，请检查XML文件是否正确!");
		/*
         List<String> nodeIdList = new ArrayList<>();
        nodeIdList.add(startEvent.getId());
        // 递归取出所有审批线路，如果有分支节点，则以分支节点作为key
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<>();
		createProcessNode2(startEvent, conditionMap, nodeIdList);
		*/
//        List<Action> actionList = new ArrayList<>();
        List<BaseElement> actionList = new ArrayList<>();
        actionList.add(startEvent);
//         递归取出所有审批线路，如果有分支节点，则以分支节点作为key
        createProcessNode(startEvent, actionList);
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

    /**
     * 取出每一个节点的出边线，递归方式找出每条线路所经过的所有节点
     * @param processNode   进程节点
     * @param actionList
     */
//    private static List<BaseElement> baseElementList = new ArrayList<>();
    private void createProcessNode(Node processNode, List<BaseElement> actionList) {
        List<Edge> outEdgeList = processNode.getOutgoing();
        for (Edge edge : outEdgeList) {
            String targetId = edge.getTargetId();
            // 连接线节点
            Edge edgeNode = edgeMap.get(edge.getId());
            // 目标节点
            Node node = nodeMap.get(targetId);
            Assert.isTrue(null != node, "根据目标节点ID[" + targetId + "]找不到对应的节点，请检查XML文件是否正确!");
//            Action action = new Action(edgeNode, node);

            // 如果遇到条件分支节点，则复制线路，再进行递归
            if (isRhombusNode(node)) {
                List<Edge> outGoingEdgeList = node.getOutgoing();
                Assert.isTrue(null != outGoingEdgeList, "条件分支节点[" + node.getId() + "]没有出边线，请检查XML文件是否正确!");
                actionList.add(edgeNode);
                actionList.add(node);
                List<BaseElement> baseElementList = new ArrayList<>(actionList);
                for (Edge e : outGoingEdgeList) {
                    List<BaseElement> copyActionList = new ArrayList(baseElementList);
                    Edge outGoEdgeNode = edgeMap.get(e.getId());
                    copyActionList.add(outGoEdgeNode);
                    Node outGoingNode = nodeMap.get(e.getTargetId());
                    copyActionList.add(outGoingNode);
//                    System.out.println(outGoEdgeNode.getId()+"                                "+outGoEdgeNode.getName()+"                "+ outGoingNode.getId()+"   "+ outGoingNode.getName() +"               "+node.getId());
//                    Action outGoingAction = new Action(outGoEdgeNode, outGoingNode);
//                    copyActionList2.add(outGoingAction);
                    createProcessNode(outGoingNode, copyActionList);
                }
            } else if (isTaskNode(node) || isCcNode(node)) {
                actionList.add(edgeNode);
                actionList.add(node);
                createProcessNode(node, actionList);
            } else if (isEndNode(node)) {
                actionList.add(edgeNode);
                actionList.add(node);
                ProcessInstance instance = new ProcessInstance(actionList);
                for(BaseElement action1 : instance.getActionList()) {
                    System.out.print(action1.getName()+"("+action1.getId()+"), ");
                }
                System.out.println(" ");
//                System.out.println("  #########:  " + instance);
                processInstanceList.add(instance);
            }
        }
    }

	private void createProcessNode2(Node processNode, LinkedHashMap<String, String> conditionMap,  List<String> nodeIdList) {
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
					ProcessInstance2 instance = new ProcessInstance2(conditionMap, nodeIdList);
//					System.out.println("  #########:  " + instance);
					processInstanceList2.add(instance);
				}
			}
		} else {
			for (Edge edge : outEdgeList) {
				String targetId = edge.getTargetId();
				Node node = nodeMap.get(targetId);
//				String nodeId = node.getId();
                String nodeId = node.getLabel();
                List<String> copyNodeIdList = new ArrayList(nodeIdList);
                LinkedHashMap<String, String> copyConditionMap = new LinkedHashMap<>(conditionMap);
				if (isRhombusNode(node)) {
					for (Iterator<Edge> iterator = node.getOutgoing().iterator(); iterator.hasNext(); ) {
						Edge e = iterator.next();
						List<String> copyNodeIdList2 = new ArrayList(nodeIdList);
						LinkedHashMap<String, String> copyConditionMap2 = new LinkedHashMap<>(conditionMap);
						Node itemNode = nodeMap.get(e.getTargetId());
//						copyNodeIdList.add(itemNode.getId());
//                        copyConditionMap.put(node.getId(), e.getId());
                        copyNodeIdList2.add(itemNode.getLabel());
                        copyConditionMap2.put(nodeId, e.getLabel());
						createProcessNode2(itemNode, copyConditionMap2, copyNodeIdList2);
					}
				} else if (isTaskNode(node) || isCcNode(node)) {
					if (isCcNode(node)) {
						nodeId = NodeEventEnum.CC.name().toLowerCase() + "_" + nodeId;
					}
                    copyNodeIdList.add(nodeId);
					createProcessNode2(node, copyConditionMap, copyNodeIdList);
				} else if (isEndNode(node)) {
                    copyNodeIdList.add(nodeId);
					ProcessInstance2 instance = new ProcessInstance2(copyConditionMap, copyNodeIdList);
					System.out.println("  #########:  " + instance);
					processInstanceList2.add(instance);
				}
			}
		}
	}
}
