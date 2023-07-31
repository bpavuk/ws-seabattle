package com.bpavuk.wsSeabattle.battle.database.integration

import com.bpavuk.wsSeabattle.battle.database.RoomsDao
import com.bpavuk.wsSeabattle.battle.usecase.LeaveRoomUsecase

class DatabaseLeaveRoomStorage: LeaveRoomUsecase.Storage {
    val roomsDao = RoomsDao()

    override fun leaveRoom(userId: Int, roomId: Int) {
        roomsDao.leaveRoom(roomId, userId)
    }
}