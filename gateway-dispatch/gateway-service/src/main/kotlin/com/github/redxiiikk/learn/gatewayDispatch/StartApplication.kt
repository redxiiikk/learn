package com.github.redxiiikk.learn.gatewayDispatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableDiscoveryClient
@SpringBootApplication
open class StartApplication

fun main(args: Array<String>) {
    runApplication<StartApplication>(*args)
}

@Configuration(proxyBeanMethods = false)
open class LoadBalancerConfig {
    @Bean
    @ConditionalOnBean(ReactiveDiscoveryClient::class)
    open fun discoveryClientServiceInstanceListSupplier(context: ConfigurableApplicationContext): ServiceInstanceListSupplier =
        ServiceInstanceListSupplier.builder()
            .withDiscoveryClient()
            .withHints()
            .withCaching()
            .build(context)
}
