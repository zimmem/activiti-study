package com.zimmem.activiti.study;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @author zhaowen.zhuang
 * @date 2014年10月16日 下午3:28:37
 *
 */
public class LoopProcess {
  public static void main(String[] args) {
    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService = engine.getRepositoryService();
    repositoryService.createDeployment().addClasspathResource("flow.definition/loop.bpmn")
        .deploy();
    System.out.println("Number of process definitions: "
        + repositoryService.createProcessDefinitionQuery().count());


    RuntimeService runtimeService = engine.getRuntimeService();
    
    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("i", 0);
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey("loopProcess", vars);
    
    System.out.println(processInstance.isEnded());

  }
}
