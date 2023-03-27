package com.github.redxiiikk.learn.gatewayDispatch.loadbalancer

import com.github.redxiiikk.learn.gatewayDispatch.loadbalancer.isolation.IsolationConfigProperty
import com.github.redxiiikk.learn.gatewayDispatch.loadbalancer.isolation.IsolationServiceInstanceListSupplier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplierBuilder.DelegateCreator
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@LoadBalancerClients(defaultConfiguration = [LoadBalancerConfig::class])
open class LoadBalancerClientsConfig {
    @Bean
    open fun isolationConfig() = IsolationConfigProperty()
}

open class LoadBalancerConfig {
    @Bean
    @ConditionalOnBean(ReactiveDiscoveryClient::class)
    open fun discoveryClientServiceInstanceListSupplier(
        context: ConfigurableApplicationContext,
        isolationConfigProperty: IsolationConfigProperty
    ): ServiceInstanceListSupplier =
        ServiceInstanceListSupplier.builder()
            .withDiscoveryClient()
            .with(create(isolationConfigProperty))
            .build(context)

    private fun create(isolationConfigProperty: IsolationConfigProperty) =
        DelegateCreator { _: ConfigurableApplicationContext, delegate: ServiceInstanceListSupplier ->
            IsolationServiceInstanceListSupplier(delegate, isolationConfigProperty)
        }
}
