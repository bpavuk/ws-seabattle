package com.bpavuk.wsSeabattle.battle.endpoints.join

interface JoinRoomRepository {
    fun join(roomId: Int, userId: Int): JoinRoomResult
}

sealed interface JoinRoomResult {
    data object Success: JoinRoomResult

    data object RoomNotFound: JoinRoomResult
}
