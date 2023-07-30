package com.bpavuk.wsSeabattle.battle.database.integration

import com.bpavuk.wsSeabattle.battle.database.RoomsDao
import com.bpavuk.wsSeabattle.battle.usecase.JoinRoomUsecase
import com.bpavuk.wsSeabattle.core.types.Room

class DatabaseJoinRoomStorage: JoinRoomUsecase.Storage {
    private val roomsDao = RoomsDao()

    override fun getRoom(id: Int): Room? =
        roomsDao.getRoomById(id)

    override fun join(roomId: Int, userId: Int) =
        roomsDao.joinToRoom(roomId, userId)
}
