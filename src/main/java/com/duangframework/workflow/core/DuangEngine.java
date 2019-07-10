package com.duangframework.workflow.core;

import com.duangframework.workflow.service.XMLParserService;

import java.util.List;

/**
 * 工作流引擎
 * @author laotang
 */
public class DuangEngine implements Engine {

    private lParserService iParserService;
    private IFlowAlgorithmService algorithmService;

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
        return processInstances;
    }

    @Override
    public <T> T execute(List<ProcessInstance> processInstance) throws Exception {
        if(null == algorithmService) {
            throw new NullPointerException("algorithmService is null");
        }
        return (T)algorithmService.execute(processInstance);
    }

}
