package com.bpavuk.wsSeabattle.core.endpoints

import com.bpavuk.wsSeabattle.core.types.UserConnection
import io.ktor.websocket.*

class Plugin(
    private val messageHandler: suspend (Frame, UserConnection) -> Boolean
) {
    suspend fun onMessage(message: Frame, thisUser: UserConnection): Boolean = messageHandler(message, thisUser)
}

object PluginRegistry {
    val plugins = mutableListOf<Plugin>()

    fun install(plugin: Plugin) {
        plugins.add(plugin)
    }
}

fun expandableScope(registry: PluginRegistry, block: PluginRegistry.() -> Unit) {
    block(registry)
}

fun createBackendPlugin(
    config: PluginApi.() -> Unit
): Plugin {
    val api = PluginApi()
    api.config()
    return Plugin(api.onMessage)
}

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PluginApi(
    internal var onMessage: suspend (Frame, UserConnection) -> Boolean = { _, _ -> true},
    val allUsers: ConnectionContainer = ConnectionContainer
) {
    suspend fun UserConnection.send(message: String) {
        session.send(message)
    }

    suspend fun UserConnection.send(message: Frame) {
        session.send(message)
    }

    suspend fun UserConnection.send(message: ByteArray) {
        session.send(message)
    }

    suspend fun broadcast(message: Frame) {
        allUsers.userConnections.forEach {
            it.send(message)
        }
    }

    suspend fun broadcast(message: ByteArray) {
        allUsers.userConnections.forEach {
            it.send(message)
        }
    }

    suspend fun broadcast(message: String) {
        allUsers.userConnections.forEach {
            it.send(message)
        }
    }

    fun onMessage(handler: suspend (Frame, UserConnection) -> Boolean) {
        onMessage = handler
    }
}
