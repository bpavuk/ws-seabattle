package com.bpavuk.wsSeabattle.battle.endpoints.create

import com.bpavuk.wsSeabattle.core.types.Room

interface CreateRoomRepository {
    fun createRoom(userId: Int): CreateRoomResponse
}

sealed interface CreateRoomResponse {
    class Success(val room: Room) : CreateRoomResponse

    data object UserAlreadyInRoom : CreateRoomResponse
}
