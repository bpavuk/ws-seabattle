package com.bpavuk.wsSeabattle.core.endpoints

import com.bpavuk.wsSeabattle.core.types.UserConnection
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import java.util.*

object ConnectionContainer {
    val userConnections: MutableSet<UserConnection> = Collections.synchronizedSet(LinkedHashSet())
    val connectionsToRemove: MutableSet<UserConnection> = Collections.synchronizedSet(LinkedHashSet())
}

suspend fun ConnectionContainer.removeInactiveConnections() {
    this.userConnections.forEach { connection ->
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
    this.userConnections.removeAll(this.connectionsToRemove)
    this.connectionsToRemove.clear()
}
