package com.bpavuk.wsSeabattle.battle.usecase.integration

import com.bpavuk.wsSeabattle.battle.endpoints.leave.LeaveRoomRepository
import com.bpavuk.wsSeabattle.battle.endpoints.leave.LeaveRoomResponse
import com.bpavuk.wsSeabattle.battle.usecase.LeaveRoomUsecase

class UsecaseLeaveRoomRepository(
    private val usecase: LeaveRoomUsecase
): LeaveRoomRepository {
    override fun leave(userId: Int): LeaveRoomResponse = usecase.leaveRoom(userId).toEndpoint()

    private fun LeaveRoomUsecase.Result.toEndpoint(): LeaveRoomResponse = when (this) {
        LeaveRoomUsecase.Result.NotInRoom -> LeaveRoomResponse.NotInRoom
        is LeaveRoomUsecase.Result.Success -> LeaveRoomResponse.Success(roomId)
    }
}