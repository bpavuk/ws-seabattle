package com.bpavuk.wsSeabattle.battle.database.integration

import RoomsDao
import com.bpavuk.wsSeabattle.battle.types.Room
import com.bpavuk.wsSeabattle.battle.usecase.GetRoomUsecase

class DatabaseGetRoomStorage: GetRoomUsecase.Storage {
    private val roomsDao = RoomsDao()
    override fun getRoomByUserId(id: Int): Room? =
        roomsDao.getRoomsByUserId(id).singleOrNull()

    override fun getRoom(id: Int): Room? =
        roomsDao.getRoomById(id)
}
