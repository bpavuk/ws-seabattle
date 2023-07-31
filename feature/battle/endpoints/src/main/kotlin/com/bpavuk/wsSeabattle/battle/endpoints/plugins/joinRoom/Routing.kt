package com.bpavuk.wsSeabattle.battle.endpoints.plugins.joinRoom

import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomResult
import com.bpavuk.wsSeabattle.core.endpoints.Plugin
import com.bpavuk.wsSeabattle.core.endpoints.createBackendPlugin
import io.ktor.websocket.*

@Suppress("FunctionName")
fun JoinRoomPlugin(
    joinRoomRepository: JoinRoomRepository
): Plugin = createBackendPlugin {
    onMessage = { message, thisUser ->
        if (message is Frame.Text && message.readText().matches("/join ?\\d+?".toRegex())) {
            val numberFinderRegex = Regex("\\d+")
            val roomId = numberFinderRegex.find(message.readText())!!.value.toInt()
            when (
                joinRoomRepository.join(roomId, thisUser.userId)
            ) {
                JoinRoomResult.RoomNotFound ->
                    thisUser.send("room with id $roomId not found")

                JoinRoomResult.Success ->
                    thisUser.send("joined to room $roomId")

                JoinRoomResult.AlreadyInRoom ->
                    thisUser.send("you are already in the room")
            }
            true
        } else false
    }
}
