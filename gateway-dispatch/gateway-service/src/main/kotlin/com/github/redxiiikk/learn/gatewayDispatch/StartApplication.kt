package com.github.redxiiikk.learn.gatewayDispatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
open class StartApplication

fun main(args: Array<String>) {
    runApplication<StartApplication>(*args)
}

