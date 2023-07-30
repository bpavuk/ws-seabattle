package com.bpavuk.wsSeabattle.battle.endpoints

import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomResponse
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomResponse
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomResult
import com.bpavuk.wsSeabattle.battle.types.Connection
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import java.util.*

fun Route.battleRouting(dependencies: BattleDependencies) {
    val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())
    val connectionsToRemove = Collections.synchronizedSet<Connection>(LinkedHashSet())
    webSocket("/battle/session") {
        val thisConnection = Connection(this)
        connections.add(thisConnection)
        thisConnection.session.send(":q for quit, /new for room creation")
        for (frame in incoming) {
            if (frame is Frame.Text) {
                connections.forEach { connection ->
                    if (!connection.session.isActive) {
                        connection.session.close(CloseReason(
                            CloseReason.Codes.GOING_AWAY,
                            "${connection.name} gone"
                        ))
                        connectionsToRemove.add(connection)
                    }
                }
                connections.removeAll(connectionsToRemove)
                connectionsToRemove.clear()

                val message = frame.readText()

                when {
                    message == ":q" -> {
                        thisConnection.session.send("bye")
                        thisConnection.session.close()
                        connections.remove(thisConnection)
                    }
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
                    message.matches("/join \\d+".toRegex()) -> {
                        val numberFinderRegex = Regex("\\d+")
                        val roomId = numberFinderRegex.find(message)!!.value.toInt()
                        when (
                            dependencies.joinRoomRepository.join(roomId, thisConnection.userId)
                        ) {
                            JoinRoomResult.RoomNotFound -> thisConnection.session.send("room with id $roomId not found")
                            JoinRoomResult.Success -> thisConnection.session.send("joined to room $roomId")
                        }
                    }
                }
            }
        }
    }
}

class BattleDependencies(
    val createRoomRepository: CreateRoomRepository,
    val getRoomRepository: GetRoomRepository,
    val joinRoomRepository: JoinRoomRepository
)
