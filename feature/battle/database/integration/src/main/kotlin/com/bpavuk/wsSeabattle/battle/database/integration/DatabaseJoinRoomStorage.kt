package com.bpavuk.wsSeabattle.battle.database.integration

import RoomsDao
import com.bpavuk.wsSeabattle.battle.types.Room
import com.bpavuk.wsSeabattle.battle.usecase.JoinRoomUsecase

class DatabaseJoinRoomStorage: JoinRoomUsecase.Storage {
    private val roomsDao = RoomsDao()

    override fun getRoom(id: Int): Room? =
        roomsDao.getRoomById(id)

    override fun join(roomId: Int, userId: Int) =
        roomsDao.joinToRoom(roomId, userId)
}
