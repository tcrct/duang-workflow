package com.duangframework.workflow.core;

import com.duangframework.workflow.core.model.Node;
import com.duangframework.workflow.entity.WorkFlow;
import com.duangframework.workflow.service.XMLParserService;

import java.util.List;
import java.util.Map;

/**
 * 工作流引擎
 * @author laotang
 */
public class DuangEngine implements Engine {

    private lParserService iParserService;
    private IFlowAlgorithmService algorithmService;
    private Map<String, Node> nodeMap;

    public DuangEngine() {
        this.iParserService = new XMLParserService();
    }

    public DuangEngine(IFlowAlgorithmService algorithmService) {
        this.iParserService = new XMLParserService();
        this.algorithmService = algorithmService;
    }

    public void setAlgorithmService(IFlowAlgorithmService algorithmService) {
        this.algorithmService = algorithmService;
    }

    /**
     *
     * @param resource
     * @return
     * @throws Exception
     */
    @Override
    public ProcessDefinition parse(String resource) throws Exception {
        return iParserService.parse(resource.trim());
    }

    @Override
    public List<ProcessInstance> deploy(ProcessDefinition processDefinition) throws Exception {
        List<ProcessInstance> processInstances =  iParserService.deploy(processDefinition);
        this.nodeMap = processDefinition.getNodeMap();
        return processInstances;
    }

    @Override
    public <T> T execute(List<ProcessInstance> processInstance) throws Exception {
        if(null == algorithmService) {
            throw new NullPointerException("algorithmService is null");
        }
        return (T)algorithmService.execute(nodeMap, processInstance);
    }

}
