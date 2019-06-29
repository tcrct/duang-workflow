package com.duangframework.workflow.core.model;


import java.util.ArrayList;
import java.util.List;

public abstract class Node extends BaseElement {

    /**
     * 入节点的链接线边
     * */
    private List<Edge> incoming = new ArrayList<>();

    /**
     * 出节点的链接线边
     */
    private List<Edge> outgoing = new ArrayList<>();

    public List<Edge> getIncoming() {
        return incoming;
    }

    public List<Edge> getOutgoing() {
        return outgoing;
    }

    public void parse(org.w3c.dom.Node Node) throws Exception {
        super.parse(Node);
    }

    /**
     * 默认执行算法,特殊的需要覆盖
     * 
     * @throws Exception
     */
//    @Override
//    public void invoke(ProcessInstance instance,
//                       ApplicationContext applicationContext) throws Exception {
//
//        setState(State.INVOKED);
//        LOGGER.debug("node [{}] -> invoked", getId());
//
//        // 再处理各个边
//        List<Edge> edges = this.getOutgoing();
//
//        if (null == edges) {
//            return;
//        }
//
//        for (Edge edge : edges) {
//
//            if (edge instanceof SequenceFlow) {
//                // 这个内部会设置边的状态是invoked还是ignored
//                // 主要看表达式是否存在以及是否计算结果为true|false
//                edge.invoke(instance, applicationContext);
//            } else {
//                LOGGER.warn("unknown edge id is {}", edge.getId());
//            }
//
//        }
//
//    }

}