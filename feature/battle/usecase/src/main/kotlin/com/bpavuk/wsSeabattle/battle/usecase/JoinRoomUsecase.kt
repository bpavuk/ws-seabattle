package com.bpavuk.wsSeabattle.battle.usecase

import com.bpavuk.wsSeabattle.core.types.Room


class JoinRoomUsecase(
    val storage: Storage
) {
    fun join(id: Int, userId: Int): Result {
        if (storage.getRoomByUserId(userId) != null) return Result.AlreadyInRoom
        val room = storage.getRoom(id) ?: return Result.RoomNotFound
        storage.join(room.id, userId)
        return Result.Success
    }

    interface Storage {
        fun getRoom(id: Int): Room?

        fun join(roomId: Int, userId: Int)

        fun getRoomByUserId(userId: Int): Room?
    }

    sealed interface Result {
        data object RoomNotFound : Result

        data object Success : Result

        data object AlreadyInRoom: Result
    }
}
