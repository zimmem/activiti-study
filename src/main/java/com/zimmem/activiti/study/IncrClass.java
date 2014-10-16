package com.zimmem.activiti.study;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author zhaowen.zhuang
 * @date 2014年10月16日 下午3:50:25
 *
 */
public class IncrClass implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
    double i = (Double) execution.getVariable("i");
    System.out.println("i in java : " + i);
    execution.setVariable("i", i + 1);
  }

}
