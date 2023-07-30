package com.bpavuk.wsSeabattle.core.types

import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val userId = lastId.getAndIncrement()
    val name = "user$userId"
}
