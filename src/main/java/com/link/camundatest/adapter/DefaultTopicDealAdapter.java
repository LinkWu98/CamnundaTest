package com.link.camundatest.adapter;

import cn.hutool.json.JSONUtil;
import com.link.camundatest.config.CamundaConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class DefaultTopicDealAdapter implements CamundaTopicDealAdapter {

    @Autowired
    ExternalTaskService externalTaskService;

    @Autowired
    CamundaConfig camundaConfig;

    private String topic = "";

    @Override
    public void deal(LockedExternalTask task) {
        log.info("dealing external task:{}", JSONUtil.toJsonStr(task));
    }

}
