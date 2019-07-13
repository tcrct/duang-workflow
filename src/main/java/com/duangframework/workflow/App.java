package com.duangframework.workflow;

import com.duangframework.workflow.core.DuangEngine;
import com.duangframework.workflow.core.ProcessDefinition;
import com.duangframework.workflow.core.ProcessInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

public class App {

    public static void main(String[] args) {
        String path = "C:\\workspace\\java\\duang-workflow\\src\\main\\resources\\demo.xml";
//        System.out.println(path);
        try {
            String xmlDoc = FileUtils.readFileToString(new File(path));
            DuangEngine engine = new DuangEngine();
            // 解释XML并返回流程定义对象
            ProcessDefinition processDefinition = engine.parse(xmlDoc);
            // 发布流程，得到进程实例
            ProcessInstance processInstance = engine.deploy(processDefinition);
            // 运行进程实例
//            WorkFlow workFlow = engine.execute(processInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
