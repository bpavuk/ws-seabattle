package com.bpavuk.wsSeabattle.frontend

import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomPlugin
import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomResponse
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomPlugin
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomRepository
import com.bpavuk.wsSeabattle.chat.endpoints.ChatRepository
import com.bpavuk.wsSeabattle.chat.endpoints.ChatResponse
import com.bpavuk.wsSeabattle.core.endpoints.*
import com.bpavuk.wsSeabattle.core.types.Connection
import com.bpavuk.wsSeabattle.core.types.toCoordinate
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch

fun Route.launchFrontend(deps: FrontendDependencies) {
    val connectionContainer = deps.connectionContainer
    expandableScope(deps.pluginRegistry) {
        webSocket("/battle/session") {
            val thisConnection = Connection(this)
            connectionContainer.connections.add(thisConnection)
            thisConnection.session.send(":q for quit, /new for room creation, /join <room id> for joining, " +
                    "/whereami for current room")

            install(QuitPlugin(connectionContainer))
            install(JoinRoomPlugin(deps.joinRoomRepository, connectionContainer))
            install(CreateRoomPlugin(deps.createRoomRepository, connectionContainer))

            for (frame in incoming) {
                connectionContainer.removeInactiveConnections()
                if (frame is Frame.Text) {
                    val message = frame.readText()

                    when {
                        message == "/whereami" -> {
                            when (val response = deps.getRoomRepository.getRoomByUserId(thisConnection.userId)) {
                                GetRoomResponse.NotFound ->
                                    thisConnection.session.send("you are not joined to any room")
                                is GetRoomResponse.Success ->
                                    thisConnection.session.send("you are in room ${response.room.id}")
                            }
                        }
                        message.matches("/leave".toRegex()) -> {
                            // TODO: add room leaving logic
                        }
                        message.matches("/shoot ?(([a-fA-F]|[1-9]){2})?".toRegex()) -> {
                            // TODO: add sea battle logic
                            val coordinateFinderRegex = Regex("(([a-fA-F]|[1-9]){2})")
                            val coordinate = coordinateFinderRegex.find(message.removePrefix("/shoot"))?.value
                                ?.toCoordinate()
                            if (coordinate == null) {
                                thisConnection.session.send("invalid coordinate. to shoot, you should insert" +
                                        " valid coordinates")
                            }
                        }
                        else -> {
                            when (deps.chatRepository.sendTheMessage(thisConnection.userId, message)) {
                                ChatResponse.NotJoinedToAnyRoom ->
                                    thisConnection.session.send("you are not joined to any room. to chat, " +
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
                        val isProcessed = plugin.onMessage(frame, thisConnection)
                    } while (!isProcessed && pluginIterator.hasNext())
                }
            }
        }
    }
}

class FrontendDependencies(
    val createRoomRepository: CreateRoomRepository,
    val getRoomRepository: GetRoomRepository,
    val joinRoomRepository: JoinRoomRepository,
    val chatRepository: ChatRepository,
    val connectionContainer: ConnectionContainer,
    val pluginRegistry: PluginRegistry
)

class QuitPlugin(
    allUsers: ConnectionContainer
) : Plugin(allUsers) {
    override suspend fun onMessage(message: Frame, thisUser: Connection): Boolean {
        return if ((message is Frame.Text) && (message.readText() == ":q")) {
            thisUser.send("bye")
            thisUser.session.close()
            allUsers.connections.remove(thisUser)
            true
        } else false
    }
}
