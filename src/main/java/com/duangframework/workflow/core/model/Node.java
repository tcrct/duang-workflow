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

    protected void copy(Object obj) {

//        super.copy(obj);

        Node node = (Node) obj;

        // 不拷贝具体的出边入边,明确设置为空
        node.incoming = new ArrayList<Edge>();
        node.outgoing = new ArrayList<Edge>();

    }
/*
    public void ignore(ProcessInstance instance) {

        setState(State.IGNORED);
        LOGGER.debug("node [{}] -> ignored", this.getId());

        // 开始扭转边的状态,这些边应该都是NEW的状态
        List<Edge> edges = outgoing;

        if (null == edges) {
            return;
        }

        // 如果有出边,出边一律设置为IGNORED
        for (Edge edge : edges) {

            Assert.isTrue(State.NEW == edge.getState(),
                "wrong edge state of " + this.getId() + " " + edge.getId());
            edge.ignore(instance);

        }

    }
*/
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