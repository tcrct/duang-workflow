package com.duangframework.workflow.core;

import com.duangframework.workflow.service.XMLParserService;

public class DuangEngine implements Engine {

    private lParserService iParserService;

    public DuangEngine() {
        iParserService = new XMLParserService();
    }

    /**
     *
     * @param resource
     * @return
     * @throws Exception
     */
    @Override
    public ProcessDefinition parse(String resource) throws Exception {
        return iParserService.parse(resource.trim()
                .replace("\n","")
                .replace("\r","")
                .replace("\t",""));
    }
}
