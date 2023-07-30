package com.bpavuk.wsSeabattle.chat.usecase

import com.bpavuk.wsSeabattle.battle.usecase.GetRoomUsecase
import com.bpavuk.wsSeabattle.core.endpoints.ConnectionContainer
import io.ktor.websocket.*

class ChatUsecase(
    private val getRoomUsecase: GetRoomUsecase,
    private val connectionContainer: ConnectionContainer
) {
    suspend fun sendMessage(userId: Int, message: String): Result {
        val room = when (val response = getRoomUsecase.getRoomByUserId(userId)) {
            GetRoomUsecase.Result.NotFound -> return Result.NotJoinedToAnyRoom
            is GetRoomUsecase.Result.Success -> response.room
        }
        connectionContainer.connections.filter { it.userId in room.opponents }.forEach {
            it.session.send("[user$userId]: $message")
        }
        return Result.MessageSent
    }

    sealed interface Result {
        data object MessageSent: Result

        data object NotJoinedToAnyRoom: Result
    }
}
