package com.zimmem.activiti.study;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;

/**
 * @author zhaowen.zhuang
 * @date 2014年10月17日 上午10:53:29
 *
 */
public class UserTaskProcess {
  public static void main(String[] args) throws InterruptedException {
    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService = engine.getRepositoryService();
    repositoryService.createDeployment().addClasspathResource("flow.definition/user-task.bpmn")
        .deploy();
    System.out.println("Number of process definitions: "
        + repositoryService.createProcessDefinitionQuery().count());


    RuntimeService runtimeService = engine.getRuntimeService();
    HistoryService historyService = engine.getHistoryService();

    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey("userTask", new HashMap());
    TaskService taskService = engine.getTaskService();
    List<Task> list = taskService.createTaskQuery().taskCandidateGroup("manager").list();

    Task task = list.get(0);

    taskService.claim(task.getId(), "Zimmem");
    
    Map<String, Object> obj = new HashMap<String, Object>();
    obj.put("name", "Zimmem");
    taskService.complete(task.getId(), obj);

    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
    System.out.println(historicProcessInstance.getEndTime());
  }
}
