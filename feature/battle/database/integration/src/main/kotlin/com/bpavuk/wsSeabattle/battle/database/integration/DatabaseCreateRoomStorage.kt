package com.bpavuk.wsSeabattle.battle.database.integration

import com.bpavuk.wsSeabattle.battle.database.RoomsDao
import com.bpavuk.wsSeabattle.battle.usecase.CreateRoomUsecase
import com.bpavuk.wsSeabattle.core.types.Room


class DatabaseCreateRoomStorage: CreateRoomUsecase.Storage {
    private val roomsDao = RoomsDao()

    override fun createRoom(): Room =
        roomsDao.addRoom()

    override fun joinRoom(room: Room, userId: Int) {
        roomsDao.joinToRoom(room.id, userId)
    }

    override fun getRoomByUserId(id: Int): List<Room> = roomsDao.getRoomsByUserId(id)
}
