package com.bpavuk.wsSeabattle.core.endpoints

import com.bpavuk.wsSeabattle.core.types.Connection
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import java.util.*

class ConnectionContainer {
    val connections: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())
    val connectionsToRemove: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())
}

suspend fun ConnectionContainer.removeInactiveConnections() {
    this.connections.forEach { connection ->
        if (!connection.session.isActive) {
            connection.session.close(
                CloseReason(
                    CloseReason.Codes.GOING_AWAY,
                    message = "${connection.name} gone"
                )
            )
            this.connectionsToRemove.add(connection)
        }
    }
    this.connections.removeAll(this.connectionsToRemove)
    this.connectionsToRemove.clear()
}
