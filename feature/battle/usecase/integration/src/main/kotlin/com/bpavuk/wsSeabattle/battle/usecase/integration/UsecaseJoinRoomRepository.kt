package com.bpavuk.wsSeabattle.battle.usecase.integration

import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomResult
import com.bpavuk.wsSeabattle.battle.usecase.JoinRoomUsecase

class UsecaseJoinRoomRepository(val usecase: JoinRoomUsecase) : JoinRoomRepository {
    override fun join(roomId: Int, userId: Int): JoinRoomResult =
        usecase.join(roomId, userId).toEndpoint()

    private fun JoinRoomUsecase.Result.toEndpoint(): JoinRoomResult = when (this) {
        JoinRoomUsecase.Result.RoomNotFound -> JoinRoomResult.RoomNotFound
        JoinRoomUsecase.Result.Success -> JoinRoomResult.Success
        JoinRoomUsecase.Result.AlreadyInRoom -> JoinRoomResult.AlreadyInRoom
    }
}
