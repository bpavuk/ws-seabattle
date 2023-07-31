package com.bpavuk.wsSeabattle.battle.endpoints.plugins.whereami

import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomResponse
import com.bpavuk.wsSeabattle.core.endpoints.Plugin
import com.bpavuk.wsSeabattle.core.endpoints.createBackendPlugin
import io.ktor.websocket.*

@Suppress("FunctionName")
fun WhereAmIPlugin(getRoomRepository: GetRoomRepository): Plugin = createBackendPlugin {
    onMessage = { message, thisUser ->
        if (message is Frame.Text && message.readText() == "/whereami") {
            when (val response = getRoomRepository.getRoomByUserId(thisUser.userId)) {
                GetRoomResponse.NotFound ->
                    thisUser.send("you are not joined to any room")
                is GetRoomResponse.Success ->
                    thisUser.send("you are in room ${response.room.id}")
            }
            true
        } else false
    }
}
