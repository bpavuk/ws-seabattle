package com.bpavuk.wsSeabattle.frontend

import com.bpavuk.wsSeabattle.chat.endpoints.ChatRepository
import com.bpavuk.wsSeabattle.chat.endpoints.ChatResponse
import com.bpavuk.wsSeabattle.core.endpoints.*
import com.bpavuk.wsSeabattle.core.types.UserConnection
import com.bpavuk.wsSeabattle.core.types.toCoordinate
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch

fun Route.launchFrontend(deps: FrontendDependencies) {
    val connectionContainer = deps.connectionContainer
    expandableScope(deps.pluginRegistry) {
        webSocket("/battle/session") {
            val thisUserConnection = UserConnection(this)
            connectionContainer.userConnections.add(thisUserConnection)
            thisUserConnection.session.send(":q for quit, /new for room creation, /join <room id> for joining, " +
                    "/whereami for current room")

            install(QuitPlugin) // look at this plugin as on example plugin
            install(deps.joinRoomPlugin)
            install(deps.createRoomPlugin)
            install(deps.whereAmIPlugin)

            for (frame in incoming) {
                connectionContainer.removeInactiveConnections()
                if (frame is Frame.Text) {
                    val message = frame.readText()

                    when {
                        message.matches("/leave".toRegex()) -> {
                            // TODO: add room leaving logic
                        }
                        message.matches("/shoot ?(([a-fA-F]|[1-9]){2})?".toRegex()) -> {
                            // TODO: add sea battle logic
                            val coordinateFinderRegex = Regex("(([a-fA-F]|[1-9]){2})")
                            val coordinate = coordinateFinderRegex.find(message.removePrefix("/shoot"))?.value
                                ?.toCoordinate()
                            if (coordinate == null) {
                                thisUserConnection.session.send("invalid coordinate. to shoot, you should insert" +
                                        " valid coordinates")
                            }
                        }
                        else -> {
                            when (deps.chatRepository.sendTheMessage(thisUserConnection.userId, message)) {
                                ChatResponse.NotJoinedToAnyRoom ->
                                    thisUserConnection.session.send("you are not joined to any room. to chat, " +
                                            "join to room using /join <room id> or create new room using /new")
                                ChatResponse.MessageSent -> {}
                            }
                        }
                    }
                }
                launch {
                    val pluginIterator = plugins.iterator()
                    do {
                        val plugin = pluginIterator.next()
                        println("plugin ${plugin.javaClass.name} is started")
                        val isProcessed = plugin.onMessage(frame, thisUserConnection)
                    } while (!isProcessed && pluginIterator.hasNext())
                }
            }
        }
    }
}

class FrontendDependencies(
    val createRoomPlugin: Plugin,
    val joinRoomPlugin: Plugin,
    val whereAmIPlugin: Plugin,
    val chatRepository: ChatRepository,
    val connectionContainer: ConnectionContainer,
    val pluginRegistry: PluginRegistry
)

val QuitPlugin = createBackendPlugin {
    onMessage = { message, thisUser ->
        if ((message is Frame.Text) && (message.readText() == ":q")) {
            thisUser.send("bye")
            thisUser.session.close()
            allUsers.userConnections.remove(thisUser)
            true
        } else false
    }
}
