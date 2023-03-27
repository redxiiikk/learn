package com.github.redxiiikk.learn.gatewayDispatch.loadbalancer.isolation

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.cloud.gateway.loadbalancer.isolation")
open class IsolationConfigProperty {
    var enable = true
    var isolationHeaderKey = "X-ISOLATION"
    var isolationMetadataKey = "ISOLATION"
    var baselineEnvName = "baseline"
}
