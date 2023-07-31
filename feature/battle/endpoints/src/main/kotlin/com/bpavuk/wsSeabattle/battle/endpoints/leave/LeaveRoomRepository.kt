package com.bpavuk.wsSeabattle.battle.endpoints.leave

interface LeaveRoomRepository {
    fun leave(userId: Int): LeaveRoomResponse
}

sealed interface LeaveRoomResponse {
    data class Success(val roomId: Int): LeaveRoomResponse

    data object NotInRoom: LeaveRoomResponse
}
