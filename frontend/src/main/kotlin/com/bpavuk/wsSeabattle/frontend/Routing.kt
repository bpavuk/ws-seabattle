package com.bpavuk.wsSeabattle.frontend

import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomResponse
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomResponse
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomResult
import com.bpavuk.wsSeabattle.chat.endpoints.ChatRepository
import com.bpavuk.wsSeabattle.chat.endpoints.ChatResponse
import com.bpavuk.wsSeabattle.core.endpoints.ConnectionContainer
import com.bpavuk.wsSeabattle.core.endpoints.Plugin
import com.bpavuk.wsSeabattle.core.endpoints.PluginRegistry
import com.bpavuk.wsSeabattle.core.endpoints.removeInactiveConnections
import com.bpavuk.wsSeabattle.core.types.Connection
import com.bpavuk.wsSeabattle.core.types.toCoordinate
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Route.launchFrontend(dependencies: FrontendDependencies) {
    val connectionContainer = dependencies.connectionContainer
    webSocket("/battle/session") {
        val thisConnection = Connection(this)
        connectionContainer.connections.add(thisConnection)
        thisConnection.session.send(":q for quit, /new for room creation, /join <room id> for joining, " +
                "/whereami for current room")

        dependencies.pluginRegistry.install(QuitPlugin(connectionContainer))

        for (frame in incoming) {
            connectionContainer.removeInactiveConnections()
            if (frame is Frame.Text) {

                val message = frame.readText()

                when {
                    message == "/new" -> {
                        when (val response = dependencies.createRoomRepository.createRoom(thisConnection.userId)) {
                            is CreateRoomResponse.Success ->
                                thisConnection.session.send("room ${response.room.id} created")
                            CreateRoomResponse.UserAlreadyInRoom ->
                                thisConnection.session.send("you are already in room")
                        }
                    }
                    message == "/whereami" -> {
                        when (val response = dependencies.getRoomRepository.getRoomByUserId(thisConnection.userId)) {
                            GetRoomResponse.NotFound ->
                                thisConnection.session.send("you are not joined to any room")
                            is GetRoomResponse.Success ->
                                thisConnection.session.send("you are in room ${response.room.id}")
                        }
                    }
                    message.matches("/join \\d+".toRegex()) || message.matches("/join ?".toRegex()) -> {
                        val numberFinderRegex = Regex("\\d+")
                        val roomId = numberFinderRegex.find(message)!!.value.toInt()
                        when (
                            dependencies.joinRoomRepository.join(roomId, thisConnection.userId)
                        ) {
                            JoinRoomResult.RoomNotFound ->
                                thisConnection.session.send("room with id $roomId not found")
                            JoinRoomResult.Success ->
                                thisConnection.session.send("joined to room $roomId")
                            JoinRoomResult.AlreadyInRoom ->
                                thisConnection.session.send("you are already in the room")
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
                        when (dependencies.chatRepository.sendTheMessage(thisConnection.userId, message)) {
                            ChatResponse.NotJoinedToAnyRoom ->
                                thisConnection.session.send("you are not joined to any room. to chat, join to" +
                                        " room using /join <room id> or create new room using /new")
                            ChatResponse.MessageSent -> {}
                        }
                    }
                }
            }
            dependencies.pluginRegistry.plugins.forEach { plugin ->
                plugin.onMessage(frame, thisConnection)
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
            thisUser.sendMessage("bye")
            thisUser.session.close()
            allUsers.connections.remove(thisUser)
            true
        } else false
    }
}
