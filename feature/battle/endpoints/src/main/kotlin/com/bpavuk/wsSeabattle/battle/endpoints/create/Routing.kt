
package com.bpavuk.wsSeabattle.battle.endpoints.create

import com.bpavuk.wsSeabattle.core.endpoints.createBackendPlugin
import com.bpavuk.wsSeabattle.core.types.Room
import io.ktor.websocket.*

interface CreateRoomRepository {
    fun createRoom(userId: Int): CreateRoomResponse
}

sealed interface CreateRoomResponse {
    class Success(val room: Room) : CreateRoomResponse

    data object UserAlreadyInRoom : CreateRoomResponse
}

@Suppress("FunctionName")
fun CreateRoomPlugin(createRoomRepository: CreateRoomRepository) = createBackendPlugin {
    // fixme: add ability to get the repository
    onMessage = { message, thisUser ->
        if (message is Frame.Text && message.readText() == "/new") {
            when (val response = createRoomRepository.createRoom(thisUser.userId)) {
                is CreateRoomResponse.Success ->
                    thisUser.send("room ${response.room.id} created")
                CreateRoomResponse.UserAlreadyInRoom ->
                    thisUser.send("you are already in room")
            }
            true
        } else false
    }
}
