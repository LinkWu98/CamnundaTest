package com.link.camundatest.config;

import lombok.Data;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    private String camundaRestUrl;

    private String username;

    private String password;

    @Bean
    public ExternalTaskClient processEngineClient() {
        return ExternalTaskClient.create()
                .baseUrl(camundaRestUrl)
                .asyncResponseTimeout(10000)
                .build();
    }

}
