package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.*;
import com.duangframework.workflow.event.*;
import com.duangframework.workflow.utils.Assert;
import com.duangframework.workflow.utils.Const;
import com.duangframework.workflow.utils.WorkflowUtils;

import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author laotang
 */
public class ProcessDefinition {

	private Map<String, Node> nodeMap;

	private Map<String, Edge> edgeMap;

	private List<List<Action>> PROCESS_ACTION_LIST;
	/**审批条件节点集合，key为父线条的节点，value为该父节点线条所拥有的下一级线条节点*/
	private static List<ActionEdge> EDGE_ACTION_LIST;
	/**审批或抄送人节点集合，key为条件节点的ID总和，用于区分线，value为审批或抄数人节点*/
	private static  List<ActionNode> TASK_CC_ACTION_LIST;

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

	public  ProcessInstance deploy() throws Exception {
		PROCESS_ACTION_LIST = new ArrayList<>();
		EDGE_ACTION_LIST = new ArrayList<>();
		TASK_CC_ACTION_LIST = new ArrayList<>();
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
				}
			}
		}
		// 确保一个合格的流程开始是至少有1个起点,不然执行啥
		Assert.isTrue(null != startEvent, "开始节点不存在，请检查XML文件是否正确!");
//        List<Action> actionList = new ArrayList<>();
        List<BaseElement> actionList = new ArrayList<>();
        actionList.add(startEvent);
		List<String> flowNodeIdList = new ArrayList<>();
		flowNodeIdList.add(startEvent.getId());
		Map<String, List<String>> edgeMap = new HashMap<>();
//         递归取出所有审批线路，如果有分支节点，则以分支节点作为key
        createProcessNode(startEvent, actionList);
		getConditionMap(PROCESS_ACTION_LIST);
		ProcessInstance instance = new ProcessInstance(PROCESS_ACTION_LIST, EDGE_ACTION_LIST, TASK_CC_ACTION_LIST);
//		System.out.println(EDGE_ACTION_LIST);
		return instance;
	}

	private boolean isStartNode(Node node) {
		return node instanceof StartEvent && WorkflowUtils.isEmpty(node.getIncoming()) && WorkflowUtils.isNotEmpty(node.getOutgoing()) && Const.START_LABEL.equals(node.getLabel());
	}

	private boolean isRhombusNode(Node node) {
		return node instanceof RhombusEvent && WorkflowUtils.isNotEmpty(node.getIncoming()) && WorkflowUtils.isNotEmpty(node.getOutgoing());
	}

	private boolean isTaskNode(Node node) {
		return node instanceof TaskEvent && WorkflowUtils.isNotEmpty(node.getIncoming()) && WorkflowUtils.isNotEmpty(node.getOutgoing());
	}

	private boolean isCcNode(Node node) {
		return node instanceof CcEvent && WorkflowUtils.isNotEmpty(node.getIncoming()) && WorkflowUtils.isNotEmpty(node.getOutgoing());
	}

	private boolean isEndNode(Node node) {
		return  node instanceof EndEvent && WorkflowUtils.isEmpty(node.getOutgoing()) && WorkflowUtils.isNotEmpty(node.getIncoming()) && Const.END_LABEL.equals(node.getLabel());
	}

    /**
     * 取出每一个节点的出边线，递归方式找出每条线路所经过的所有节点
     * @param processNode   进程节点
     * @param actionList
     */
    private void createProcessNode(Node processNode, List<BaseElement> actionList) {
        List<Edge> outEdgeList = processNode.getOutgoing();
        if(WorkflowUtils.isEmpty(outEdgeList) && isEndNode(processNode)) {
			add2ActionList(actionList);
			return;
		}
		for (Edge edge : outEdgeList) {
			String targetId = edge.getTargetId();
			// 连接线节点
			Edge edgeNode = edgeMap.get(edge.getId());
			// 目标节点
			Node node = nodeMap.get(targetId);
			Assert.isTrue(null != node, "根据目标节点ID[" + targetId + "]找不到对应的节点，请检查XML文件是否正确!");
			// 添加线路所经过的所有节点
			actionList.add(edgeNode);
			actionList.add(node);
			// 如果遇到条件分支节点，则复制线路，再进行递归
			if (isRhombusNode(node)) {
				List<Edge> outGoingEdgeList = node.getOutgoing();
				Assert.isTrue(null != outGoingEdgeList, "条件分支节点[" + node.getId() + "]没有出边线，请检查XML文件是否正确!");
				for (Edge e : outGoingEdgeList) {
					Edge outGoEdgeNode = edgeMap.get(e.getId());
					actionList.add(outGoEdgeNode);
					Node outGoingNode = nodeMap.get(e.getTargetId());
					actionList.add(outGoingNode);
					createProcessNode(outGoingNode, actionList);
					// 递归退出时，删除掉添加的节点，让集合回退到开始进行递归时的状态
					actionList.remove(outGoingNode);
					actionList.remove(outGoEdgeNode);
				}
				// 如果审核人节点或抄送人节点，则继续递归下一节点
			} else if (isTaskNode(node) || isCcNode(node)) {
				createProcessNode(node, actionList);
				// 如果结事节点，则转换内容
			} else if (isEndNode(node)) {
				add2ActionList(actionList);
			}
			// 递归退出时，删除掉添加的节点，让集合回退到开始进行递归时的状态
			actionList.remove(node);
			actionList.remove(edgeNode);
		}
    }

	/**
	 * 添加到集合里
	 * @param actionList
	 */
	private void add2ActionList(List<BaseElement> actionList) {
		List<Action> actions = new ArrayList(actionList.size());
		for (BaseElement element : actionList) {
			System.out.print(element.getName() + "(" + element.getId() + "-"+element.getCode()+"), ");
			actions.add(new Action(element.getId(), element.getLabel(), element.getDescription(), element.getName(), element.getCode()));
		}
		System.out.println(" ");
		PROCESS_ACTION_LIST.add(actions);
	}


    /** 生成条件节点与任务节点集合 */
	private void getConditionMap(List<List<Action>> instanceList) {
		LinkedHashMap<String, LinkedHashSet<String>> conditionMap = new LinkedHashMap<>();
		LinkedHashMap<String, LinkedHashSet<String>> taksCcNodeMap = new LinkedHashMap<>();
        StringBuilder conditionCode = new StringBuilder();
		for(List<Action> actionList : instanceList) {
			int size = actionList.size();
			String pid = "0";
            conditionCode.delete(0, conditionCode.length());
			LinkedHashSet<String> taskCcNodeSet = new LinkedHashSet<>();
			for (int i = 0; i < size; i++) {
				Action action = actionList.get(i);
				String actionType = action.getType();
				// 条件节点
				if (Const.RHOMBUS_NODE_NAME.equals(actionType)) {
					Action edgeAction = actionList.get(i + 1);
					String edgeId = "";
					if (Const.EDGE_NODE_NAME.equals(edgeAction.getType()) && WorkflowUtils.isNotEmpty(edgeAction.getLabel())) {
						edgeId = edgeAction.getId();
						conditionCode.append(edgeAction.getCode());
					}
					LinkedHashSet<String> set = conditionMap.get(pid);
					if (WorkflowUtils.isEmpty(set)) {
						set = new LinkedHashSet<>();
					}
					set.add(edgeId);
					conditionMap.put(pid, set);
					pid = edgeId;
				}
				//审批或抄送人节点
				if(Const.TASK_NODE_NAME.equals(actionType) || Const.CC_NODE_NAME.equals(actionType)) {
					taskCcNodeSet.add(action.getId());
				}
			}
//			taksCcNodeMap.put(WorkflowUtils.getRandomStr(6)+"_"+conditionCode, taskCcNodeSet);
			taksCcNodeMap.put(""+conditionCode, taskCcNodeSet);
		}

		System.out.println("############################## 条件树型节点 #############################################");
		conditionMap.entrySet().iterator().forEachRemaining(new Consumer<Map.Entry<String, LinkedHashSet<String>>>() {
			@Override
			public void accept(Map.Entry<String, LinkedHashSet<String>> entry) {
				String key = entry.getKey();
				BaseElement perantEdge = null;
				if("0".equals(key)) {
					perantEdge = new EdgeEvent();
					perantEdge.setId("0");
				} else {
					perantEdge = edgeMap.get(key);
				}
				Action parentAction = new Action(perantEdge.getId(), perantEdge.getLabel(), perantEdge.getDescription(), perantEdge.getName(), perantEdge.getCode());
				List<Action> edgeActionList = new ArrayList<>();
				entry.getValue().iterator().forEachRemaining(new Consumer<String>() {
					@Override
					public void accept(String key) {
						BaseElement subEdge = edgeMap.get(key);
						edgeActionList.add(new Action(subEdge.getId(), subEdge.getLabel(), subEdge.getDescription(), subEdge.getName(), subEdge.getCode()));
					}
				});
				ActionEdge actionEdge = new ActionEdge();
				actionEdge.setParentEdge(parentAction);
				actionEdge.setSubEdgeList(edgeActionList);
				EDGE_ACTION_LIST.add(actionEdge);
				System.out.print(actionEdge.getParentEdge().getId()+"              ");
				actionEdge.getSubEdgeList().iterator().forEachRemaining(new Consumer<Action>() {
					@Override
					public void accept(Action action) {
						System.out.print(action.getId()+",");
					}
				});
				System.out.println(" ");
			}
		});
		System.out.println("################################# 审批人或抄送节点 ##########################################");
		taksCcNodeMap.entrySet().iterator().forEachRemaining(new Consumer<Map.Entry<String, LinkedHashSet<String>>>() {
			@Override
			public void accept(Map.Entry<String, LinkedHashSet<String>> entry) {
				List<Action> taskCcNodeList = new ArrayList<>();
				String key = entry.getKey();
				System.out.print(key+"            ");
				entry.getValue().iterator().forEachRemaining(new Consumer<String>() {
					@Override
					public void accept(String value) {
						BaseElement node = nodeMap.get(value);
						taskCcNodeList.add(new Action(node.getId(), node.getLabel(), node.getDescription(), node.getName(), node.getCode()));
						System.out.print(value+",");
					}
				});
				System.out.println(" ");
				TASK_CC_ACTION_LIST.add(new ActionNode(key, taskCcNodeList));
			}
		});
	}
}
