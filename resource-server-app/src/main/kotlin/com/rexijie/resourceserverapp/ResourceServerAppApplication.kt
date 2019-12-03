package com.rexijie.resourceserverapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResourceServerAppApplication

fun main(args: Array<String>) {
    runApplication<ResourceServerAppApplication>(*args)
}
