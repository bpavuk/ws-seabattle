package com.bpavuk.wsSeabattle.battle.usecase.integration

import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.get.GetRoomResponse
import com.bpavuk.wsSeabattle.battle.usecase.GetRoomUsecase

class UsecaseGetRoomRepository(private val usecase: GetRoomUsecase) : GetRoomRepository {
    override fun getRoomById(id: Int): GetRoomResponse =
        usecase.getRoomById(id).toEndpoint()

    override fun getRoomByUserId(userId: Int): GetRoomResponse =
        usecase.getRoomByUserId(userId).toEndpoint()

    private fun GetRoomUsecase.Result.toEndpoint(): GetRoomResponse = when (this) {
        GetRoomUsecase.Result.NotFound -> GetRoomResponse.NotFound
        is GetRoomUsecase.Result.Success -> GetRoomResponse.Success(room)
    }
}