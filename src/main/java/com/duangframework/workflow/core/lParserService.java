package com.duangframework.workflow.core;

import java.util.List;

/**
 * @author laotang
 * @date 2019-6-8
 */
public interface lParserService {

    /**
     *  将xml字符串解析为对象
     * @param xml      XML格式的字符串
     * @return  ProcessDefinition
     * @throws Exception
     */
    ProcessDefinition parse(String xml) throws Exception;

    /**
     * 发布
     * @param processDefinition
     * @return
     * @throws Exception
     */
    ProcessInstance deploy(ProcessDefinition processDefinition) throws Exception;

}
