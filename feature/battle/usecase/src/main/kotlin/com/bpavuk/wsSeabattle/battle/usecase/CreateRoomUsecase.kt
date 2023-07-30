package com.bpavuk.wsSeabattle.battle.usecase

import com.bpavuk.wsSeabattle.battle.usecase.CreateRoomUsecase.Storage
import com.bpavuk.wsSeabattle.core.types.Room

/**
 * The CreateRoomUsecase class is responsible for creating a room for a given user ID and joining the user to the room.
 * It interacts with a [Storage] implementation to perform the necessary operations.
 *
 * @property storage the storage implementation used for room operations
 */
class CreateRoomUsecase(
    private val storage: Storage
) {
    /**
     * Creates a room for the given user ID and joins user to the room.
     *
     * @param userId the ID of the user
     * @return the result of the room creation
     */
    fun createRoom(userId: Int): Result {
        val userRoom = storage.getRoomByUserId(userId)

        return if (userRoom.singleOrNull() != null) {
            Result.UserAlreadyInRoom
        } else {
            val room = storage.createRoom()
            storage.joinRoom(room, userId)
            Result.Success(room)
        }
    }

    /**
     * Represents the possible results of an operation.
     */
    sealed interface Result {
        class Success(val room: Room) : Result

        data object UserAlreadyInRoom : Result
    }

    interface Storage {
        fun createRoom() : Room

        fun joinRoom(room: Room, userId: Int)

        fun getRoomByUserId(id: Int) : List<Room>
    }
}
