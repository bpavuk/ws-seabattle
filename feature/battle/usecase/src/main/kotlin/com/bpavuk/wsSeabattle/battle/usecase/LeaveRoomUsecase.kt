package com.bpavuk.wsSeabattle.battle.usecase

class LeaveRoomUsecase(
    private val storage: Storage,
    private val getRoomUsecase: GetRoomUsecase
) {
    fun leaveRoom(userId: Int): Result {
        val room = when (val result = getRoomUsecase.getRoomByUserId(userId)) {
            GetRoomUsecase.Result.NotFound ->
                return Result.NotInRoom
            is GetRoomUsecase.Result.Success ->
                result.room
        }
        storage.leaveRoom(userId, room.id)
        return Result.Success(room.id)
    }

    interface Storage {
        fun leaveRoom(userId: Int, roomId: Int)
    }

    sealed interface Result {
        data class Success(val roomId: Int): Result

        data object NotInRoom: Result
    }
}
