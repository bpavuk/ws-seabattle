package com.bpavuk.wsSeabattle.battle.endpoints.plugins.leaveRoom

import com.bpavuk.wsSeabattle.battle.endpoints.leave.LeaveRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.leave.LeaveRoomResponse
import com.bpavuk.wsSeabattle.core.endpoints.Plugin
import com.bpavuk.wsSeabattle.core.endpoints.createBackendPlugin
import io.ktor.websocket.*

@Suppress("FunctionName")
fun LeaveRoomPlugin(leaveRoomRepository: LeaveRoomRepository): Plugin = createBackendPlugin {
    onMessage { message, thisUser ->
        if (message is Frame.Text && message.readText() == "/leave") {
            when (val response = leaveRoomRepository.leave(thisUser.userId)) {
                LeaveRoomResponse.NotInRoom ->
                    thisUser.send("you are not in room")
                is LeaveRoomResponse.Success ->
                    thisUser.send("you have left the room ${response.roomId}")
            }
            true
        } else false
    }
}
