package com.link.camundatest.adapter;

import org.camunda.bpm.engine.externaltask.LockedExternalTask;

import java.util.Map;

public interface CamundaTopicDealAdapter {

    /**
     * 获取当前适配器对应的topic
     * @return
     */
    String getTopic();

    /**
     * 处理camunda task
     * @param task
     */
    void deal(LockedExternalTask task);

}

