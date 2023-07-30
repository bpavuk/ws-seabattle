package com.bpavuk.wsSeabattle.battle.database.integration

import RoomsDao
import com.bpavuk.wsSeabattle.battle.types.Room
import com.bpavuk.wsSeabattle.battle.usecase.CreateRoomUsecase


class DatabaseCreateRoomStorage: CreateRoomUsecase.Storage {
    private val roomsDao = RoomsDao()

    override fun createRoom(): Room =
        roomsDao.addRoom()

    override fun joinRoom(room: Room, userId: Int) {
        roomsDao.joinToRoom(room.id, userId)
    }

    override fun getRoomByUserId(id: Int): List<Room> = roomsDao.getRoomsByUserId(id)
}
