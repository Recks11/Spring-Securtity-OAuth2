package com.rexijie.resourceserverapp.appRoutes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
class BaseRoute {

    @Bean
    fun whoAmI(): RouterFunction<ServerResponse> {
        return router {
            GET("/api/whoAmI") {
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(it.principal().get())
            }
        }
    }
}