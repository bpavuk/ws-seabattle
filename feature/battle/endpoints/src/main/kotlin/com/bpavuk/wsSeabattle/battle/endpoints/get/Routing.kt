package com.bpavuk.wsSeabattle.battle.endpoints.get

import com.bpavuk.wsSeabattle.battle.types.Room

interface GetRoomRepository {

    fun getRoomById(id: Int): GetRoomResponse
    fun getRoomByUserId(userId: Int): GetRoomResponse
}

sealed interface GetRoomResponse {
    class Success(val room: Room) : GetRoomResponse

    data object NotFound : GetRoomResponse
}
