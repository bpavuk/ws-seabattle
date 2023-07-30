package com.bpavuk.wsSeabattle.battle.database.integration

import com.bpavuk.wsSeabattle.battle.database.RoomsDao
import com.bpavuk.wsSeabattle.battle.usecase.GetRoomUsecase
import com.bpavuk.wsSeabattle.core.types.Room

class DatabaseGetRoomStorage: GetRoomUsecase.Storage {
    private val roomsDao = RoomsDao()
    override fun getRoomByUserId(id: Int): Room? =
        roomsDao.getRoomsByUserId(id).singleOrNull()

    override fun getRoom(id: Int): Room? =
        roomsDao.getRoomById(id)
}
