package com.link.camundatest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Schema(description = "启动流程请求DTO")
@Data
public class StartProcessReqDTO {

    @Schema(description = "流程定义key", example = "payment-process", required = true)
    private String processKey;

    @Schema(description = "业务标识", example = "ORDER_12345")
    private String businessKey;

    @Schema(description = "流程变量", example = "{\"amount\": 100}")
    private Map<String, Object> variables;

}
