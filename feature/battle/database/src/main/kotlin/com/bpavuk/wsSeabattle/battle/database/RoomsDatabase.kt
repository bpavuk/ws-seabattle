package com.bpavuk.wsSeabattle.battle.database

import com.bpavuk.wsSeabattle.core.types.Room


/**
 * A singleton class that represents a database of rooms.
 */
object RoomsDatabase {
    val roomList = mutableListOf<Room>()
}

/**
 * This class represents a data access object for managing rooms in RoomsDatabase.
 *
 * RoomsDao provides methods to add rooms, retrieve rooms by their IDs, retrieve rooms where a user plays,
 * and join a user to a room.
 */
class RoomsDao {
    /**
     * Adds a room to the [RoomsDatabase].
     *
     * @return the newly added room.
     */
    fun addRoom(): Room {
        val room = Room()
        RoomsDatabase.roomList.add(room)
        return room
    }

    /**
     * Retrieves the room with the specified room ID.
     *
     * @param roomId The ID of the room to retrieve.
     * @return The room with the specified ID, or null if no room is found.
     */
    fun getRoomById(roomId: Int): Room? =
        RoomsDatabase.roomList.find { it.id == roomId }

    /**
     * Retrieves a list of rooms where the user plays.
     *
     * @param userId The ID of the user.
     * @return A list of Room objects.
     */
    fun getRoomsByUserId(userId: Int): List<Room> =
        RoomsDatabase.roomList.filter { it.opponents.contains(userId) }

    /**
     * Joins a user to the specified room.
     *
     * @param roomId The ID of the room to join.
     * @param userId The user ID of the user joining the room.
     * @throws NullPointerException If the room with the specified ID does not exist.
     */
    @Throws(NullPointerException::class)
    fun joinToRoom(roomId: Int, userId: Int) {
        getRoomById(roomId)!!.addOpponent(userId)
    }
}
