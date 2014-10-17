package com.zimmem.activiti.study;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.util.FileCopyUtils;

/**
 * @author zhaowen.zhuang
 * @date 2014年10月17日 下午1:57:18
 *
 */
public class FlowImageProcess {

  static ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
  static RepositoryService repositoryService = engine.getRepositoryService();
  static RuntimeService runtimeService = engine.getRuntimeService();
  private static TaskService taskService = engine.getTaskService();

  public static void main(String[] args) throws Exception {

    Deployment deploy =
        repositoryService.createDeployment()
            .addClasspathResource("flow.definition/flow-image.bpmn").deploy();
    System.out.println("Number of process definitions: "
        + repositoryService.createProcessDefinitionQuery().count());


    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("i", 0);
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey("flowImageProcess", vars);
    viewImage(repositoryService, deploy.getId());
    viewCurrentImage(processInstance.getProcessDefinitionId(), processInstance.getId(), "e:\\current0.png");
    
    Task task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
    taskService.complete(task.getId());
    viewCurrentImage(processInstance.getProcessDefinitionId(), processInstance.getId(), "e:\\current1.png");
  }


  public static void viewImage(RepositoryService repositoryService, String deploymentId)
      throws Exception {
    // 从仓库中找需要展示的文件
    List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
    String imageName = null;
    for (String name : names) {
      if (name.indexOf(".png") >= 0) {
        imageName = name;
      }
    }
    if (imageName != null) {
      InputStream in = repositoryService.getResourceAsStream(deploymentId, imageName);
      FileOutputStream out = new FileOutputStream("e:\\image.png");
      byte[] bytes = new byte[4096];
      while (in.read(bytes) > 0) {
        out.write(bytes);
      }
      out.close();
    }
  }

  public static void viewCurrentImage(String processDefinitionId, String processInstanceId, String file) throws Exception {
   
    BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

    // 得到正在执行的Activity的Id
    List<String> activityIds =runtimeService.getActiveActivityIds(processInstanceId);
    InputStream in = engine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activityIds);
    FileOutputStream out = new FileOutputStream(file);
    FileCopyUtils.copy(in, out);
  }
}
