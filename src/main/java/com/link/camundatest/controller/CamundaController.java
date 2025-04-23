package com.link.camundatest.controller;

import com.link.camundatest.common.Response;
import com.link.camundatest.model.StartProcessReqDTO;
import com.link.camundatest.service.CamundaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "流程管理", description = "流程相关的操作接口")
@RestController
@RequestMapping("/camunda")
public class CamundaController {

    @Autowired
    CamundaService camundaService;

    @Operation(summary = "启动流程", description = "根据流程定义key启动一个新的流程实例")
    @ApiResponse(responseCode = "200", description = "流程启动成功")
    @PostMapping("/process/start")
    public Response<?> startProcess(@RequestBody @Parameter(description = "启动流程请求参数")  StartProcessReqDTO startProcessReqDTO) {
        ProcessInstance processInstance = camundaService.startProcess(startProcessReqDTO);
        return Response.success(processInstance);
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
