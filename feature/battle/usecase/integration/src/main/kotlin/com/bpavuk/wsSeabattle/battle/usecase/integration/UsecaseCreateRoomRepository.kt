package com.bpavuk.wsSeabattle.battle.usecase.integration

import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomResponse
import com.bpavuk.wsSeabattle.battle.usecase.CreateRoomUsecase

class UsecaseCreateRoomRepository(
    private val usecase: CreateRoomUsecase
): CreateRoomRepository {
    override fun createRoom(userId: Int): CreateRoomResponse = usecase.createRoom(userId).toEndpoint()

    private fun CreateRoomUsecase.Result.toEndpoint(): CreateRoomResponse = when (this) {
        is CreateRoomUsecase.Result.Success -> CreateRoomResponse.Success(room)
        CreateRoomUsecase.Result.UserAlreadyInRoom -> CreateRoomResponse.UserAlreadyInRoom
    }
}
