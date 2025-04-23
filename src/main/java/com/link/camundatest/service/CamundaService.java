package com.link.camundatest.service;

import com.link.camundatest.adapter.CamundaTopicDealAdapter;
import com.link.camundatest.config.CamundaConfig;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Camunda 处理 Service
 */
@Service
@Slf4j
public class CamundaService {

    private final RuntimeService runtimeService;
    private final ExternalTaskService externalTaskService;
    private final CamundaConfig camundaConfig;
    private final Map<String, CamundaTopicDealAdapter> topicDealAdapterMap;

    public CamundaService(RuntimeService runtimeService,
                          ExternalTaskService externalTaskService,
                          CamundaConfig camundaConfig,
                          List<CamundaTopicDealAdapter> camundaTopicDealAdapters) {
        this.runtimeService = runtimeService;
        this.externalTaskService = externalTaskService;
        this.camundaConfig = camundaConfig;
        //注入所有的适配器
        this.topicDealAdapterMap = camundaTopicDealAdapters
                .stream()
                .collect(Collectors.toMap(CamundaTopicDealAdapter::getTopic, Function.identity()));
    }

    /**
     * 通过流程定义键启动一个新的流程实例
     *
     * @param processKey 流程定义的key，与BPMN文件中定义的process id对应
     * @return 返回创建的流程实例对象
     */
    public ProcessInstance startProcessByKey(String processKey) {
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    /**
     * 暂停指定的流程实例
     * 暂停后，该流程实例将无法继续执行，直到被重新激活
     *
     * @param processInstanceId 需要暂停的流程实例ID
     */
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 删除指定的流程实例
     * 删除后，该流程实例及其所有相关数据将被永久移除
     *
     * @param processInstanceId 需要删除的流程实例ID
     * @param deleteReason      删除原因，用于记录删除操作的说明
     */
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    /**
     * Camunda 外部任务处理
     */
    public void handleExternalTask() {

        //1. fetch
        List<LockedExternalTask> externalTaskList = fetchTask();
        for (LockedExternalTask task : externalTaskList) {
            try {
                // 执行业务逻辑
                topicDealAdapterMap.get(task.getTopicName()).deal(task);
            } catch (Exception e) {
                log.error("处理外部任务失败", e);
                // 报告失败
                externalTaskService.handleFailure(
                        task.getId(),
                        camundaConfig.getWorkerId(),
                        e.getMessage(),
                        0,  // 重试次数
                        10000L  // 重试超时时间
                );
            }
        }
    }

    /**
     * fetchTask并锁定任务
     * @return fetch到的任务
     */
    private List<LockedExternalTask> fetchTask() {

        //1. fetchTask并锁定任务
        ExternalTaskQueryBuilder externalTaskQuery = externalTaskService.fetchAndLock(camundaConfig.getFetchMaxTaskNum(), camundaConfig.getWorkerId());

        //1.1 设置fetch的topic和其他参数
        for (String topic : topicDealAdapterMap.keySet()) {
            externalTaskQuery = externalTaskQuery
                    .topic(topic, camundaConfig.getLockTime())
                    .tenantIdIn(camundaConfig.getTenantId());
        }

        //1.2 fetch
        List<LockedExternalTask> externalTaskList = externalTaskQuery.execute();
        log.info("获取到 {} 个外部任务", externalTaskList.size());
        return externalTaskList;
    }

}
