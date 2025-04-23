package com.link.camundatest.controller;

import com.link.camundatest.service.CamundaService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/camunda")
public class CamundaController {

    @Autowired
    CamundaService camundaService;

    @PostMapping("/process/{processKey}/start")
    public String startProcess(@PathVariable String processKey) {
        ProcessInstance processInstance = camundaService.startProcessByKey(processKey);
        return processInstance.getProcessInstanceId();
    }

    @PostMapping("/process/{processInstanceId}/suspend")
    public void suspendProcess(@PathVariable String processInstanceId) {
        camundaService.suspendProcessInstance(processInstanceId);
    }

    @DeleteMapping("/process/{processInstanceId}")
    public void deleteProcess(@PathVariable String processInstanceId) {
        camundaService.deleteProcessInstance(processInstanceId, "手动删除流程实例");
    }

}
