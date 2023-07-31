package com.bpavuk.wsSeabattle.battle.endpoints.plugins.createRoom

import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomResponse
import com.bpavuk.wsSeabattle.core.endpoints.createBackendPlugin
import io.ktor.websocket.*

@Suppress("FunctionName")
fun CreateRoomPlugin(createRoomRepository: CreateRoomRepository) = createBackendPlugin {
    // fixme: add ability to get the repository
    onMessage { message, thisUser ->
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
