package com.link.camundatest.adapter;

import com.link.camundatest.config.CamundaConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Data
public class ChargeCardDealAdapter implements CamundaTopicDealAdapter {

    @Autowired
    ExternalTaskService externalTaskService;

    @Autowired
    CamundaConfig camundaConfig;

    @Value("${link.camunda.topic.chargeCard}")
    private String topic;

    @Override
    public void deal(LockedExternalTask task) {


        Map<String, Object> completeVariables = new HashMap<>();
        // 完成任务
        externalTaskService.complete(task.getId(), camundaConfig.getWorkerId(), completeVariables);
    }
}
