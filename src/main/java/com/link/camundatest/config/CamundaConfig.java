package com.link.camundatest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("link.camunda")
@Data
public class CamundaConfig {

    private String workerId;

    /**
     * 一次fetch最大任务数量
     */
    private int fetchMaxTaskNum;

    /**
     * 一次fetch任务锁定时间
     */
    private long lockTime;

    /**
     * 租户id
     */
    private String tenantId;

}
