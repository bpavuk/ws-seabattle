package com.bpavuk.wsSeabattle

import com.bpavuk.wsSeabattle.battle.endpoints.battleRouting
import com.bpavuk.wsSeabattle.plugins.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureOpenAPI()
    configureSerialization()
    configureSockets()

    routing {
        battleRouting()
    }
}
