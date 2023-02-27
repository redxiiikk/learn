package com.github.redxiiikk.learn.gatewayDispatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients
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


@Configuration
@LoadBalancerClients
open class GatewayConfiguration {
    @Bean
    open fun envColorServiceInstanceListSupplier(context: ConfigurableApplicationContext): ServiceInstanceListSupplier {
        return ServiceInstanceListSupplier.builder()
            .withDiscoveryClient()
            .withCaching()
            .withHints()
            .build(context)
    }
}
