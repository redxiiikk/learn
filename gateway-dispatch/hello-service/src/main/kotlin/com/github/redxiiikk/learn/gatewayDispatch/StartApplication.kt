package com.github.redxiiikk.learn.gatewayDispatch

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@EnableDiscoveryClient
@SpringBootApplication
open class StartApplication

fun main(args: Array<String>) {
    runApplication<StartApplication>(*args)
}


@RestController
@RequestMapping("/hello")
open class HelloController {
    @Value("\${spring.cloud.nacos.discovery.namespace}")
    lateinit var registerNamespace: String

    @Value("\${spring.cloud.nacos.discovery.group}")
    lateinit var registerGroup: String


    @Value("\${spring.cloud.nacos.discovery.metadata.ISOLATION}")
    lateinit var isolationEnv: String

    @GetMapping
    fun hello() = "Hello, World"

    @GetMapping("/{name}")
    fun hello(@PathVariable name: String) =
        "Hello, $name. this is from [$registerNamespace:$registerGroup ]: $isolationEnv"
}