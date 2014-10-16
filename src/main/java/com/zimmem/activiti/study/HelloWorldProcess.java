package com.zimmem.activiti.study;

import java.util.HashMap;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @author zhaowen.zhuang
 * @date 2014年10月16日 下午2:43:16
 *
 */
public class HelloWorldProcess {
  public static void main(String[] args) {
    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService = engine.getRepositoryService();
    repositoryService.createDeployment().addClasspathResource("flow.definition/helloworld.bpmn").deploy();
    System.out.println("Number of process definitions: "
        + repositoryService.createProcessDefinitionQuery().count());


    RuntimeService runtimeService = engine.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloworld", new HashMap());
    System.out.println(processInstance.isEnded());
    
  }
}
