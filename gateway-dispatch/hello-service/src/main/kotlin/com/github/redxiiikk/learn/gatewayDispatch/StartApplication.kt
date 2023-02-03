package com.github.redxiiikk.learn.gatewayDispatch

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
    @GetMapping
    fun hello() = "Hello, World"

    @GetMapping("/{name}")
    fun hello(@PathVariable name: String) = "Hello, $name"
}