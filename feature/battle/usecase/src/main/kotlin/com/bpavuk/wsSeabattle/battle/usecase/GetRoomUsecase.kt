package com.bpavuk.wsSeabattle.battle.usecase

import com.bpavuk.wsSeabattle.core.types.Room


class GetRoomUsecase(
    private val storage: Storage
) {
    fun getRoomById(id: Int): Result {
        val room = storage.getRoom(id) ?: return Result.NotFound
        return Result.Success(room)
    }

    fun getRoomByUserId(id: Int): Result {
        val room = storage.getRoomByUserId(id) ?: return Result.NotFound
        return Result.Success(room)
    }

    interface Storage {
        fun getRoomByUserId(id: Int): Room?

        fun getRoom(id: Int): Room?
    }

    sealed interface Result {
        data object NotFound : Result

        data class Success(val room: Room) : Result
    }
}
