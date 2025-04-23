package com.link.camundatest.adapter;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.link.camundatest.config.CamundaConfig;
import com.link.camundatest.consts.CamundaConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@Data
public class CheckCardDealAdapter implements CamundaTopicDealAdapter {

    @Autowired
    ExternalTaskService externalTaskService;

    @Autowired
    CamundaConfig camundaConfig;

    @Value("${link.camunda.topic.checkCard}")
    private String topic;

    /**
     * 查看余额是否充足
     * @param task
     */
    @Override
    public void deal(LockedExternalTask task) {
        log.info("CheckCardDealAdapter::deal, task:{}", JSONUtil.toJsonStr(task));

        //Camunda 节点参数
        Map<String, String> extensionProperties = task.getExtensionProperties();
        String checkAmountStr = extensionProperties.getOrDefault(CamundaConstants.CHECK_AMOUNT, BigDecimal.ZERO.toString());
        BigDecimal checkAmount = new BigDecimal(checkAmountStr);

        //完成参数
        Map<String, Object> completeVariables = new HashMap<>();

        //1. 获取余额
        VariableMap variables = task.getVariables();
        BigDecimal amount = Optional.ofNullable(variables.getValue(CamundaConstants.CHECK_CARD_VARIABLE_KEY, BigDecimal.class))
                .orElse(BigDecimal.ZERO);

        //2. 比较 amount 是否大于等于 checkAmount
        boolean isEnough = NumberUtil.isGreaterOrEqual(amount, checkAmount);
        completeVariables.put(CamundaConstants.AMOUNT_WHETHER_ENOUGH, isEnough);

        // 完成任务
        externalTaskService.complete(task.getId(), camundaConfig.getWorkerId(), completeVariables);
    }
}
