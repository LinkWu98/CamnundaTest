package com.link.camundatest.scheduling;

import com.link.camundatest.service.CamundaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CamundaExternalTaskWorker {



    @Autowired
    private CamundaService camundaService;
    
    @Scheduled(fixedRate = 1000)
    public void execute() {
        camundaService.handleExternalTask("myTopic", WORKER_ID);
    }
}