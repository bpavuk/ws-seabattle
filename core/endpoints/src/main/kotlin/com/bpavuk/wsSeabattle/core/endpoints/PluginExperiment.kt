package com.bpavuk.wsSeabattle.core.endpoints

import com.bpavuk.wsSeabattle.core.types.Connection
import io.ktor.websocket.*

abstract class Plugin(
    val allUsers: ConnectionContainer
) {
    suspend fun Connection.send(message: String) {
        session.send(message)
    }

    suspend fun Connection.send(message: Frame) {
        session.send(message)
    }

    suspend fun Connection.send(message: ByteArray) {
        session.send(message)
    }

    /**
     * Handles incoming messages.
     *
     * @param message The message to be processed.
     * @return Boolean value indicating whether the message was processed.
     */
    abstract suspend fun onMessage(message: Frame, thisUser: Connection): Boolean
}

class PluginRegistry {
    val plugins = mutableListOf<Plugin>()

    fun install(plugin: Plugin) {
        plugins.add(plugin)
    }
}

fun expandableScope(registry: PluginRegistry, block: PluginRegistry.() -> Unit) {
    block(registry)
}
