package com.bpavuk.wsSeabattle.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*

fun Application.configureOpenAPI() {
    routing {
        swaggerUI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    }
}
