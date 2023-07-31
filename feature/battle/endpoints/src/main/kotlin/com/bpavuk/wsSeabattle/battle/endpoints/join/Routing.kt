package com.bpavuk.wsSeabattle.battle.endpoints.join

import com.bpavuk.wsSeabattle.core.endpoints.ConnectionContainer
import com.bpavuk.wsSeabattle.core.endpoints.Plugin
import com.bpavuk.wsSeabattle.core.types.Connection
import io.ktor.websocket.*

interface JoinRoomRepository {
    fun join(roomId: Int, userId: Int): JoinRoomResult
}

sealed interface JoinRoomResult {
    data object Success: JoinRoomResult

    data object RoomNotFound: JoinRoomResult

    data object AlreadyInRoom: JoinRoomResult
}

class JoinRoomPlugin(
    private val joinRoomRepository: JoinRoomRepository,
    allUsers: ConnectionContainer
): Plugin(allUsers) {
    override suspend fun onMessage(message: Frame, thisUser: Connection): Boolean {
        println("join room is called")
        return if (message is Frame.Text && message.readText().matches("/join ?\\d+?".toRegex())) {
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
