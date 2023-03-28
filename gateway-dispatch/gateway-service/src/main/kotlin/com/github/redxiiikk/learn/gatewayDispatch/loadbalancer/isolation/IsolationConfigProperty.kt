package com.github.redxiiikk.learn.gatewayDispatch.loadbalancer.isolation

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.cloud.gateway.loadbalancer.isolation")
data class IsolationConfigProperty(
    var enable: Boolean = true,
    var isolationHeaderKey: String = "X-ISOLATION",
    var isolationMetadataKey: String = "ISOLATION",
    var baselineEnvName: String = "baseline"
)
