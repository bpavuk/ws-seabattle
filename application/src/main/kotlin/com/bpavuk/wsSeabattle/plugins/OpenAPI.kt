package com.bpavuk.wsSeabattle.plugins

import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureOpenAPI() {
    routing {
        openAPI(path = "openapi")
    }
}
